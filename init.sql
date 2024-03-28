-- Create Reporting Locations table
CREATE TABLE reportinglocations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create Status table
CREATE TABLE status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create Main Categories table
CREATE TABLE maincategories (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Create Sub Categories table
CREATE TABLE subcategories (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Create Role table
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create User table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_picture BYTEA,
    notifications_enabled BOOLEAN,
    reporting_location_id INTEGER
);

-- Create User Roles table
CREATE TABLE user_roles (
    user_id INTEGER,
    role_id INTEGER,
    PRIMARY KEY (user_id, role_id)
);


-- Create Messages table
CREATE TABLE reports (
    id SERIAL PRIMARY KEY,
    category_id INTEGER,
    user_id INTEGER,
    title VARCHAR(255),
    description TEXT,
    longitude FLOAT,
    latitude FLOAT,
    status_id INTEGER,
    additional_picture BYTEA
);

-- Insert roles
INSERT INTO roles (name) VALUES ('USER'), ('ADMIN');

-- Insert reporting location
INSERT INTO reportinglocations (name) VALUES ('Zweibrücken');
