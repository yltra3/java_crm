create database clausdb;
create user claus identified by password 'claus';
grant all on clausdb to claus;
create schema if not exists clausdb;
create table if not exists clausdb.users
(
    id         int not null auto_increment primary key,
    first_name varchar(50),
    last_name  varchar(50),
    username   varchar(50),
    password   varchar(250),
    role       varchar(20),
    enabled    tinyint
);
create table if not exists clausdb.warehouse
(
    id           int auto_increment not null primary key,
    description  varchar(50)        null,
    is_available tinyint            null
);
create table if not exists clausdb.client
(
    id                  int         not null auto_increment primary key,
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
    id           int         not null auto_increment primary key,
    children_id  int         null,
    behaviour    varchar(10) null,
    date_created timestamp   null,
    constraint parent_response_ibfk_1
        foreign key (children_id) references clausdb.client (id)
            on delete cascade
);

create index children_id
    on clausdb.parent_response (children_id);

create table if not exists clausdb.courier
(
    id                int     not null auto_increment primary key,
    courier_id        int     null,
    active_deliveries bigint  null,
    times_delivered   bigint  null,
    times_failed      bigint  null,
    efficiency        decimal null,
    constraint courier_ibfk_1
        foreign key (courier_id) references users (id)
);

create index courier_id on clausdb.courier (courier_id);
create table if not exists clausdb.delivery_order
(
    id           int         not null auto_increment primary key,
    country      varchar(30) null,
    children_id  int         null,
    address      varchar(50) null,
    date_created timestamp   null,
    is_actual    tinyint     null,
    courier_id   bigint      null,
    gift_id      bigint      null,
    constraint delivery_order_ibfk_1
        foreign key (children_id) references clausdb.client (id)
);


-- CLAUS1234
INSERT INTO clausdb.users (id, first_name, last_name, username, password, role, enabled)
VALUES (1, 'Санта', 'Клаус', 'BigClaus', '$2a$10$DnZ3cQimdyFjO4DygvRVOOvoOnheNGyDzmP.ABEsTwqT7069jJKTe',
        'ROLE_DIRECTOR', 1);
-- SORTER1234
INSERT INTO clausdb.users (id, first_name, last_name, username, password, role, enabled)
VALUES (2, 'Эльф', 'Росс', 'SortaSorter', '$2a$10$JdnI3toc.cUlM/KgSo5nVu36/PR/y/f5dSpnHVjNWme0rVmrEZxB.', 'ROLE_SORTER',
        1);
-- DELIVERY1234
INSERT INTO clausdb.users (id, first_name, last_name, username, password, role, enabled)
VALUES (3, 'Виталя', 'Деливерович', 'CeliveryDlub', '$2a$10$pQ406TAZ2iiZyRLuylOo2u8MXF0m3K8a2Q7jb43URV4aYF.GLjK4e',
        'ROLE_COURIER', 1);
-- STAS1234
INSERT INTO clausdb.users (id, first_name, last_name, username, password, role, enabled)
VALUES (4, 'Стас', 'Сиземов', 'BruhSorta', '$2a$10$s1674y/OKG7fEJEP51MZX.MaKphGPI5rDRY8jpmi3ZKvpzUVX.eLW',
        'ROLE_COURIER', 1);

INSERT INTO clausdb.courier (id, courier_id, active_deliveries, times_delivered, times_failed, efficiency)
VALUES (1, 3, 1, 0, 0, null);
INSERT INTO clausdb.courier (id, courier_id, active_deliveries, times_delivered, times_failed, efficiency)
VALUES (2, 4, 0, 1, 1, null);







