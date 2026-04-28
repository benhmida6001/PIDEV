package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import models.Message;
import models.Utilisateur;
import services.UtilisateurService;
import interfaces.IUtilisateurService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ChatController {
    
    @FXML
    private ListView<Utilisateur> listUsers;
    
    @FXML
    private Button btnActualiser;
    
    @FXML
    private Label labelConversation;
    
    @FXML
    private Label labelStatut;
    
    @FXML
    private Button btnResumeIA;
    
    @FXML
    private ScrollPane scrollMessages;
    
    @FXML
    private VBox vboxMessages;
    
    @FXML
    private TextArea txtMessage;
    
    @FXML
    private Button btnEnvoyer;
    
    @FXML
    private Button btnEffacer;
    
    @FXML
    private AnchorPane paneResume;
    
    @FXML
    private TextArea txtResume;
    
    @FXML
    private Label labelChargement;
    
    private IUtilisateurService utilisateurService;
    private Utilisateur utilisateurConnecte;
    private Utilisateur utilisateurSelectionne;
    private List<Message> messagesConversation;
    
    public ChatController() {
        this.utilisateurService = null;
        this.utilisateurConnecte = null;
        this.utilisateurSelectionne = null;
    }
    
    private IUtilisateurService getUtilisateurService() {
        if (utilisateurService == null) {
            utilisateurService = new UtilisateurService();
        }
        return utilisateurService;
    }
    
    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
    }
    
    @FXML
    public void initialize() {
        // Initialiser la liste des utilisateurs
        listUsers.setCellFactory(param -> new ListCell<Utilisateur>() {
            @Override
            protected void updateItem(Utilisateur utilisateur, boolean empty) {
                super.updateItem(utilisateur, empty);
                if (empty || utilisateur == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(utilisateur.getPrenom() + " " + utilisateur.getNom());
                    setStyle("-fx-text-fill: white; -fx-font-size: 14;");
                }
            }
        });
        
        // Gérer la sélection d'utilisateur
        listUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectionnerUtilisateur(newVal);
            }
        });
        
        // Activer/désactiver les boutons selon la sélection
        actualiserEtatBoutons();
        
        // Charger la liste des utilisateurs
        chargerUtilisateurs();
        
        // Afficher le message de bienvenue
        ajouterMessageSysteme("Bienvenue dans le chat Greencore ! Sélectionnez un utilisateur pour commencer.");
    }
    
    private void chargerUtilisateurs() {
        try {
            List<Utilisateur> utilisateurs = getUtilisateurService().afficherUtilisateurs();
            
            // Filtrer l'utilisateur connecté de la liste
            if (utilisateurConnecte != null) {
                utilisateurs.removeIf(u -> u.getId() == utilisateurConnecte.getId());
            }
            
            listUsers.getItems().clear();
            listUsers.getItems().addAll(utilisateurs);
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des utilisateurs: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible de charger la liste des utilisateurs", AlertType.ERROR);
        }
    }
    
    private void selectionnerUtilisateur(Utilisateur utilisateur) {
        this.utilisateurSelectionne = utilisateur;
        
        // Mettre à jour l'interface
        labelConversation.setText("Conversation avec " + utilisateur.getPrenom() + " " + utilisateur.getNom());
        labelStatut.setText("En ligne");
        
        // Charger les messages
        chargerMessages();
        
        // Activer les boutons
        actualiserEtatBoutons();
    }
    
    private void chargerMessages() {
        // Pour l'instant, nous allons simuler les messages
        // Dans une vraie application, vous auriez un service de messages
        
        vboxMessages.getChildren().clear();
        
        if (utilisateurSelectionne != null) {
            // Simulation de quelques messages pour la conversation sélectionnée
            ajouterMessage("Bonjour ! Comment allez-vous ?", false);
            ajouterMessage("Bonjour ! Je vais bien, merci. Et vous ?", true);
            ajouterMessage("Très bien, merci. Prêt pour la conférence Greencore ?", false);
            ajouterMessage("Oui, j'ai hâte d'y participer !", true);
        } else {
            // Message d'invitation
            ajouterMessageSysteme("Veuillez sélectionner un utilisateur pour commencer à chatter");
        }
        
        // Faire défiler vers le bas
        defilerVersBas();
    }
    
    private void ajouterMessage(String contenu, boolean estEnvoye) {
        System.out.println("Ajout du message: " + contenu + " (envoyé: " + estEnvoye + ")");
        System.out.println("vboxMessages est null: " + (vboxMessages == null));
        
        if (vboxMessages == null) {
            System.err.println("ERREUR: vboxMessages est null!");
            return;
        }
        
        // Créer un conteneur principal pour le message
        VBox messageContainer = new VBox(2);
        
        // Créer le message
        Text messageText = new Text(contenu);
        messageText.setFont(Font.font("System", 14));
        
        // Créer le label d'heure
        Label heureLabel = new Label(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        heureLabel.setFont(Font.font("System", 10));
        heureLabel.setTextFill(Color.GRAY);
        
        // Ajouter le texte et l'heure au conteneur
        messageContainer.getChildren().addAll(messageText, heureLabel);
        
        if (estEnvoye) {
            // Message envoyé (aligné à droite)
            messageContainer.setStyle("-fx-background-color: #4caf50; -fx-background-radius: 10; -fx-padding: 10;");
            messageText.setFill(Color.WHITE);
            VBox.setMargin(messageContainer, new javafx.geometry.Insets(5, 50, 5, 5));
        } else {
            // Message reçu (aligné à gauche)
            messageContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10;");
            messageText.setFill(Color.BLACK);
            VBox.setMargin(messageContainer, new javafx.geometry.Insets(5, 5, 5, 50));
        }
        
        // Ajouter le conteneur de message à la vbox principale
        vboxMessages.getChildren().add(messageContainer);
        
        // Forcer le rafraîchissement de l'interface
        vboxMessages.requestLayout();
        scrollMessages.requestLayout();
        
        // Faire défiler vers le bas
        defilerVersBas();
        
        System.out.println("Message ajouté avec succès. Total messages: " + vboxMessages.getChildren().size());
    }
    
    private void ajouterMessageSysteme(String contenu) {
        Label systemMessage = new Label(contenu);
        systemMessage.setFont(Font.font("System", 12));
        systemMessage.setTextFill(Color.GRAY);
        systemMessage.setStyle("-fx-font-style: italic; -fx-background-color: #f0f0f0; -fx-background-radius: 5; -fx-padding: 8;");
        VBox.setMargin(systemMessage, new javafx.geometry.Insets(10, 100, 10, 100));
        
        // Centrer le message
        javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(systemMessage);
        hbox.setAlignment(javafx.geometry.Pos.CENTER);
        
        vboxMessages.getChildren().add(hbox);
    }
    
    private void defilerVersBas() {
        // Forcer la mise à jour du layout avant de défiler
        javafx.application.Platform.runLater(() -> {
            scrollMessages.setVvalue(1.0);
            // Forcer un rafraîchissement supplémentaire
            scrollMessages.requestLayout();
            vboxMessages.requestLayout();
        });
    }
    
    private void actualiserEtatBoutons() {
        boolean utilisateurSelectionne = (this.utilisateurSelectionne != null);
        btnEnvoyer.setDisable(!utilisateurSelectionne);
        txtMessage.setDisable(!utilisateurSelectionne);
        btnEffacer.setDisable(!utilisateurSelectionne);
    }
    
    @FXML
    public void handleActualiser() {
        chargerUtilisateurs();
        afficherAlerte("Info", "Liste des utilisateurs actualisée", AlertType.INFORMATION);
    }
    
    @FXML
    public void handleEnvoyer() {
        System.out.println("=== BOUTON ENVOYER CLICÉ ===");
        String contenu = txtMessage.getText().trim();
        System.out.println("Contenu du message: '" + contenu + "'");
        
        if (contenu.isEmpty()) {
            afficherAlerte("Avertissement", "Veuillez saisir un message", AlertType.WARNING);
            return;
        }
        
        if (utilisateurSelectionne == null) {
            afficherAlerte("Avertissement", "Veuillez sélectionner un utilisateur", AlertType.WARNING);
            return;
        }
        
        try {
            // Créer le message
            Message message = new Message(
                utilisateurConnecte.getId(),
                utilisateurSelectionne.getId(),
                contenu
            );
            
            // Pour l'instant, nous allons juste l'afficher localement
            // Dans une vraie application, vous sauvegarderiez en base de données
            ajouterMessage(contenu, true);
            
            // Simuler une réponse automatique après un court délai
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(1000); // Attendre 1 seconde
                    String[] reponses = {
                        "Merci pour votre message !",
                        "Je comprends. Intéressant !",
                        "Pas de problème, je suis là.",
                        "D'accord, noté !",
                        "Absolument !"
                    };
                    
                    String reponse = reponses[(int)(Math.random() * reponses.length)];
                    ajouterMessage(reponse, false);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            // Vider le champ de saisie
            txtMessage.clear();
            
            // Faire défiler vers le bas
            defilerVersBas();
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du message: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible d'envoyer le message", AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleEffacer() {
        txtMessage.clear();
        txtMessage.requestFocus();
    }
    
    @FXML
    public void handleResumeIA() {
        // Afficher le panneau de résumé IA
        paneResume.setVisible(true);
        scrollMessages.setVisible(false);
        
        // Simuler un chargement
        labelChargement.setText("Génération du résumé en cours...");
        
        // Dans une vraie application, vous appelleriez une API IA
        // Pour l'instant, nous allons simuler un résumé
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulation de chargement
                
                javafx.application.Platform.runLater(() -> {
                    String resume = generateResumeSimulation();
                    txtResume.setText(resume);
                    labelChargement.setText("Résumé généré avec succès !");
                });
                
            } catch (InterruptedException e) {
                javafx.application.Platform.runLater(() -> {
                    labelChargement.setText("Erreur lors de la génération du résumé");
                });
            }
        }).start();
    }
    
    @FXML
    public void handleFermerResume() {
        paneResume.setVisible(false);
        scrollMessages.setVisible(true);
        labelChargement.setText("");
    }
    
    private String generateResumeSimulation() {
        return "RÉSUMÉ IA DES ÉVÉNEMENTS GREENCORE\n\n" +
               "🌱 **Événements à venir**\n" +
               "• Conférence sur le développement durable - 15 Mai 2024\n" +
               "• Atelier de recyclage créatif - 20 Mai 2024\n" +
               "• Forum sur les énergies renouvelables - 25 Mai 2024\n\n" +
               "📊 **Statistiques de participation**\n" +
               "• Total des participants ce mois: 245\n" +
               "• Taux de satisfaction: 4.8/5\n" +
               "• Événements les plus populaires: Ateliers pratiques\n\n" +
               "🎯 **Recommandations personnalisées**\n" +
               "Basé sur vos intérêts, nous vous recommandons:\n" +
               "• L'atelier permaculture du week-end prochain\n" +
               "• La conférence sur l'innovation verte\n" +
               "• Le réseau de mentorat environnemental\n\n" +
               "💡 **Tendances actuelles**\n" +
               "Les sujets les plus discutés dans la communauté:\n" +
               "• Économie circulaire et zéro déchet\n" +
               "• Agriculture urbaine et alimentation locale\n" +
               "• Technologies vertes et innovation durable\n\n" +
               "_Ce résumé est généré automatiquement par notre IA et est mis à jour toutes les heures._";
    }
    
    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
