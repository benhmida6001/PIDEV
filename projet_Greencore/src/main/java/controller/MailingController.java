package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;


public class MailingController {
    
    @FXML
    private TabPane tabPane;
    
    @FXML
    private ComboBox<String> comboEvenementInvit;
    
    @FXML
    private ComboBox<String> comboEvenementRappel;
    
    @FXML
    private ComboBox<String> comboUtilisateurBienvenue;
    
    @FXML
    private TextArea txtApercuInvit;
    
    @FXML
    private TextField txtSujetGroupe;
    
    @FXML
    private TextArea txtBodyGroupe;
    
    @FXML
    private Label labelResultatInvit;
    
    @FXML
    private Label labelResultatRappel;
    
    @FXML
    private Label labelResultatBienvenue;
    
    @FXML
    private Label labelResultatGroupe;
    
    @FXML
    public void initialize() {
        // Initialiser les combobox avec des données de test
        comboEvenementInvit.getItems().addAll(
            "Conférence GreenTech 2024",
            "Atelier Recyclage",
            "Forum Développement Durable",
            "Journée Portes Ouvertes"
        );
        
        comboEvenementRappel.getItems().addAll(
            "Conférence GreenTech 2024",
            "Atelier Recyclage", 
            "Forum Développement Durable",
            "Journée Portes Ouvertes"
        );
        
        comboUtilisateurBienvenue.getItems().addAll(
            "Jean Dupont - jean.dupont@email.com",
            "Marie Martin - marie.martin@email.com",
            "Pierre Bernard - pierre.bernard@email.com",
            "Sophie Petit - sophie.petit@email.com"
        );
        
        // Masquer les labels de résultat
        labelResultatInvit.setText("");
        labelResultatRappel.setText("");
        labelResultatBienvenue.setText("");
        labelResultatGroupe.setText("");
    }
    
    @FXML
    public void handleApercuInvitation() {
        String evenement = comboEvenementInvit.getValue();
        if (evenement == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un événement", AlertType.WARNING);
            return;
        }
        
        String apercu = genererApercuInvitation(evenement);
        txtApercuInvit.setText(apercu);
    }
    
    @FXML
    public void handleEnvoyerInvitation() {
        String evenement = comboEvenementInvit.getValue();
        if (evenement == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un événement", AlertType.WARNING);
            return;
        }
        
        try {
            // Simuler l'envoi d'emails
            String sujet = "Invitation: " + evenement;
            String corps = genererApercuInvitation(evenement);
            
            // Envoyer à tous les utilisateurs (simulation)
            int nbEmails = envoyerEmailGroupé(sujet, corps);
            
            labelResultatInvit.setText("✓ " + nbEmails + " invitations envoyées avec succès");
            labelResultatInvit.setStyle("-fx-text-fill: #4caf50;");
            
        } catch (Exception e) {
            labelResultatInvit.setText("✗ Erreur lors de l'envoi: " + e.getMessage());
            labelResultatInvit.setStyle("-fx-text-fill: #f44336;");
        }
    }
    
    @FXML
    public void handleEnvoyerRappel() {
        String evenement = comboEvenementRappel.getValue();
        if (evenement == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un événement", AlertType.WARNING);
            return;
        }
        
        try {
            String sujet = "Rappel: " + evenement;
            String corps = genererRappelEvenement(evenement);
            
            int nbEmails = envoyerEmailGroupé(sujet, corps);
            
            labelResultatRappel.setText("✓ " + nbEmails + " rappels envoyés avec succès");
            labelResultatRappel.setStyle("-fx-text-fill: #4caf50;");
            
        } catch (Exception e) {
            labelResultatRappel.setText("✗ Erreur lors de l'envoi: " + e.getMessage());
            labelResultatRappel.setStyle("-fx-text-fill: #f44336;");
        }
    }
    
    @FXML
    public void handleEnvoyerBienvenue() {
        String utilisateur = comboUtilisateurBienvenue.getValue();
        if (utilisateur == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un utilisateur", AlertType.WARNING);
            return;
        }
        
        try {
            String email = utilisateur.split(" - ")[1];
            String sujet = "Bienvenue chez GreenCore";
            String corps = genererEmailBienvenue(utilisateur.split(" - ")[0]);
            
            envoyerEmailIndividuel(email, sujet, corps);
            
            labelResultatBienvenue.setText("✓ Email de bienvenue envoyé à " + email);
            labelResultatBienvenue.setStyle("-fx-text-fill: #4caf50;");
            
        } catch (Exception e) {
            labelResultatBienvenue.setText("✗ Erreur lors de l'envoi: " + e.getMessage());
            labelResultatBienvenue.setStyle("-fx-text-fill: #f44336;");
        }
    }
    
