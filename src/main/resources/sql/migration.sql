DROP TABLE IF EXISTS OFFER;
DROP TABLE IF EXISTS CAR;
DROP TABLE IF EXISTS CLIENT;

-- Create CLIENT table
CREATE TABLE CLIENT (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    age INT,
    has_accidents BOOLEAN,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Insert records into CLIENT table
INSERT INTO CLIENT (name, address, phone, age, has_accidents, is_deleted)
VALUES
    ('Ivan Petrov', 'Plovdiv', '0888123456', 32, FALSE, FALSE),
    ('Maria Dimitrova', 'Sofia', '0899765432', 27, TRUE, FALSE),
    ('Georgi Ivanov', 'Varna', '0877654321', 40, FALSE, FALSE),
    ('Elena Nikolova', 'Burgas', '0888345678', 35, TRUE, FALSE),
    ('Petar Stoyanov', 'Plovdiv', '0898456123', 29, FALSE, FALSE),
    ('Tanya Popova', 'Sofia', '0897554322', 23, FALSE, FALSE),
    ('Dimitar Kolev', 'Varna', '0876345123', 45, TRUE, FALSE),
    ('Stanimir Todorov', 'Burgas', '0889456234', 38, FALSE, FALSE);

-- Create CAR table
CREATE TABLE CAR (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(255) NOT NULL,
    color VARCHAR(50),
    city VARCHAR(100) NOT NULL,
    rent_per_day DECIMAL(10, 2),
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Insert records into CAR table
INSERT INTO CAR (model, color, city, rent_per_day, is_deleted)
VALUES
    ('Toyota Corolla', 'White', 'Plovdiv', 40.50, FALSE),
    ('Ford Fiesta', 'Red', 'Sofia', 35.00, FALSE),
    ('BMW 3 Series', 'Black', 'Varna', 80.00, FALSE),
    ('Volkswagen Golf', 'Blue', 'Burgas', 50.75, FALSE),
    ('Hyundai Tucson', 'Silver', 'Plovdiv', 60.00, FALSE),
    ('Renault Clio', 'Yellow', 'Sofia', 30.00, FALSE),
    ('Audi A4', 'Gray', 'Varna', 90.25, FALSE),
    ('Kia Sportage', 'Green', 'Burgas', 70.00, FALSE);

-- Create OFFER table
CREATE TABLE OFFER (
    id INT AUTO_INCREMENT PRIMARY KEY,
    car_id INT NOT NULL,
    client_id INT NOT NULL,
    week_days INT NOT NULL,
    weekend_days INT NOT NULL,
    total_cost DOUBLE NOT NULL,
    is_accepted INT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Insert records into OFFER table
INSERT INTO OFFER (car_id, client_id, week_days, weekend_days, total_cost, is_accepted, is_deleted)
VALUES
    -- Plovdiv Offers
    (1, 1, 5, 2, (40.50 * 5) + (40.50 * 2 * 1.1), 1, FALSE),
    (5, 5, 3, 1, (60.00 * 3) + (60.00 * 1 * 1.1), 0, FALSE),

    -- Sofia Offers
    (2, 2, 4, 2, (35.00 * 4) + (35.00 * 2 * 1.1) + 200, 1, FALSE),
    (6, 6, 6, 0, (30.00 * 6) + (30.00 * 0 * 1.1), 1, FALSE),

    -- Varna Offers
    (3, 3, 5, 2, (80.00 * 5) + (80.00 * 2 * 1.1), 0, FALSE),
    (7, 7, 2, 1, (90.25 * 2) + (90.25 * 1 * 1.1) + 200, 1, FALSE),

    -- Burgas Offers
    (4, 4, 3, 2, (50.75 * 3) + (50.75 * 2 * 1.1) + 200, 0, FALSE),
    (8, 8, 4, 1, (70.00 * 4) + (70.00 * 1 * 1.1), 1, FALSE);
