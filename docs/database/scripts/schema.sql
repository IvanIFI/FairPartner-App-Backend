DROP DATABASE IF EXISTS fairpartner_db;
CREATE DATABASE fairpartner_db;
USE fairpartner_db;


CREATE TABLE users(
  id BIGINT AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  registration_date DATE NOT NULL DEFAULT (CURRENT_DATE),
  PRIMARY KEY (id)
);


CREATE TABLE roles(
	id BIGINT AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL UNIQUE,
	PRIMARY KEY (id)
);


CREATE TABLE user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);


CREATE TABLE expense_group(
  id BIGINT AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  description VARCHAR(200),
  icon VARCHAR(300) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE invitation (
  id BIGINT AUTO_INCREMENT,
  expense_group_id BIGINT NOT NULL,
  inviter_user_id BIGINT NOT NULL,
  invited_user_id BIGINT NOT NULL,
  token VARCHAR(255) NOT NULL UNIQUE,
  creation_date DATETIME NOT NULL,
  expiration_date DATETIME NOT NULL,
  status VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT invitation_group_fk FOREIGN KEY (expense_group_id) REFERENCES expense_group(id) ON DELETE CASCADE,
  CONSTRAINT invitation_inviter_user_fk FOREIGN KEY (inviter_user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT invitation_invited_user_fk FOREIGN KEY (invited_user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE participates (
  id_user BIGINT NOT NULL,
  id_expense_group BIGINT NOT NULL,
  PRIMARY KEY (id_user, id_expense_group),
  CONSTRAINT participates_user_fk FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT participates_expense_group_fk FOREIGN KEY (id_expense_group) REFERENCES expense_group(id) ON DELETE CASCADE
);


CREATE TABLE category(
  id BIGINT AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  icon VARCHAR(300) NOT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE expense (
  id BIGINT AUTO_INCREMENT,
  id_expense_group BIGINT NOT NULL,
  id_category BIGINT NOT NULL,
  id_user BIGINT NOT NULL,
  name VARCHAR(20) NOT NULL,
  description VARCHAR(200),
  date DATE NOT NULL DEFAULT (CURRENT_DATE),
  icon VARCHAR(300) NOT NULL,
  cant DECIMAL(10,2) NOT NULL CHECK (cant > 0),
  PRIMARY KEY (id),
  CONSTRAINT expense_expense_group_fk FOREIGN KEY (id_expense_group) REFERENCES expense_group(id),
  CONSTRAINT expense_category_fk FOREIGN KEY (id_category) REFERENCES category(id),
  CONSTRAINT expense_user_fk FOREIGN KEY (id_user) REFERENCES users(id)
);


CREATE TABLE payment(
  id_user BIGINT NOT NULL,
  id_expense BIGINT NOT NULL,
  cant DECIMAL(10,2) NOT NULL CHECK (cant > 0),
  PRIMARY KEY (id_user, id_expense),
  CONSTRAINT payment_user_fk FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT payment_expense_fk FOREIGN KEY (id_expense) REFERENCES expense(id) ON DELETE CASCADE
);


CREATE TABLE pantry (
  id BIGINT AUTO_INCREMENT,
  description VARCHAR(200) NOT NULL,
  icon VARCHAR(300) NOT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE share (
  id_user BIGINT NOT NULL,
  id_pantry BIGINT NOT NULL,
  PRIMARY KEY (id_user, id_pantry),
  CONSTRAINT share_user_fk FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT share_pantry_fk FOREIGN KEY (id_pantry) REFERENCES pantry(id) ON DELETE CASCADE  
);


CREATE TABLE shopping_list (
  id BIGINT AUTO_INCREMENT,
  id_pantry BIGINT NOT NULL,
  name VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT shopping_list_pantry_fk FOREIGN KEY (id_pantry) REFERENCES pantry(id)  
);


CREATE TABLE product (
  id BIGINT AUTO_INCREMENT,
  id_pantry BIGINT NOT NULL,
  id_category BIGINT NOT NULL,
  id_shopping_list BIGINT,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(200),
  icon VARCHAR(300) NOT NULL,
  cant INT NOT NULL CHECK (cant >= 0),
  expiration_date DATE,
  PRIMARY KEY (id),
  CONSTRAINT product_pantry_fk FOREIGN KEY (id_pantry) REFERENCES pantry(id),
  CONSTRAINT product_category_fk FOREIGN KEY (id_category) REFERENCES category(id),
  CONSTRAINT product_shopping_list_fk FOREIGN KEY (id_shopping_list) REFERENCES shopping_list(id)
);