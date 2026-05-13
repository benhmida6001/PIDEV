CREATE DATABASE IF NOT EXISTS pidev_greencore;
USE pidev_greencore;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(180) NOT NULL UNIQUE,
    profile_picture VARCHAR(255),
    roles JSON NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS materiel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    quantity INT NOT NULL,
    image VARCHAR(255),
    status ENUM('EXCELLENT', 'GOOD', 'ACCEPTABLE', 'TO_CHECK') NOT NULL,
    available_quantity INT NOT NULL,
    category_id BIGINT NOT NULL,
    owner_id BIGINT NOT NULL,
    transport BOOLEAN DEFAULT FALSE,
    visible BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (owner_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS operation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('BORROW', 'REPAIR') NOT NULL,
    quantity INT NOT NULL,
    description TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at DATETIME NOT NULL,
    materiel_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    FOREIGN KEY (materiel_id) REFERENCES materiel(id) ON DELETE CASCADE,
    FOREIGN KEY (requester_id) REFERENCES user(id)
);

INSERT INTO category (name) VALUES 
('Électronique'),
('Outillage'),
('Jardinage'),
('Sport'),
('Bricolage'),
('Cuisine'),
('Nettoyage'),
('Autre');

INSERT INTO user (email, password, roles) VALUES 
('admin@greencore.com', '$2a$12$N9qo8uLOickgx2ZMRZoMye.IY4JTy5X3G5Q5R5Q5Q5Q5Q5Q5Q5Q5Q', '["ADMIN", "USER"]'),
('user@greencore.com', '$2a$12$N9qo8uLOickgx2ZMRZoMye.IY4JTy5X3G5Q5R5Q5Q5Q5Q5Q5Q5Q5Q', '["USER"]');

INSERT INTO materiel (name, description, quantity, status, available_quantity, category_id, owner_id, transport, visible) VALUES 
('Perceuse sans fil', 'Perceuse professionnelle avec batterie', 2, 'EXCELLENT', 1, 2, 1, true, true),
('Tondeuse à gazon', 'Tondeuse électrique pour petit jardin', 1, 'GOOD', 1, 3, 1, false, true),
('Ordinateur portable', 'PC portable 15 pouces pour travail', 1, 'EXCELLENT', 0, 1, 2, true, true);
