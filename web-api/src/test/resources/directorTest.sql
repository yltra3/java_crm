truncate table clausdb.delivery_order;
truncate table clausdb.courier;
truncate table clausdb.users;


INSERT INTO clausdb.users (id, first_name, last_name, username, password, role, enabled)
VALUES (3, 'Vitalya', 'Деливерович', 'CeliveryDlub', '$2a$04$1JCzirMVTQQR4D0pa9ucl.TCdrreIHDYbNADTbUmsibwbBfPmrR7S',
        'ROLE_COURIER', 1);
INSERT INTO clausdb.users (id, first_name, last_name, username, password, role, enabled)
VALUES (4, 'Stas', 'Сиземов', 'BruhSorta', '$2a$04$1JCzirMVTQQR4D0pa9ucl.TCdrreIHDYbNADTbUmsibwbBfPmrR7S',
        'ROLE_COURIER', 1);

INSERT INTO clausdb.courier (id, courier_id, active_deliveries, times_delivered, times_failed, efficiency)
VALUES (1, 3, 10, 5, 2, null);
INSERT INTO clausdb.courier (id, courier_id, active_deliveries, times_delivered, times_failed, efficiency)
VALUES (2, 4, 30, 2, 10, null);






