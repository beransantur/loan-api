insert into user (id, name, surname, username, password, created_at) VALUES (1, 'adminname', 'adminsurname', 'admin', '$2a$12$j.QqkmTmaykoCLr.tLHuG.8mpLMH3F9OJaROPBWWiP168Go9IpEnG', NOW());
insert into user (id, name, surname, username, password, created_at) VALUES (2, 'beran', 'santur', 'beransantur161', '$2a$12$j.QqkmTmaykoCLr.tLHuG.8mpLMH3F9OJaROPBWWiP168Go9IpEnG', NOW());

insert into customer (id, credit_limit, used_credit_limit, user_id) VALUES (1, '5000.10', '2000.10', 2);

insert into role(id, name, description, created_at) values (1, 'ADMIN', 'Administrator of the system', NOW()), (2, 'CUSTOMER', 'Customer of the bank', NOW());

INSERT INTO user_role (user_id, role_id) VALUES (1, 1), (2, 2);

insert into authority(id, name, description, created_at) values (1, 'create-loan', 'Create loan', NOW()), (2, 'read-loan', 'Read loan detail', NOW()),
                                                    (3, 'read-installments', 'Read loan installments', NOW()), (4, 'pay-loan', 'Pay loan by installments', NOW());
INSERT INTO role_authority (role_id, authority_id) VALUES (1, 1), (1, 2), (1, 3), (1, 4), (2, 1), (2, 2);