    @FXML
    public void handleEnvoyerGroupe() {
        String sujet = txtSujetGroupe.getText().trim();
        String corps = txtBodyGroupe.getText().trim();
        
        if (sujet.isEmpty() || corps.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez remplir le sujet et le message", AlertType.WARNING);
            return;
        }
        
        try {
            int nbEmails = envoyerEmailGroupé(sujet, corps);
            
            labelResultatGroupe.setText("✓ " + nbEmails + " emails envoyés avec succès");
            labelResultatGroupe.setStyle("-fx-text-fill: #4caf50;");
            
            // Vider les champs
            txtSujetGroupe.clear();
            txtBodyGroupe.clear();
            
        } catch (Exception e) {
            labelResultatGroupe.setText("✗ Erreur lors de l'envoi: " + e.getMessage());
            labelResultatGroupe.setStyle("-fx-text-fill: #f44336;");
        }
    }
    
    private String genererApercuInvitation(String evenement) {
        return "Cher membre GreenCore,\n\n" +
               "Nous avons le plaisir de vous inviter à notre événement :\n\n" +
               evenement + "\n\n" +
               "Cet événement s'inscrit dans notre démarche de développement durable " +
               "et de sensibilisation écologique.\n\n" +
               "Date: À déterminer\n" +
               "Lieu: Siège GreenCore\n\n" +
               "Pour confirmer votre participation, merci de répondre à cet email.\n\n" +
               "Cordialement,\n" +
               "L'équipe GreenCore";
    }
    
    private String genererRappelEvenement(String evenement) {
        return "Cher membre GreenCore,\n\n" +
               "Rappel concernant l'événement :\n\n" +
               evenement + "\n\n" +
               "N'oubliez pas de vous préparer pour cet événement important " +
               "dans notre calendrier écologique.\n\n" +
               "Merci de votre engagement pour un avenir durable.\n\n" +
               "Cordialement,\n" +
               "L'équipe GreenCore";
    }
    
    private String genererEmailBienvenue(String nom) {
        return "Cher " + nom + ",\n\n" +
               "Bienvenue chez GreenCore !\n\n" +
               "Nous sommes ravis de vous accueillir dans notre communauté " +
               "engagée pour l'environnement et le développement durable.\n\n" +
               "En tant que membre, vous aurez accès à :\n" +
               "- Nos événements écologiques\n" +
               "- Des ateliers de sensibilisation\n" +
               "- Nos ressources pédagogiques\n" +
               "- Un réseau de passionnés\n\n" +
               "N'hésitez pas à nous contacter pour toute question.\n\n" +
               "Ensemble, faisons la différence !\n\n" +
               "Cordialement,\n" +
               "L'équipe GreenCore";
    }
    
    private int envoyerEmailGroupé(String sujet, String corps) {
        // Simulation d'envoi groupé
        // Dans un vrai projet, utiliser une vraie API SMTP
        
        // Simuler 10 utilisateurs
        int nbEmails = 10;
        
        try {
            // Simulation - ne pas envoyer réellement
            System.out.println("Simulation envoi groupé:");
            System.out.println("Sujet: " + sujet);
            System.out.println("Corps: " + corps);
            System.out.println("Nombre d'emails: " + nbEmails);
            
            return nbEmails;
            
        } catch (Exception e) {
            System.err.println("Erreur envoi groupé: " + e.getMessage());
            throw e;
        }
    }
    
    private void envoyerEmailIndividuel(String email, String sujet, String corps) {
        // Simulation d'envoi individuel
        try {
            System.out.println("Simulation envoi individuel:");
            System.out.println("À: " + email);
            System.out.println("Sujet: " + sujet);
            System.out.println("Corps: " + corps);
            
        } catch (Exception e) {
            System.err.println("Erreur envoi individuel: " + e.getMessage());
            throw e;
        }
    }
    
    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
