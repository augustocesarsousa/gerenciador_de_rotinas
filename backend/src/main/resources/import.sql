INSERT INTO tb_role (authority, description) VALUES ('ROLE_ADMIN', 'Administrador');
INSERT INTO tb_role (authority, description) VALUES ('ROLE_FINANCIAL_MANAGER', 'Gerente Financeiro');
INSERT INTO tb_role (authority, description) VALUES ('ROLE_FINANCIAL_ASSISTANT', 'Assistente Financeiro');
INSERT INTO tb_role (authority, description) VALUES ('ROLE_ADMINISTRATIVE_MANAGER', 'Gerente Administrativo');
INSERT INTO tb_role (authority, description) VALUES ('ROLE_ADMINISTRATIVE_ASSISTANT', 'Assistente Administrativo');

INSERT INTO tb_user (name, login, password, email, status, created_at, updated_at, user_id_edit) VALUES ('Bruce Wayne', 'batman', '1234', 'bruce.wayne@email.com', 0, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_user (name, login, password, email, status, created_at, updated_at, user_id_edit) VALUES ('Clark Kent', 'superman', '1234', 'clark.kent@email.com', 0, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_user (name, login, password, email, status, created_at, updated_at, user_id_edit) VALUES ('Martha Wayne', 'martha', '1234', 'martha.wayne@email.com', 1, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_user (name, login, password, email, status, created_at, updated_at, user_id_edit) VALUES ('Louis Lane', 'louis.lane', '1234', 'loys.lane@email.com', 0, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_user (name, login, password, email, status, created_at, updated_at, user_id_edit) VALUES ('Lex Luthor', 'luthor', '1234', 'lex.luthor@email.com', 1, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_user (name, login, password, email, status, created_at, updated_at, user_id_edit) VALUES ('Alfred', 'alfred', '1234', 'alfred@email.com', 0, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 3);
INSERT INTO tb_user_role (user_id, role_id) VALUES (4, 4);
INSERT INTO tb_user_role (user_id, role_id) VALUES (5, 5);
INSERT INTO tb_user_role (user_id, role_id) VALUES (6, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (6, 4);