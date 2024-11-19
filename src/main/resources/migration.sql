-- Create the CLIENT table

DROP TABLE IF EXISTS CLIENT;
DROP TABLE IF EXISTS CAR;
DROP TABLE IF EXISTS OFFER;

CREATE TABLE CLIENT (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    age INT,
    has_accidents BOOLEAN
);

INSERT INTO CLIENT (name, address, phone, age, has_accidents)
VALUES
    ('Ivan Petrov', 'Plovdiv', '0888123456', 32, FALSE),
    ('Maria Dimitrova', 'Sofia', '0899765432', 27, TRUE),
    ('Georgi Ivanov', 'Varna', '0877654321', 40, FALSE),
    ('Elena Nikolova', 'Burgas', '0888345678', 35, TRUE),
    ('Petar Stoyanov', 'Plovdiv', '0898456123', 29, FALSE),
    ('Tanya Popova', 'Sofia', '0897554322', 23, FALSE),
    ('Dimitar Kolev', 'Varna', '0876345123', 45, TRUE),
    ('Stanimir Todorov', 'Burgas', '0889456234', 38, FALSE);

    CREATE TABLE CAR (
        id INT AUTO_INCREMENT PRIMARY KEY,
        model VARCHAR(255) NOT NULL,
        color VARCHAR(50),
        city VARCHAR(100) NOT NULL,
        rent_per_day DECIMAL(10, 2)
    );

INSERT INTO CAR (model, color, city, rent_per_day) VALUES
('Toyota Corolla', 'White', 'Plovdiv', 40.50),
('Ford Fiesta', 'Red', 'Sofia', 35.00),
('BMW 3 Series', 'Black', 'Varna', 80.00),
('Volkswagen Golf', 'Blue', 'Burgas', 50.75),
('Hyundai Tucson', 'Silver', 'Plovdiv', 60.00),
('Renault Clio', 'Yellow', 'Sofia', 30.00),
('Audi A4', 'Gray', 'Varna', 90.25),
('Kia Sportage', 'Green', 'Burgas', 70.00);

CREATE TABLE OFFER (
    offer_id INT AUTO_INCREMENT PRIMARY KEY,
    car_id INT NOT NULL,
    client_id INT NOT NULL,
    week_days INT NOT NULL,
    weekend_days INT NOT NULL,
    total_cost DOUBLE NOT NULL,
    is_accepted INT NOT NULL
);

INSERT INTO OFFER (car_id, client_id, week_days, weekend_days, total_cost, is_accepted)
VALUES
    -- Plovdiv Offers
    (1, 1, 5, 2, (40.50 * 5) + (40.50 * 2 * 1.1), 1), -- Ivan Petrov, no accidents
    (5, 5, 3, 1, (60.00 * 3) + (60.00 * 1 * 1.1), 0), -- Petar Stoyanov, no accidents

    -- Sofia Offers
    (2, 2, 4, 2, (35.00 * 4) + (35.00 * 2 * 1.1) + 200, 1), -- Maria Dimitrova, had accidents
    (6, 6, 6, 0, (30.00 * 6) + (30.00 * 0 * 1.1), 1),       -- Tanya Popova, no accidents

    -- Varna Offers
    (3, 3, 5, 2, (80.00 * 5) + (80.00 * 2 * 1.1), 0),       -- Georgi Ivanov, no accidents
    (7, 7, 2, 1, (90.25 * 2) + (90.25 * 1 * 1.1) + 200, 1), -- Dimitar Kolev, had accidents

    -- Burgas Offers
    (4, 4, 3, 2, (50.75 * 3) + (50.75 * 2 * 1.1) + 200, 0), -- Elena Nikolova, had accidents
    (8, 8, 4, 1, (70.00 * 4) + (70.00 * 1 * 1.1), 1);       -- Stanimir Todorov, no accidents


