truncate table ClausDB.users;

INSERT INTO ClausDB.users (id, first_name, last_name, username, password, role, enabled)
VALUES (1, 'Санта', 'Клаус', 'BigClaus', '$2a$10$mANtdlnEmaE8gTE5ZjD4relEQQ0cecP89RF.VyZD2ZkIXqAyby.4u',
        'ROLE_DIRECTOR', 1);
INSERT INTO ClausDB.users (id, first_name, last_name, username, password, role, enabled)
VALUES (2, 'Эльф', 'Росс', 'SortaSorter', '$2a$04$thVEOW2lXtfw2taZgkMmR.zcmykYeNR6mUNgmJVvGSCeWrjbrwIxy', 'ROLE_SORTER',
        1);
INSERT INTO ClausDB.users (id, first_name, last_name, username, password, role, enabled)
VALUES (3, 'Виталя', 'Деливерович', 'CeliveryDlub', '$2a$04$1JCzirMVTQQR4D0pa9ucl.TCdrreIHDYbNADTbUmsibwbBfPmrR7S',
        'ROLE_COURIER', 1);
INSERT INTO ClausDB.users (id, first_name, last_name, username, password, role, enabled)
VALUES (4, 'Стас', 'Сиземов', 'BruhSorta', '$2a$04$1JCzirMVTQQR4D0pa9ucl.TCdrreIHDYbNADTbUmsibwbBfPmrR7S',
        'ROLE_COURIER', 1);