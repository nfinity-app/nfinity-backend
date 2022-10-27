create database if not exists nft_business;
use nft_business;

create table IF NOT EXISTS collection(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(64),
    icon varchar(256),
    category int,
    domain_name varchar(128),
    contract_chain varchar(128),
    symbol varchar(32),
    total_supply int,
    mint_price decimal(40, 18),
    description varchar(256),
    airdrop_retention int,
    retained_qty int,
    revenue decimal(40, 18),
    address varchar(128),
    minted_qty int,
    status int NOT NULL comment '1-drafted, 2-pending, 3-published, 4-suspended, 5-failed',
    tx_status int comment '1-init, 2-pending, 3-published, 4-failed',
    create_time timestamp not null,
    update_time timestamp not null
);

create table IF NOT EXISTS collection_action
(
    id            bigint PRIMARY KEY AUTO_INCREMENT,
    collection_id bigint,
    tx_type       int,
    tx_status     int comment '1-init, 2-pending, 3-published, 4-failed',
    create_time   timestamp,
    update_time   timestamp
);
create index collection_action_collection_id on collection_action(collection_id);

create table if not exists folder(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(64) not null,
    icon varchar(256) not null,
    mint_status int not null comment '1-init, 2-deployed',
    create_time timestamp not null,
    update_time timestamp not null
);

create table if not exists nft(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    path varchar(256) not null,
    mint_status int not null comment '1-init, 2-deployed, 3-minting, 4-minted',
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
    nft_status int not null comment '1-disable, 2-enable',
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
    photo varchar(256),
    status int not null comment '1-disable, 2-enable',
    address_status int not null comment '1-disable, 2-enable',
    vault_id varchar(32) comment 'fireblocks vault id',
    create_time timestamp not null,
    update_time timestamp not null
);
create unique index index_user_email on user (email);
create unique index index_user_username on user (username);

create table if not exists business_info
(
    id bigint primary key auto_increment,
    name varchar(64) not null,
    type int not null comment '1-business, 2-personal',
    category int not null comment '1-Apparel, 2-Beauty, 3-Fashion, 4-Influencer',
    birth_date datetime not null,
    logo varchar(256),
    bio varchar(1024),
    user_id bigint not null,
    website varchar(128),
    email varchar(64),
    phone_number varchar(20),
    address varchar(1024),
    facebook varchar(128),
    twitter varchar(128),
    instagram varchar(128),
    youtube varchar(128),
    medium varchar(128),
    create_time timestamp,
    update_time timestamp
);
create unique index index_business_info_user_id on business_info (user_id);

create table if not exists `order`
(
    id bigint primary key auto_increment,
    collection_id bigint,
    user_id bigint,
    payment_id varchar(64),
    mint_qty int,
    amount decimal(40, 18),
    status int comment '1-unpaid, 2-succeed, 3-failed, 4-cancelled',
    tx_status int comment '1-init, 2-pending, 3-published, 4-failed',
    type int, -- 1- , 2-
    create_time timestamp,
    update_time timestamp
);
create index index_order_collection_id on `order`(collection_id);
create index index_order_user_id on `order`(user_id);
create unique index index_order_payment_id on `order`(payment_id);

create table if not exists order_nft
(
    id bigint primary key auto_increment,
    order_id bigint,
    nft_id bigint,
    token_id bigint,
    user_address varchar(64),
    create_time timestamp,
    update_time timestamp
);
create index index_order_nft_order_id on order_nft(order_id);
create index index_order_nft_nft_id on order_nft(nft_id);
