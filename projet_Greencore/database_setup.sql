-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS pidev;
USE pidev;

-- Create utilisateur table
CREATE TABLE IF NOT EXISTS utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    adrEmail VARCHAR(255) UNIQUE NOT NULL,
    numTel VARCHAR(20),
    adresse VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    mdp VARCHAR(255) NOT NULL,
    pointGagne INT DEFAULT 0
);

-- Create evenement table
CREATE TABLE IF NOT EXISTS evenement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    date_evenement DATE NOT NULL,
    lieu VARCHAR(255),
    capacite_max INT,
    points_recompense INT DEFAULT 0,
    type_evenement VARCHAR(50),
    statut VARCHAR(50) DEFAULT 'actif'
);

-- Create junction table for user-event participation
CREATE TABLE IF NOT EXISTS utilisateur_evenement (
    utilisateur_id INT,
    evenement_id INT,
    date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (utilisateur_id, evenement_id),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (evenement_id) REFERENCES evenement(id) ON DELETE CASCADE
);

-- Insert sample data for testing
INSERT INTO utilisateur (nom, prenom, adrEmail, numTel, adresse, role, mdp, pointGagne) VALUES
('Admin', 'User', 'admin@greencore.com', '1234567890', '123 Rue Admin', 'ADMIN', 'admin123', 100),
('John', 'Doe', 'john.doe@email.com', '0987654321', '456 Avenue User', 'USER', 'user123', 50),
('Jane', 'Smith', 'jane.smith@email.com', '1122334455', '789 Boulevard Member', 'MEMBER', 'member123', 75);

INSERT INTO evenement (nom, description, date_evenement, lieu, capacite_max, points_recompense, type_evenement, statut) VALUES
('Nettoyage de Plage', 'Participez au nettoyage de la plage locale', '2024-05-15', 'Plage Central', 100, 20, 'ENVIRONNEMENT', 'actif'),
('Plantation d\'Arbres', 'Aidez-nous à planter des arbres dans le parc', '2024-05-20', 'Parc Municipal', 50, 30, 'ENVIRONNEMENT', 'actif'),
('Sensibilisation Recyclage', 'Atelier de sensibilisation au recyclage', '2024-05-25', 'Centre Communautaire', 30, 15, 'EDUCATION', 'actif');

-- Insert some sample participations
INSERT INTO utilisateur_evenement (utilisateur_id, evenement_id) VALUES
(2, 1), -- John Doe participating in beach cleanup
(3, 2), -- Jane Smith participating in tree planting
(2, 3); -- John Doe participating in recycling workshop

-- Show the created tables
SHOW TABLES;
