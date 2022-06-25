create schema if not exists clausdb;
create table if not exists clausdb.users
(
    id         int not null primary key,
    first_name varchar(50),
    last_name  varchar(50),
    username   varchar(50),
    password   varchar(150),
    role       varchar(20),
    enabled    tinyint
);
create table if not exists clausdb.warehouse
(
    id           int         not null primary key,
    description  varchar(50) null,
    is_available tinyint     null
);
create table if not exists clausdb.client
(
    id                  int not null primary key,
    address             varchar(50) null,
    first_name          varchar(20) null,
    last_name           varchar(20) null,
    email               varchar(20) null,
    parent_email        varchar(20) null,
    behaviour           varchar(10) null,
    country             varchar(30) null,
    last_time_delivered datetime    null
);

create table if not exists clausdb.parent_response
(
    id           int primary key,
    children_id  int      null,
    behaviour    varchar(10) null,
    date_created datetime   null,
    constraint parent_response_ibfk_1
        foreign key (children_id) references  clausdb.client (id)
            on delete cascade
);


create table if not exists clausdb.courier
(
    id                int primary key,
    courier_id        int  null,
    active_deliveries bigint  null,
    times_delivered   bigint  null,
    times_failed      bigint  null,
    efficiency        decimal null,
    constraint courier_ibfk_1
        foreign key (courier_id) references users (id)
);

create table if not exists clausdb.delivery_order
(
    id           int primary key,
    country      varchar(30) null,
    children_id  int      null,
    address      varchar(50) null,
    date_created datetime   null,
    is_actual    tinyint  null,
    courier_id   bigint      null,
    gift_id      bigint      null,
    constraint delivery_order_ibfk_1
        foreign key (children_id) references clausdb.client (id)
);












