--Database reset
DROP DATABASE guestbook;
DROP USER admin;

--User creation
CREATE USER admin WITH ENCRYPTED PASSWORD 'admin';

--Database creation
CREATE DATABASE guestbook WITH OWNER = admin;
GRANT CONNECT ON DATABASE guestbook TO admin;

--Connection to new created database
\c guestbook admin localhost

--Tables creation
CREATE TABLE
IF NOT EXISTS entry (
id SERIAL PRIMARY KEY,
content TEXT,
author TEXT,
date TEXT
);

CREATE TABLE
IF NOT EXISTS
login_data (
user_name TEXT,
password TEXT
);

--Insert default user
INSERT INTO login_data(user_name, password)
VALUES ('admin', 'admin');
