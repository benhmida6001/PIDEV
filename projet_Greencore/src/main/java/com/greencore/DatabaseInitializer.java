package com.greencore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String URL = "jdbc:mysql://localhost:3306/pidev?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public static void main(String[] args) {
        System.out.println("=== INITIALISATION DE LA BASE DE DONNÉES ===");
        
        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL chargé");
            
            // Connexion à la base de données
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion à la base de données établie");
            
            Statement stmt = conn.createStatement();
            
            // Créer la table utilisateur
            System.out.println("Création de la table utilisateur...");
            String createTableSQL = "CREATE TABLE IF NOT EXISTS utilisateur (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nom VARCHAR(50) NOT NULL," +
                "prenom VARCHAR(50) NOT NULL," +
                "adrEmail VARCHAR(100) UNIQUE NOT NULL," +
                "numTel VARCHAR(20)," +
                "adresse VARCHAR(200)," +
                "role VARCHAR(50) DEFAULT 'utilisateur'," +
                "mdp VARCHAR(255) NOT NULL," +
                "pointGagne INT DEFAULT 0" +
                ")";
            
            stmt.execute(createTableSQL);
            System.out.println("Table utilisateur créée avec succès");
            
            // Insérer les données de test
            System.out.println("Insertion des données de test...");
            String insertSQL = "INSERT IGNORE INTO utilisateur (nom, prenom, adrEmail, numTel, adresse, role, mdp, pointGagne) VALUES " +
                "('Dupont', 'Jean', 'jean.dupont@email.com', '0612345678', '123 rue de Paris, 75001 Paris', 'admin', 'admin123', 100)," +
                "('Martin', 'Sophie', 'sophie.martin@email.com', '0623456789', '456 avenue de Lyon, 69000 Lyon', 'utilisateur', 'sophie123', 50)," +
                "('Bernard', 'Pierre', 'pierre.bernard@email.com', '0634567890', '789 boulevard de Marseille, 13000 Marseille', 'utilisateur', 'pierre123', 75)";
            
            stmt.execute(insertSQL);
            System.out.println("Données de test insérées");
            
            // Vérifier
            var rs = stmt.executeQuery("SELECT COUNT(*) as total FROM utilisateur");
            if (rs.next()) {
                int count = rs.getInt("total");
                System.out.println("Nombre d'utilisateurs dans la base: " + count);
            }
            rs.close();
            
            stmt.close();
            conn.close();
            
            System.out.println("\n=== BASE DE DONNÉES PRÊTE! ===");
            System.out.println("Vous pouvez maintenant lancer l'application");
            
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
