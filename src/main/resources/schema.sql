create database if not exists nft_business;
use nft_business;

create table IF NOT EXISTS collection(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(64) NOT NULL,
    icon varchar(1024) NOT NULL,
    category int not null,
    domain_name varchar(128) not null,
    contract_chain varchar(128) NOT NULL,
    symbol varchar(32) not null,
    total_supply int not null,
    mint_price decimal not null,
    description varchar(1024) not null,
    airdrop_retention int not null,
    retained_qty int,
    revenue decimal NOT NULL,
    address varchar(128) NOT NULL,
    minted_qty int NOT NULL,
    status int NOT NULL, -- 1-drafted, 2-pending, 3-published, 4-suspended
    contract_status int not null -- 1-init, 2-pending, 3-published, 4-failed
);

create table if not exists folder(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(64) not null,
    icon varchar(1024) not null
);

create table if not exists nft(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    path varchar(1024) not null,
    status int not null -- 1-disable, 2-enable
);

create table if not exists folder_nft(
    id bigint primary key auto_increment,
    folder_id bigint,
    nft_id bigint,
    s3_folder_name varchar(64)
);
create unique index index_folder_nft_nft_id on folder_nft (nft_id);
create index index_folder_nft_folder_id on folder_nft (folder_id);

create table if not exists collection_folder_nft(
    id bigint primary key auto_increment,
    collection_id bigint,
    folder_id bigint,
    nft_id bigint
);
create unique index index_collection_folder_nft_nft_id on collection_folder_nft (nft_id);
create index index_collection_folder_nft_folder_id on collection_folder_nft (folder_id);
create index index_collection_folder_nft_collection_id on collection_folder_nft (collection_id);

create table IF NOT EXISTS draft_collection(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(64),
    icon varchar(1024),
    category int not null,
    domain_name varchar(128),
    contract_chain varchar(128),
    symbol varchar(32) ,
    total_supply int,
    mint_price decimal,
    description varchar(1024),
    airdrop_retention int,
    retained_qty int,
    status int NOT NULL -- 1-drafted, 2-pending, 3-published, 4-suspended
);

create table if not exists draft_collection_folder_nft(
    id bigint primary key auto_increment,
    collection_id bigint,
    folder_id bigint,
    nft_id bigint,
    nft_status int -- 1-disable, 2-enable
);
create unique index index_draft_collection_folder_nft_nft_id on draft_collection_folder_nft (nft_id);
create index index_draft_collection_folder_nft_folder_id on draft_collection_folder_nft (folder_id);
create index index_draft_collection_folder_nft_collection_id on draft_collection_folder_nft (collection_id);
