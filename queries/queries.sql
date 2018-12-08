--Database reset
DROP DATABASE guestbook;
DROP USER ADMIN;

--User creation
CREATE USER ADMIN WITH ENCRYPTED PASSWORD 'admin';

--Database creation
CREATE DATABASE guestbook WITH OWNER = ADMIN;
GRANT CONNECT ON DATABASE guestbook TO ADMIN;

--Connection to new created database
\c guestbook ADMIN localhost

--Tables creation
CREATE TABLE
IF NOT EXISTS entry (
id SERIAL PRIMARY KEY,
content TEXT,
author TEXT,
DATE TEXT
);

CREATE TABLE
IF NOT EXISTS
login_data (
id SERIAL PRIMARY KEY,
user_name TEXT,
password TEXT
);CREATE TABLE IF NOT EXISTS session(
user_id INTEGER,
session_id TEXT UNIQUE
);--Insert default users
INSERT INTO login_data(user_name, password)
VALUES('admin', 'admin'),('daro123', 'daro123');
