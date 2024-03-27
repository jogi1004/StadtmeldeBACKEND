-- Create User table
CREATE TABLE "User" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_picture BYTEA,
    notifications_enabled BOOLEAN,
    reporting_location_id INTEGER
);

-- Create Messages table
CREATE TABLE Reports (
    id SERIAL PRIMARY KEY,
    category_id INTEGER,
    user_id INTEGER,
    title VARCHAR(255),
    longitude FLOAT,
    latitude FLOAT,
    additional_picture BYTEA
);

-- Create Main Categories table
CREATE TABLE MainCategories (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Create Sub Categories table
CREATE TABLE SubCategories (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    main_category_id INTEGER
);

-- Create Reporting Locations table
CREATE TABLE ReportingLocations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);
