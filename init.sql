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

-- Create Main Categories table
CREATE TABLE maincategories (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Create Sub Categories table
CREATE TABLE subcategories (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    main_category_id INTEGER
);

-- Create Reporting Locations table
CREATE TABLE reportinglocations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create Status table
CREATE TABLE status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
)
