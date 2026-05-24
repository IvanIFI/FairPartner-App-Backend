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
  created_by_user_id BIGINT NOT NULL,
  name VARCHAR(20) NOT NULL,
  description VARCHAR(200),
  created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  icon VARCHAR(300) NOT NULL,
  amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
  PRIMARY KEY (id),
  CONSTRAINT expense_expense_group_fk FOREIGN KEY (id_expense_group) REFERENCES expense_group(id) ON DELETE CASCADE,
  CONSTRAINT expense_category_fk FOREIGN KEY (id_category) REFERENCES category(id),
  CONSTRAINT expense_user_fk FOREIGN KEY (created_by_user_id) REFERENCES users(id)
);


CREATE TABLE payment(
  id_user BIGINT NOT NULL,
  id_expense BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
  PRIMARY KEY (id_user, id_expense),
  CONSTRAINT payment_user_fk FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT payment_expense_fk FOREIGN KEY (id_expense) REFERENCES expense(id) ON DELETE CASCADE
);

CREATE TABLE expense_share(
  id_user BIGINT NOT NULL,
  id_expense BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
  PRIMARY KEY (id_user, id_expense),
  CONSTRAINT exp_shared_user_fk FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT exp_shared_expense_fk FOREIGN KEY (id_expense) REFERENCES expense(id) ON DELETE CASCADE
);

CREATE TABLE settlement(
  id BIGINT AUTO_INCREMENT,
  id_expense_group BIGINT NOT NULL,
  from_id_user BIGINT NOT NULL,
  to_id_user BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
  created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT settlement_from_user_fk FOREIGN KEY (from_id_user) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT settlement_to_user_fk FOREIGN KEY (to_id_user) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT settlement_expense_group_fk FOREIGN KEY (id_expense_group) REFERENCES expense_group(id) ON DELETE CASCADE
);
