package controller;

import models.Evenement;
import models.Utilisateur;
import services.EvenementService;
import services.UtilisateurService;
import interfaces.IEvenementService;
import interfaces.IUtilisateurService;

import java.sql.SQLException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashboardController {
    
    // FXML components
    @FXML
    private Button btnHome;
    @FXML
    private Button btnGererUtilisateur;
    @FXML
    private Button btnGererEvenement;
    @FXML
    private Button btnParametres;
    @FXML
    private Button btnChat;
    @FXML
    private Button btnMeteo;
    @FXML
    private Button btnMailing;
    @FXML
    private Button btnPaiement;
    @FXML
    private Button btnAvance;
    @FXML
    private Button btnDeconnexion;
    @FXML
    private Label labelUtilisateur;
    @FXML
    private Label labelRole;
    @FXML
    private Label labelSousTitre;
    @FXML
    private Label labelDate;
    @FXML
    private AnchorPane contentPane;
    
    private IEvenementService evenementService;
    private IUtilisateurService utilisateurService;
    private Utilisateur utilisateurCourant;
    
    public DashboardController() {
        // Initialisation différée pour éviter la connexion BDD au démarrage
        this.evenementService = null;
        this.utilisateurService = null;
        this.utilisateurCourant = null;
    }
    
    public void setUtilisateurCourant(Utilisateur utilisateur) {
        this.utilisateurCourant = utilisateur;
        
        // Mettre à jour les labels du dashboard
        if (utilisateur != null) {
            labelUtilisateur.setText(utilisateur.getPrenom() + " " + utilisateur.getNom());
            labelRole.setText(utilisateur.getRole());
        }
    }
    
    private IEvenementService getEvenementService() {
        if (evenementService == null) {
            evenementService = new EvenementService();
        }
        return evenementService;
    }
    
    private IUtilisateurService getUtilisateurService() {
        if (utilisateurService == null) {
            utilisateurService = new UtilisateurService();
        }
        return utilisateurService;
    }
    
    // Statistiques générales
    public int getNombreTotalEvenements() {
        try {
            return getEvenementService().afficherEvenements().size();
        } catch (SQLException ex) {
            System.err.println("Erreur lors du comptage des événements : " + ex.getMessage());
            return 0;
        }
    }
    
    public int getNombreTotalUtilisateurs() {
        try {
            return getUtilisateurService().afficherUtilisateurs().size();
        } catch (SQLException ex) {
            System.err.println("Erreur lors du comptage des utilisateurs : " + ex.getMessage());
            return 0;
        }
    }
    
    public int getNombreEvenementsAVenir() {
        try {
            return getEvenementService().getEvenementsAVenir().size();
        } catch (SQLException ex) {
            System.err.println("Erreur lors du comptage des événements à venir : " + ex.getMessage());
            return 0;
        }
    }
    
    // Gestion des événements
    public List<Evenement> getAllEvenements() {
        try {
            return getEvenementService().afficherEvenements();
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des événements : " + ex.getMessage());
            return List.of();
        }
    }
    
    public List<Evenement> getEvenementsAVenir() {
        try {
            return getEvenementService().getEvenementsAVenir();
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des événements à venir : " + ex.getMessage());
            return List.of();
        }
    }
    
    public boolean ajouterEvenement(Evenement evenement) {
        try {
            getEvenementService().ajouterEvenement(evenement);
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout de l'événement : " + ex.getMessage());
            return false;
        }
    }
    
    public boolean modifierEvenement(Evenement evenement) {
        try {
            getEvenementService().modifierEvenement(evenement);
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la modification de l'événement : " + ex.getMessage());
            return false;
        }
    }
    
    public boolean supprimerEvenement(int idEvent) {
        try {
            getEvenementService().supprimerEvenement(idEvent);
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression de l'événement : " + ex.getMessage());
            return false;
        }
    }
    
    public Evenement rechercherEvenementParId(int idEvent) {
        try {
            return getEvenementService().rechercherEvenementParId(idEvent);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche de l'événement : " + ex.getMessage());
            return null;
        }
    }
    
    public List<Evenement> rechercherEvenementsParCritere(String critere) {
        try {
            return getEvenementService().rechercherEvenementsParCritere(critere);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche des événements : " + ex.getMessage());
            return List.of();
        }
    }
    
    // Gestion des utilisateurs
    public List<Utilisateur> getAllUtilisateurs() {
        try {
            return getUtilisateurService().afficherUtilisateurs();
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + ex.getMessage());
            return List.of();
        }
    }
    
    public boolean ajouterUtilisateur(Utilisateur utilisateur) {
        try {
            getUtilisateurService().ajouterUtilisateur(utilisateur);
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + ex.getMessage());
            return false;
        }
    }
    
    public boolean modifierUtilisateur(Utilisateur utilisateur) {
        try {
            getUtilisateurService().modifierUtilisateur(utilisateur);
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la modification de l'utilisateur : " + ex.getMessage());
            return false;
        }
    }
    
    public boolean supprimerUtilisateur(int idUtilisateur) {
        try {
            getUtilisateurService().supprimerUtilisateur(idUtilisateur);
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + ex.getMessage());
            return false;
        }
    }
    
    public Utilisateur rechercherUtilisateurParId(int idUtilisateur) {
        try {
            List<Utilisateur> utilisateurs = getUtilisateurService().rechercherUtilisateurParId(idUtilisateur);
            return utilisateurs.isEmpty() ? null : utilisateurs.get(0);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche de l'utilisateur : " + ex.getMessage());
            return null;
        }
    }
    
    public List<Utilisateur> rechercherUtilisateursParCritere(String critere) {
        try {
            return ((UtilisateurService) getUtilisateurService()).rechercherUtilisateursParCritere(critere);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche des utilisateurs : " + ex.getMessage());
            return List.of();
        }
    }
    
    // Authentification
    public Utilisateur authentifier(String email, String motDePasse) {
        try {
            List<Utilisateur> utilisateurs = getUtilisateurService().authentifier(email, motDePasse);
            return utilisateurs.isEmpty() ? null : utilisateurs.get(0);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'authentification : " + ex.getMessage());
            return null;
        }
    }
    
    // Gestion des participations
    public boolean inscrireUtilisateurEvenement(int idUtilisateur, int idEvent) {
        try {
            getEvenementService().inscrireUtilisateurEvenement(idUtilisateur, idEvent);
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'inscription à l'événement : " + ex.getMessage());
            return false;
        }
    }
    
    public boolean desinscrireUtilisateurEvenement(int idUtilisateur, int idEvent) {
        try {
            getEvenementService().desinscrireUtilisateurEvenement(idUtilisateur, idEvent);
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la désinscription de l'événement : " + ex.getMessage());
            return false;
        }
    }
    
    public List<Utilisateur> getParticipantsEvenement(int idEvent) {
        try {
            return getEvenementService().getParticipantsEvenement(idEvent);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des participants : " + ex.getMessage());
            return List.of();
        }
    }
    
    public List<Evenement> getEvenementsUtilisateur(int idUtilisateur) {
        try {
            return getEvenementService().getEvenementsUtilisateur(idUtilisateur);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des événements de l'utilisateur : " + ex.getMessage());
            return List.of();
        }
    }
    
    public boolean estInscrit(int idUtilisateur, int idEvent) {
        try {
            return getEvenementService().estInscrit(idUtilisateur, idEvent);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la vérification de l'inscription : " + ex.getMessage());
            return false;
        }
    }
    
    public int getNombreParticipants(int idEvent) {
        try {
            return getEvenementService().compterParticipants(idEvent);
        } catch (SQLException ex) {
            System.err.println("Erreur lors du comptage des participants : " + ex.getMessage());
            return 0;
        }
    }
    
    // Gestion des points
    public boolean mettreAJourPoints(int idUtilisateur, int points) {
        try {
            getUtilisateurService().mettreAJourPoints(idUtilisateur, points);
            return true;
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la mise à jour des points : " + ex.getMessage());
            return false;
        }
    }
    
    // Statistiques avancées
    public int getNombreTotalParticipations() {
        try {
            int total = 0;
            List<Evenement> evenements = getEvenementService().afficherEvenements();
            for (Evenement e : evenements) {
                total += getEvenementService().compterParticipants(e.getIdEvent());
            }
            return total;
        } catch (SQLException ex) {
            System.err.println("Erreur lors du comptage total des participations : " + ex.getMessage());
            return 0;
        }
    }
    
    public double getMoyenneParticipantsParEvenement() {
        int nombreEvenements = getNombreTotalEvenements();
        if (nombreEvenements == 0) return 0;
        
        int totalParticipations = getNombreTotalParticipations();
        return (double) totalParticipations / nombreEvenements;
    }
    
    public List<Utilisateur> getUtilisateursParRole(String role) {
        try {
            return ((UtilisateurService) getUtilisateurService()).getUtilisateursParRole(role);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des utilisateurs par rôle : " + ex.getMessage());
            return List.of();
        }
    }
    
    public boolean emailExiste(String email) {
        try {
            return ((UtilisateurService) getUtilisateurService()).emailExiste(email);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la vérification de l'email : " + ex.getMessage());
            return false;
        }
    }
    
    // FXML Event Handlers
    @FXML
    public void initialize() {
        // Initialiser l'interface
        System.out.println("Dashboard initialisé");
        
        // Charger la page d'accueil par défaut
        handleHomeAction();
    }
    
    @FXML
    public void handleHomeAction() { 
        try {
            // Charger la page d'accueil dans le content pane
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Parent homeRoot = loader.load();
            
            // Mettre à jour le sous-titre
            labelSousTitre.setText("Accueil");
            
            // Effacer le contenu actuel et ajouter la vue d'accueil
            contentPane.getChildren().clear();
            contentPane.getChildren().add(homeRoot);
            
            // Ajuster la taille pour remplir le content pane
            AnchorPane.setTopAnchor(homeRoot, 0.0);
            AnchorPane.setBottomAnchor(homeRoot, 0.0);
            AnchorPane.setLeftAnchor(homeRoot, 0.0);
            AnchorPane.setRightAnchor(homeRoot, 0.0);
            
            // Centrer le contenu de la page d'accueil
            AnchorPane.setTopAnchor(homeRoot, 50.0); // Un peu d'espace en haut
            
            System.out.println("Page d'accueil chargée dans le dashboard");
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page d'accueil: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible de charger la page d'accueil", AlertType.ERROR);
        }
    }

    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleGererUtilisateurAction() {
        try {
            // Charger la gestion des utilisateurs dans le content pane
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestion_utilisateurs.fxml"));
            Parent gestionRoot = loader.load();
            
            // Mettre à jour le sous-titre
            labelSousTitre.setText("Gestion des Utilisateurs");
            
            // Effacer le contenu actuel et ajouter la vue de gestion
            contentPane.getChildren().clear();
            contentPane.getChildren().add(gestionRoot);
            
            // Ajuster la taille pour remplir le content pane
            AnchorPane.setTopAnchor(gestionRoot, 0.0);
            AnchorPane.setBottomAnchor(gestionRoot, 0.0);
            AnchorPane.setLeftAnchor(gestionRoot, 0.0);
            AnchorPane.setRightAnchor(gestionRoot, 0.0);
            
            System.out.println("Gestion des utilisateurs chargée dans le dashboard");
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la gestion des utilisateurs: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible de charger la gestion des utilisateurs", AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleGererEvenementAction() { 
        try {
            System.out.println("Gestion événements button clicked");
            labelSousTitre.setText("Gestion des Événements");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/gestion_evenements.fxml"));
            Parent gestionRoot = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(gestionRoot);

            AnchorPane.setTopAnchor(gestionRoot, 0.0);
            AnchorPane.setBottomAnchor(gestionRoot, 0.0);
            AnchorPane.setLeftAnchor(gestionRoot, 0.0);
            AnchorPane.setRightAnchor(gestionRoot, 0.0);

            System.out.println("Gestion des événements chargée dans le dashboard");
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la gestion des événements: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible de charger la gestion des événements", Alert.AlertType.ERROR);
        } catch (Exception e) {
            System.err.println("=== ERREUR COMPLÈTE ===");
            System.err.println("Type    : " + e.getClass().getName());
            System.err.println("Message : " + e.getMessage());
            // Afficher toute la chaîne des causes
            Throwable cause = e.getCause();
            int i = 1;
            while (cause != null) {
                System.err.println("Cause " + i + " : "
                        + cause.getClass().getName()
                        + " — " + cause.getMessage());
                cause = cause.getCause();
                i++;
            }
            e.printStackTrace();
            
            afficherAlerte("Erreur", "Impossible de charger la gestion des événements", Alert.AlertType.ERROR);
        }
    }

    
    @FXML
    public void handleChatAction() {
        try {
            System.out.println("Chat IA button clicked");
            
            // Vérifier si un utilisateur est connecté
            if (utilisateurCourant == null) {
                // Pour tester : créer un utilisateur fictif
                utilisateurCourant = new Utilisateur(1, "Test", "User", "test@greencore.com", "", "", "USER", "password", 100);
                System.out.println("Utilisateur de test créé pour le chat");
            }
            
            // Charger l'interface de chat directement dans le contentPane
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chat_interface.fxml"));
            Parent chatRoot = loader.load();
            
            // Obtenir le contrôleur de chat et lui passer l'utilisateur connecté
            ChatController chatController = loader.getController();
            chatController.setUtilisateurConnecte(utilisateurCourant);
            
            // Vider le contentPane et ajouter le chat
            contentPane.getChildren().clear();
            contentPane.getChildren().add(chatRoot);
            
            // Ajuster le chat pour remplir le contentPane
            AnchorPane.setTopAnchor(chatRoot, 0.0);
            AnchorPane.setBottomAnchor(chatRoot, 0.0);
            AnchorPane.setLeftAnchor(chatRoot, 0.0);
            AnchorPane.setRightAnchor(chatRoot, 0.0);
            
            // Mettre à jour le sous-titre du dashboard
            labelSousTitre.setText("Chat Intelligence Artificielle");
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface de chat: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible d'ouvrir l'interface de chat", Alert.AlertType.ERROR);
        }
    }
    @FXML
    public void handleMeteoAction() {
        try {
            System.out.println("Météo button clicked");
            
            // Charger l'interface météo directement dans le contentPane
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/weather_simple.fxml"));
            Parent weatherRoot = loader.load();
            
            // Vider le contentPane et ajouter la météo
            contentPane.getChildren().clear();
            contentPane.getChildren().add(weatherRoot);
            
            // Ajuster l'interface météo pour remplir le contentPane
            AnchorPane.setTopAnchor(weatherRoot, 0.0);
            AnchorPane.setBottomAnchor(weatherRoot, 0.0);
            AnchorPane.setLeftAnchor(weatherRoot, 0.0);
            AnchorPane.setRightAnchor(weatherRoot, 0.0);
            
            // Mettre à jour le sous-titre du dashboard
            labelSousTitre.setText("Service Météo");
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface météo: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible d'ouvrir l'interface météo", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleMailingAction() {
        try {
            System.out.println("Mailing button clicked");
            labelSousTitre.setText("Service Mailing");
            
            // Charger l'interface mailing dans le contentPane
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mailing_interface.fxml"));
            Parent mailingRoot = loader.load();
            
            // Vider le contentPane et ajouter l'interface mailing
            contentPane.getChildren().clear();
            contentPane.getChildren().add(mailingRoot);
            
            // Ajuster la taille du contenu pour remplir le contentPane
            AnchorPane.setTopAnchor(mailingRoot, 0.0);
            AnchorPane.setBottomAnchor(mailingRoot, 0.0);
            AnchorPane.setLeftAnchor(mailingRoot, 0.0);
            AnchorPane.setRightAnchor(mailingRoot, 0.0);
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface mailing: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible d'ouvrir l'interface mailing", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void handlePaiementAction() {
        try {
            System.out.println("Paiement button clicked");
            labelSousTitre.setText("Service Paiement");
            
            // Charger l'interface paiement dans le contentPane
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/payment_interface.fxml"));
            Parent paymentRoot = loader.load();
            
            // Vider le contentPane et ajouter l'interface paiement
            contentPane.getChildren().clear();
            contentPane.getChildren().add(paymentRoot);
            
            // Ajuster la taille du contenu pour remplir le contentPane
            AnchorPane.setTopAnchor(paymentRoot, 0.0);
            AnchorPane.setBottomAnchor(paymentRoot, 0.0);
            AnchorPane.setLeftAnchor(paymentRoot, 0.0);
            AnchorPane.setRightAnchor(paymentRoot, 0.0);
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface paiement: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible d'ouvrir l'interface paiement", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleAvanceAction() {
        try {
            System.out.println("QR · Stats · ICS button clicked");
            labelSousTitre.setText("Générateur QR Code");
            
            // Charger l'interface QR Code dans le contentPane
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/qr_code_generator.fxml"));
            Parent qrRoot = loader.load();
            
            // Vider le contentPane et ajouter l'interface QR Code
            contentPane.getChildren().clear();
            contentPane.getChildren().add(qrRoot);
            
            // Ajuster la taille du contenu pour remplir le contentPane
            AnchorPane.setTopAnchor(qrRoot, 0.0);
            AnchorPane.setBottomAnchor(qrRoot, 0.0);
            AnchorPane.setLeftAnchor(qrRoot, 0.0);
            AnchorPane.setRightAnchor(qrRoot, 0.0);
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface QR Code: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible d'ouvrir l'interface QR Code", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleApplicationAction() {
        try {
            System.out.println("Application button clicked");
            labelSousTitre.setText("Tableau de Bord Statistiques");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/statistics_dashboard.fxml"));
            Parent statsRoot = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(statsRoot);

            AnchorPane.setTopAnchor(statsRoot, 0.0);
            AnchorPane.setBottomAnchor(statsRoot, 0.0);
            AnchorPane.setLeftAnchor(statsRoot, 0.0);
            AnchorPane.setRightAnchor(statsRoot, 0.0);

        } catch (Exception e) {
            System.err.println("=== ERREUR COMPLÈTE ===");
            System.err.println("Type    : " + e.getClass().getName());
            System.err.println("Message : " + e.getMessage());
            // Afficher toute la chaîne des causes
            Throwable cause = e.getCause();
            int i = 1;
            while (cause != null) {
                System.err.println("Cause " + i + " : "
                        + cause.getClass().getName()
                        + " — " + cause.getMessage());
                cause = cause.getCause();
                i++;
            }
            e.printStackTrace();
            
            // Afficher une alerte à l'utilisateur
            afficherAlerte("Erreur", "Impossible de charger le tableau de bord statistiques", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleDeconnexionAction() {
        System.out.println("Déconnexion button clicked");
        // TODO: Retourner à l'écran de login
        System.exit(0);
    }
    
    /**
     * Méthode pour réinitialiser le contenu du dashboard
     */
    public void resetContent() {
        contentPane.getChildren().clear();
        labelSousTitre.setText("Bienvenue sur Greencore");
    }
}
