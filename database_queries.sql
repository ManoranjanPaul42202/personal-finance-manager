CREATE DATABASE personal_finance_manager;
USE personal_finance_manager;

-- Users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL
);

-- Categories table
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Transactions table
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255),
    category_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

ALTER TABLE transactions
ADD COLUMN type VARCHAR(10) NOT NULL AFTER description;
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255),
    category_id INT,
    type VARCHAR(10) NOT NULL, 
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
ALTER TABLE categories
ADD COLUMN type VARCHAR(10) NOT NULL AFTER name;
INSERT INTO categories (name, type) VALUES 
('Salary', 'Income'),
('Freelancing', 'Income'),
('Food', 'Expense'),
('Rent', 'Expense'),
('Utilities', 'Expense'),
('Entertainment', 'Expense');

SELECT * FROM users;

SELECT * FROM categories;

SELECT * FROM transactions;