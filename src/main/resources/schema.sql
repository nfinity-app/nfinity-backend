create database if not exists nft_business;
use nft_business;

create table IF NOT EXISTS collection(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    user_id bigint not null,
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
    contract_type int comment '1-721, 2-1155',
    create_time timestamp not null,
    update_time timestamp not null
);
create index index_collection_user_id on collection (user_id);

create table IF NOT EXISTS collection_action
(
    id            bigint PRIMARY KEY AUTO_INCREMENT,
    collection_id bigint,
    param_key       varchar(32),
    param_value     varchar(64),
    tx_status     int comment '1-init, 2-pending, 3-published, 4-failed',
    create_time   timestamp,
    update_time   timestamp
);
create index index_collection_action_collection_id on collection_action(collection_id);

create table if not exists folder(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    user_id bigint not null,
    name varchar(64) not null,
    icon varchar(256) not null,
    mint_status int not null comment '1-init, 2-deployed',
    create_time timestamp not null,
    update_time timestamp not null
);
create index index_folder_user_id on folder (user_id);

create table if not exists nft(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    user_id bigint not null,
    path varchar(256) not null,
    mint_status int not null comment '1-init, 2-deployed, 3-minting, 4-minted',
    create_time timestamp not null,
    update_time timestamp not null
);
create index index_nft_user_id on nft (user_id);

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
    google_auth_status int comment '1-disable, 2-enable',
    google_auth_key varchar(64),
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
    lat decimal(40, 18),
    lng decimal(40, 18),
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
    type int comment '1-public sale, 2-airdrop, 3-pre-sale',
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

create table if not exists loyalty_program
(
    id bigint primary key auto_increment,
    user_id bigint not null,
    status int comment '1-disable(drafted), 2-enable(done)',
    banner varchar(128),
    title varchar(64),
    description varchar(500),
    unlockable_points int comment '1-disable, 2-enable',
    unlockable_per_claim_points int,
    unlockable_per_video_watch_points int,
    youtube_video int comment '1-disable, 2-enable',
    youtube_per_video_watch_points int,
    youtube_per_event_check_points int,
    max_unlockable_claim_per_month_points int,
    max_video_watch_per_month_points int,
    max_youtube_video_per_month_points int,
    max_event_check_per_month_points int,
    tiers_creation int comment '1-disable, 2-enable',
    total_tier int,
    points_redeem_required_for_tier_upgrade int,
    redeem_ticket_points int,
    redeem_coupon_points int,
    points_expiration int comment '1-disable, 2-enable',
    expiration_months int,
    step int comment '1-create program, 2-collection rewards, 3-unlockable rewards, 4-tiers setting, 5-expiration setting, 6-done',
    create_time timestamp,
    update_time timestamp
);
create index index_loyalty_program_user_id on loyalty_program (user_id);

create table if not exists loyalty_program_collection
(
    id bigint primary key auto_increment,
    program_id bigint not null,
    collection_id bigint not null,
    nft_rewards int comment '1-disable, 2-enable',
    nft_rewards_points int,
    allow_repetitive_counting int comment '1-disable, 2-enable',
    max_repetitive_counting int,
    twitter_engagement int comment '1-disable, 2-enable',
    twitter_photo varchar(128),
    twitter_user_id varchar(64),
    twitter_username varchar(64),
    twitter_follow_points int,
    twitter_per_post_like_points int,
    instagram_engagement int comment '1-disable, 2-enable',
    instagram_photo varchar(128),
    instagram_user_id varchar(64),
    instagram_username varchar(64),
    instagram_follow_points int,
    create_time timestamp,
    update_time timestamp
);
create index index_loyalty_program_collection_program_id on loyalty_program_collection (program_id);
create index index_loyalty_program_collection_collection_id on loyalty_program_collection (collection_id);

create table if not exists tier
(
    id bigint primary key auto_increment,
    program_id bigint not null,
    name varchar(64),
    required_points int,
    create_time timestamp,
    update_time timestamp
);
create index index_tier_program_id on tier (program_id);

create table if not exists tier_user
(
    id bigint primary key auto_increment,
    tier_id bigint,
    user_id bigint comment 'C-end user',
    create_time timestamp,
    update_time timestamp
);
create index index_tier_user_tier_id on tier_user (tier_id);
create index index_tier_user_user_id on tier_user (user_id);

create table if not exists instagram_hashtag
(
    id bigint primary key auto_increment,
    program_id bigint not null,
    username varchar(64) comment 'instagram username',
    name varchar(64),
    per_like_points int,
    create_time timestamp,
    update_time timestamp
);
create index index_instagram_hashtag_username on instagram_hashtag (username);
create index index_instagram_hashtag_program_id on instagram_hashtag (program_id);
