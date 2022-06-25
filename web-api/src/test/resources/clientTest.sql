truncate table clausdb.parent_response;
truncate table clausdb.client;


insert into clausdb.client (id, address, first_name, last_name, email, parent_email,
                            behaviour, country,
                            last_time_delivered)
values (1, 'street1', 'name1', 'name1', 'dantab1@gmail.com', 'dantabx@gmail.com',
        'UNKNOWN',
        'russia', '1998-01-23 12:45:56');
insert into clausdb.client (id, address, first_name, last_name, email, parent_email,
                            behaviour, country,
                            last_time_delivered)
values (2, 'street2', 'name2', 'name2', 'dantab2@gmail.com', 'dantabz@gmail.com',
        'UNKNOWN',
        'russia',
        null);
insert into clausdb.client (id, address, first_name, last_name, email, parent_email,
                            behaviour, country,
                            last_time_delivered)
VALUES (3, 'street3', 'Name3', 'Name3', 'dantab3@gmail.com', 'dantaba@gmail.com',
        'UNKNOWN',
        'Russia',
        null);

insert into clausdb.parent_response (id, children_id, behaviour, date_created)
values (1, 3, 'GOOD', '2022-01-01 05:09:19');



