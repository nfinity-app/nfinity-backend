create database if not exists nft_business;
use nft_business;

create table IF NOT EXISTS collection(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(64),
    icon varchar(1024),
    category int,
    domain_name varchar(128),
    contract_chain varchar(128),
    symbol varchar(32),
    total_supply int,
    mint_price decimal(40, 18),
    description varchar(1024),
    airdrop_retention int,
    retained_qty int,
    revenue decimal(40, 18),
    address varchar(128),
    minted_qty int,
    status int NOT NULL, -- 1-drafted, 2-pending, 3-published, 4-suspended, 5-failed
    contract_status int, -- 1-init, 2-pending, 3-published, 4-failed
    create_time timestamp not null,
    update_time timestamp not null
);

create table if not exists folder(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(64) not null,
    icon varchar(1024) not null,
    mint_status int not null, -- 1-unminted, 2-minted
    create_time timestamp not null,
    update_time timestamp not null
);

create table if not exists nft(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    path varchar(1024) not null,
    mint_status int not null, -- 1-unminted, 2-minted
    create_time timestamp not null,
    update_time timestamp not null
);

create table if not exists folder_nft(
    id bigint primary key auto_increment,
    folder_id bigint,
    nft_id bigint,
    s3_folder_name varchar(64),
    create_time timestamp not null,
    update_time timestamp not null
);
create unique index index_folder_nft_nft_id on folder_nft (nft_id);
create index index_folder_nft_folder_id on folder_nft (folder_id);

create table if not exists collection_folder_nft(
    id bigint primary key auto_increment,
    collection_id bigint,
    folder_id bigint,
    nft_id bigint,
    nft_status int not null, -- 1-disable, 2-enable
    create_time timestamp not null,
    update_time timestamp not null
);
create index index_collection_folder_nft_nft_id on collection_folder_nft (nft_id);
create index index_collection_folder_nft_folder_id on collection_folder_nft (folder_id);
create index index_collection_folder_nft_collection_id on collection_folder_nft (collection_id);

create table if not exists user
(
    id bigint primary key auto_increment,
    email varchar(64) not null,
    username varchar(64),
    password varchar(128) not null,
    telephone varchar(64),
    photo varchar(1024),
    status int not null comment '1-disable, 2-enable',
    address_status int not null, -- 1-disable, 2-enable
    create_time timestamp not null,
    update_time timestamp not null
);
create unique index index_user_email on user (email);
create unique index index_user_user_name on user (user_name);

alter table collection modify revenue decimal(40, 18) null;
alter table collection modify mint_price decimal(40, 18) null;