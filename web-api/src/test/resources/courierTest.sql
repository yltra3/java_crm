truncate table clausdb.delivery_order;
truncate table clausdb.warehouse;
truncate table clausdb.courier;
truncate table clausdb.users;
truncate table clausdb.parent_response;
truncate table clausdb.client;



INSERT INTO clausdb.users (id, first_name, last_name, username, password, role, enabled)
VALUES (3, 'Виталя', 'Деливерович', 'CeliveryDlub', '$2a$04$1JCzirMVTQQR4D0pa9ucl.TCdrreIHDYbNADTbUmsibwbBfPmrR7S',
        'ROLE_COURIER', 1);

INSERT INTO clausdb.courier (id, courier_id, active_deliveries, times_delivered, times_failed, efficiency)
VALUES (1, 3, 1, 0, 0, null);


insert into clausdb.client (id, address, first_name, last_name, email, parent_email,
                            behaviour, country,
                            last_time_delivered)
values (1, 'street1', 'name1', 'name1', 'dantab1@gmail.com', 'dantabx@gmail.com',
        'GOOD',
        'russia', '1998-01-23 12:45:56');
insert into clausdb.warehouse (id, description, is_available)
values (1, 'Gift №1463947152', 1);

INSERT INTO clausdb.delivery_order (id, country, children_id, address, date_created, is_actual, courier_id, gift_id)
VALUES (1, 'Russia', 1, 'купчага', '2022-01-19 05:12:53', 1, null, null);

insert into clausdb.parent_response (id, children_id, behaviour, date_created)
values (1, 1, 'GOOD', null);



