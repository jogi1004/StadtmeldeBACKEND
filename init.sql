-- Create Reporting Locations table
CREATE TABLE reportinglocations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create Status table
CREATE TABLE status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    reporting_location_id INTEGER REFERENCES reportinglocations(id) NOT NULL
)

-- Create Main Categories table
CREATE TABLE maincategories (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    reporting_location_id INTEGER REFERENCES reportinglocations(id) NOT NULL
);

-- Create Sub Categories table
CREATE TABLE subcategories (
    id SERIAL PRIMARY KEY,
    main_category_id INTEGER REFERENCES maincategories(id) NOT NULL,
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
    notifications_enabled BOOLEAN
);

-- Create User Roles table
CREATE TABLE user_roles (
    user_id INTEGER REFERENCES users(id),
    role_id INTEGER REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE reportPictures (
    id SERIAL PRIMARY KEY,
    picture BYTEA
);

-- Create Messages table
CREATE TABLE reports (
    id SERIAL PRIMARY KEY,
    category_id INTEGER REFERENCES subcategories(id) NOT NULL,
    user_id INTEGER,
    title VARCHAR(255),
    description TEXT,
    longitude FLOAT,
    latitude FLOAT,
    reporting_location_id INTEGER REFERENCES reportinglocations(id) NOT NULL,
    status_id INTEGER REFERENCES status(id),
    additional_picture BYTEA
);

-- add reportinglocations
INSERT INTO reportinglocations (name)
VALUES ('Zweibrücken');

-- add maincategories
INSERT INTO maincategories (title, reporting_location_id)
VALUES ('Straßenschäden', 1),
       ('Gebäudeschäden', 1),
       ('Öffentliche Einrichtungen', 1),
       ('Grünanlagen', 1),
       ('Abfall und Umweltverschmutzung', 1),
       ('Verkehrsschilder und Markierungen', 1),
       ('Öffentlicher Nahverkehr', 1),
       ('Versorgungsleitungen', 1);

-- add subcategories
INSERT INTO subcategories (main_category_id, title)
VALUES (1, 'Schlaglöcher'),
       (1, 'Risse im Asphalt'),
       (1, 'Beschädigte Bordsteine'),
       (1, 'Sonstiges'),
       (2, 'Zerbrochene Fenster'),
       (2, 'Beschädigte Fassaden'),
       (2, 'Eingestürzte Dächer'),
       (2, 'Sonstiges'),
       (3, 'Defekte Straßenlampen'),
       (3, 'Kaputte Parkbänke'),
       (3, 'Beschädigte Spielplätze'),
       (3, 'Sonstiges'),
       (4, 'Umgestürzte Bäume'),
       (4, 'Beschädigte Zäune'),
       (4, 'Verwüstete Blumenbeete'),
       (4, 'Sonstiges'),
       (5, 'Müllablagerungen'),
       (5, 'Graffiti'),
       (5, 'Verschmutzte Gewässer'),
       (5, 'Sonstiges'),
       (6, 'Fehlende Verkehrsschilder'),
       (6, 'Unleserliche Markierungen'),
       (6, 'Beschädigte Leitpfosten'),
       (6, 'Sonstiges'),
       (7, 'Defekte Bushaltestellen'),
       (7, 'Störungen im Bahnverkehr'),
       (7, 'Verspätete Busse'),
       (7, 'Sonstiges'),
       (8, 'Wasserrohrbrüche'),
       (8, 'Stromausfälle'),
       (8, 'Gaslecks'),
       (8, 'Sonstiges');

-- add status
INSERT INTO status (name, reporting_location_id)
VALUES ('Neu', 1),
       ('In Bearbeitung', 1),
       ('Abgeschlossen', 1),
       ('Abgelehnt', 1);

-- Insert roles
INSERT INTO roles (name) VALUES ('USER'), ('ADMIN');