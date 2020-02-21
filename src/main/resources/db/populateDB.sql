DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (id, user_id, description, datetime, calories) VALUES
(1, 100000, 'Завтрак', '2020-02-12 07:30', 1500),
(2, 100000, 'Обед', '2020-02-12 13:30', 1500),
(3, 100000, 'Ужин', '2020-02-12 20:30', 1500),
(4, 100001, 'Обед', '2020-02-13 13:30', 1000),
(5, 100001, 'Ужин', '2020-02-13 20:30', 100),
(6, 100001, 'Завтрак', '2020-02-13 07:30', 500);