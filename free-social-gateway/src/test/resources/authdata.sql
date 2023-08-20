INSERT INTO free_social_user(id, enabled, uuid) VALUES (1, false, '123');
INSERT INTO user_authentication(id, username, password, user_id) VALUES (2, 'tester1',
                                                                         '$2a$10$q.K3vE8WQEJKzDgnWBfe7O5Zwl2e1lz8UulpsBUlqYlcFa.Wa4NRG', 1);

INSERT INTO free_social_user(id, enabled, uuid) VALUES (3, true, '321');
INSERT INTO user_authentication(id, username, password, user_id) VALUES (4, 'joseph',
                                                                         '$2a$10$q.K3vE8WQEJKzDgnWBfe7O5Zwl2e1lz8UulpsBUlqYlcFa.Wa4NRG', 3);