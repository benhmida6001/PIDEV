-- Create evenement table with camelCase column names to match Java code
CREATE TABLE IF NOT EXISTS evenement (
    idEvent INT AUTO_INCREMENT PRIMARY KEY,
    nomEvent VARCHAR(255) NOT NULL,
    description TEXT,
    dateEvent DATETIME NOT NULL,
    lieuEvent VARCHAR(255),
    capaciteMax INT,
    pointsOfferts INT DEFAULT 0,
    typeEvenement VARCHAR(50),
    statut VARCHAR(50) DEFAULT 'actif'
);

-- Create participation table for user-event relationships
CREATE TABLE IF NOT EXISTS participation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idUtilisateur INT,
    idEvent INT,
    dateInscription DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idUtilisateur) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (idEvent) REFERENCES evenement(idEvent) ON DELETE CASCADE,
    UNIQUE KEY unique_participation (idUtilisateur, idEvent)
);

-- Insert sample data for testing
INSERT INTO evenement (nomEvent, description, dateEvent, lieuEvent, capaciteMax, pointsOfferts, typeEvenement, statut) VALUES
('Nettoyage de Plage', 'Participez au nettoyage de la plage locale', '2024-05-15 10:00:00', 'Plage Central', 100, 20, 'ENVIRONNEMENT', 'actif'),
('Plantation d''Arbres', 'Aidez-nous à planter des arbres dans le parc', '2024-05-20 14:00:00', 'Parc Municipal', 50, 30, 'ENVIRONNEMENT', 'actif'),
('Sensibilisation Recyclage', 'Atelier de sensibilisation au recyclage', '2024-05-25 16:00:00', 'Centre Communautaire', 30, 15, 'EDUCATION', 'actif'),
('Collecte de Déchets', 'Collecte de déchets recyclables dans le quartier', '2024-06-01 09:00:00', 'Quartier Eco', 75, 25, 'ENVIRONNEMENT', 'actif'),
('Atelier Jardinage', 'Apprenez les techniques de jardinage urbain', '2024-06-10 11:00:00', 'Jardin Communautaire', 25, 40, 'EDUCATION', 'actif');

-- Show the created table structure
DESCRIBE evenement;
DESCRIBE participation;

-- Show sample data
SELECT * FROM evenement;
SELECT * FROM participation;
