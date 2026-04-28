package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import models.Utilisateur;
import services.UtilisateurService;
import interfaces.IUtilisateurService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RechercherUtilisateurController {
    
    private GestionUtilisateursController gestionUtilisateursController;
    
    @FXML
    private TextField tfIdRecherche;
    
    @FXML
    private Button btnRechercher;
    
    @FXML
    private Button btnEffacer;
    
    @FXML
    private Label lblResultat;
    
    @FXML
    private VBox vboxUserInfo;
    
    @FXML
    private Label lblNom;
    
    @FXML
    private Label lblPrenom;
    
    @FXML
    private Label lblEmail;
    
    @FXML
    private Label lblRole;
    
    @FXML
    private Label lblPoints;
    
    private IUtilisateurService utilisateurService;
    
    public RechercherUtilisateurController() {
        this.utilisateurService = null;
    }
    
    private IUtilisateurService getUtilisateurService() {
        if (utilisateurService == null) {
            utilisateurService = new UtilisateurService();
        }
        return utilisateurService;
    }
    
    @FXML
    public void initialize() {
        // Masquer la section des informations utilisateur au démarrage
        vboxUserInfo.setVisible(false);
        
        // Validation pour le champ ID (uniquement des chiffres)
        tfIdRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfIdRecherche.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
    
    @FXML
    public void handleRechercher() {
        String idText = tfIdRecherche.getText().trim();
        
        // Validation
        if (idText.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez saisir un ID d'utilisateur", AlertType.ERROR);
            return;
        }
        
        try {
            int id = Integer.parseInt(idText);
            
            // Rechercher l'utilisateur par ID
            List<Utilisateur> utilisateurs = getUtilisateurService().rechercherUtilisateurParId(id);
            
            if (utilisateurs.isEmpty()) {
                // Aucun utilisateur trouvé
                lblResultat.setText("Aucun utilisateur trouvé avec l'ID: " + id);
                lblResultat.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                vboxUserInfo.setVisible(false);
            } else {
                // Utilisateur trouvé (normalement il n'y en a qu'un seul)
                Utilisateur utilisateur = utilisateurs.get(0);
                
                lblResultat.setText("Utilisateur trouvé:");
                lblResultat.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                
                // Afficher les informations de l'utilisateur
                afficherInformationsUtilisateur(utilisateur);
                vboxUserInfo.setVisible(true);
            }
            
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Veuillez saisir un ID valide (nombre entier)", AlertType.ERROR);
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche: " + e.getMessage());
            afficherAlerte("Erreur", "Erreur de base de données: " + e.getMessage(), AlertType.ERROR);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
            afficherAlerte("Erreur", "Une erreur est survenue: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleEffacer() {
        // Vider le champ de recherche
        tfIdRecherche.clear();
        
        // Masquer les résultats
        lblResultat.setText("");
        vboxUserInfo.setVisible(false);
        
        // Donner le focus au champ de recherche
        tfIdRecherche.requestFocus();
    }
    
    private void afficherInformationsUtilisateur(Utilisateur utilisateur) {
        lblNom.setText("Nom: " + utilisateur.getNom());
        lblPrenom.setText("Prénom: " + utilisateur.getPrenom());
        lblEmail.setText("Email: " + utilisateur.getAdrEmail());
        lblRole.setText("Rôle: " + utilisateur.getRole());
        lblPoints.setText("Points: " + utilisateur.getPointGagne());
    }
    
    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void setGestionUtilisateursController(GestionUtilisateursController controller) {
        this.gestionUtilisateursController = controller;
    }
}
