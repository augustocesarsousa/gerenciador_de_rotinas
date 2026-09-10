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

INSERT INTO tb_person (name, type, status, cpf, cnpj, address, number, neighborhood, city, state, zipcode, phone, email, created_at, updated_at, user_id_edit) VALUES ('Bruce Wayne', 'PHYSICAL', 0, '28142131056', null, 'Gotham Crest', 1007, 'Crest Hill', 'Gotham City', 'NJ', '07001000', '11988887777', 'bruce.wayne@waynecorp.com', '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_person (name, type, status, cpf, cnpj, address, number, neighborhood, city, state, zipcode, phone, email, created_at, updated_at, user_id_edit) VALUES ('Clark Kent', 'PHYSICAL', 0, '72595391003', null, 'Rua de Smallville', 10, 'Rural', 'Metropolis', 'KS', '66001000', '11999998888', 'clark.kent@dailyplanet.com', '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_person (name, type, status, cpf, cnpj, address, number, neighborhood, city, state, zipcode, phone, email, created_at, updated_at, user_id_edit) VALUES ('Wayne Enterprises Inc', 'LEGAL', 0, null, '40819005000165', 'Wayne Tower, Diamond District', 1, 'Downtown', 'Gotham City', 'NJ', '07002000', '1133334444', 'contact@wayneenterprises.com', '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_person (name, type, status, cpf, cnpj, address, number, neighborhood, city, state, zipcode, phone, email, created_at, updated_at, user_id_edit) VALUES ('LexCorp Industries', 'LEGAL', 0, null, '46159347000182', 'LexCorp Tower', 500, 'Midtown', 'Metropolis', 'KS', '66002000', '1122223333', 'contact@lexcorp.com', '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);

INSERT INTO tb_bank (code, ispb, name, short_name, status, created_at, updated_at, user_id_edit) VALUES ('001', '00000000', 'Banco do Brasil S.A.', 'Banco do Brasil', 0, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_bank (code, ispb, name, short_name, status, created_at, updated_at, user_id_edit) VALUES ('104', '00360305', 'Caixa Econômica Federal', 'Caixa', 0, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_bank (code, ispb, name, short_name, status, created_at, updated_at, user_id_edit) VALUES ('237', '60746948', 'Banco Bradesco S.A.', 'Bradesco', 0, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_bank (code, ispb, name, short_name, status, created_at, updated_at, user_id_edit) VALUES ('341', '60701190', 'Itaú Unibanco S.A.', 'Itaú', 0, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);
INSERT INTO tb_bank (code, ispb, name, short_name, status, created_at, updated_at, user_id_edit) VALUES ('260', '18236120', 'Nu Pagamentos S.A.', 'Nubank', 0, '2025-01-01 00:00:00.000000', '2025-01-01 00:00:00.000000', 1);