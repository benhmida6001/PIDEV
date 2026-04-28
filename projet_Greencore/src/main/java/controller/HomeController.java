package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class HomeController {
    
    @FXML
    private Label labelDate;
    
    @FXML
    private Label kpiUsers;
    
    @FXML
    private Label kpiUsersSub;
    
    @FXML
    private Label kpiEvents;
    
    @FXML
    private Label kpiEventsSub;
    
    @FXML
    private Label kpiPoints;
    
    @FXML
    private Label kpiPointsSub;
    
    @FXML
    private Label event1Nom;
    
    @FXML
    private Label event1Detail;
    
    @FXML
    private Label event2Nom;
    
    @FXML
    private Label event2Detail;
    
    @FXML
    private Label event3Nom;
    
    @FXML
    private Label event3Detail;
    
    private Random random = new Random();
    
    @FXML
    public void initialize() {
        // Initialiser la date et l'heure actuelle
        updateDateTime();
        
        // Charger les KPIs
        loadKPIs();
        
        // Charger les prochains événements
        loadUpcomingEvents();
    }
    
    private void updateDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        String currentDateTime = LocalDateTime.now().format(dtf);
        labelDate.setText("Dernière mise à jour: " + currentDateTime);
    }
    
    private void loadKPIs() {
        // Simuler le chargement des données depuis une base de données
        try {
            // Simuler un délai de chargement
            Thread.sleep(500);
            
            // Générer des KPIs réalistes
            int totalUsers = 1247 + random.nextInt(200);
            int newUsersThisMonth = 89 + random.nextInt(30);
            int totalEvents = 85 + random.nextInt(20);
            int activeEvents = 12 + random.nextInt(8);
            int totalPoints = 45678 + random.nextInt(5000);
            int pointsThisMonth = 2340 + random.nextInt(1000);
            
            // Mettre à jour les KPIs
            kpiUsers.setText(String.valueOf(totalUsers));
            kpiUsersSub.setText("+" + newUsersThisMonth + " ce mois");
            
            kpiEvents.setText(String.valueOf(totalEvents));
            kpiEventsSub.setText(activeEvents + " actifs");
            
            kpiPoints.setText(String.valueOf(totalPoints));
            kpiPointsSub.setText("+" + pointsThisMonth + " ce mois");
            
        } catch (InterruptedException e) {
            // En cas d'erreur, afficher des valeurs par défaut
            kpiUsers.setText("--");
            kpiUsersSub.setText("Erreur de chargement");
            
            kpiEvents.setText("--");
            kpiEventsSub.setText("Erreur de chargement");
            
            kpiPoints.setText("--");
            kpiPointsSub.setText("Erreur de chargement");
        }
    }
    
    private void loadUpcomingEvents() {
        // Simuler le chargement des prochains événements
        String[] eventNames = {
            "Conférence GreenTech 2024",
            "Atelier Recyclage Urbain",
            "Forum Développement Durable",
            "Journée Portes Ouvertes",
            "Formation Écologie Numérique",
            "Campagne de Plantation",
            "Séminaire Énergies Renouvelables",
            "Hackathon Écologique"
        };
        
        String[] eventDetails = {
            "15 juin 2024 • 150 participants",
            "22 juin 2024 • 45 participants",
            "28 juin 2024 • 200 participants",
            "5 juillet 2024 • 80 participants",
            "12 juillet 2024 • 60 participants",
            "19 juillet 2024 • 120 participants",
            "26 juillet 2024 • 90 participants",
            "2 août 2024 • 35 participants"
        };
        
        // Sélectionner 3 événements aléatoires
        int[] selectedIndices = getRandomIndices(eventNames.length, 3);
        
        event1Nom.setText(eventNames[selectedIndices[0]]);
        event1Detail.setText(eventDetails[selectedIndices[0]]);
        
        event2Nom.setText(eventNames[selectedIndices[1]]);
        event2Detail.setText(eventDetails[selectedIndices[1]]);
        
        event3Nom.setText(eventNames[selectedIndices[2]]);
        event3Detail.setText(eventDetails[selectedIndices[2]]);
    }
    
    private int[] getRandomIndices(int max, int count) {
        int[] indices = new int[count];
        for (int i = 0; i < count; i++) {
            indices[i] = random.nextInt(max);
        }
        return indices;
    }
    
    @FXML
    public void handleGererUtilisateurs() {
        try {
            // Charger l'interface de gestion des utilisateurs
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestion_utilisateurs.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Gestion des Utilisateurs - GreenCore");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
            
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible d'ouvrir l'interface de gestion des utilisateurs", AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleGererEvenements() {
        try {
            // Charger l'interface de gestion des événements
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestion_evenements.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Gestion des Événements - GreenCore");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
            
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible d'ouvrir l'interface de gestion des événements", AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleChat() {
        try {
            // Charger l'interface de chat
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chat_interface.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Chat IA - GreenCore");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
            
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible d'ouvrir l'interface de chat", AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleAvance() {
        try {
            // Charger l'interface des fonctionnalités avancées
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/statistics_dashboard.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Fonctionnalités Avancées - GreenCore");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
            
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible d'ouvrir l'interface des fonctionnalités avancées", AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleRefresh() {
        // Rafraîchir les données
        updateDateTime();
        loadKPIs();
        loadUpcomingEvents();
    }
    
    @FXML
    public void handleSettings() {
        afficherAlerte("Paramètres", "Fonctionnalité de paramètres en cours de développement", AlertType.INFORMATION);
    }
    
    @FXML
    public void handleAbout() {
        String aboutText = "GreenCore v2.0\n\n" +
                          "Plateforme de gestion d'événements écologiques\n\n" +
                          "Fonctionnalités:\n" +
                          "• Gestion des utilisateurs\n" +
                          "• Gestion des événements\n" +
                          "• Chat IA intégré\n" +
                          "• Système de points\n" +
                          "• Statistiques avancées\n\n" +
                          "Développé avec JavaFX 21\n" +
                          "© 2024 GreenCore Team";
        
        afficherAlerte("À propos de GreenCore", aboutText, AlertType.INFORMATION);
    }
    
    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
