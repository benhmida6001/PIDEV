package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import models.Utilisateur;
import services.UtilisateurService;
import interfaces.IUtilisateurService;

import java.sql.SQLException;

public class ModifierUtilisateurController {

    private GestionUtilisateursController gestionUtilisateursController;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfPrenom;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfTelephone;

    @FXML
    private TextField tfAdresse;

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private PasswordField pfMotDePasse;

    @FXML
    private TextField tfPoints;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnRetour;

    private IUtilisateurService utilisateurService;
    private Utilisateur utilisateurCourant;

    public ModifierUtilisateurController() {
        this.utilisateurService = null;
        this.utilisateurCourant = null;
    }

    private IUtilisateurService getUtilisateurService() {
        if (utilisateurService == null) {
            utilisateurService = new UtilisateurService();
        }
        return utilisateurService;
    }

    @FXML
    public void initialize() {
        // Initialiser la combobox des rôles
        cbRole.getItems().addAll("ADMIN", "USER", "MEMBER");
        cbRole.getSelectionModel().selectFirst();

        // Validation en temps réel pour l'email
        tfEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidEmail(newValue)) {
                tfEmail.setStyle("-fx-border-color: red;");
            } else {
                tfEmail.setStyle("-fx-border-color: green;");
            }
        });

        // Validation pour les points
        tfPoints.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfPoints.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void precharger(Utilisateur utilisateur) {
        this.utilisateurCourant = utilisateur;

        if (utilisateur != null) {
            tfNom.setText(utilisateur.getNom());
            tfPrenom.setText(utilisateur.getPrenom());
            tfEmail.setText(utilisateur.getAdrEmail());
            tfTelephone.setText(utilisateur.getNumTel());
            tfAdresse.setText(utilisateur.getAdresse());
            cbRole.setValue(utilisateur.getRole());
            pfMotDePasse.setText(utilisateur.getMdp());
            tfPoints.setText(String.valueOf(utilisateur.getPointGagne()));
        }
    }

    @FXML
    public void handleModifierUtilisateur() {
        if (utilisateurCourant == null) {
            afficherAlerte("Erreur", "Aucun utilisateur sélectionné pour la modification", AlertType.ERROR);
            return;
        }

        // Récupérer les valeurs des champs
        String nom = tfNom.getText().trim();
        String prenom = tfPrenom.getText().trim();
        String email = tfEmail.getText().trim();
        String telephone = tfTelephone.getText().trim();
        String adresse = tfAdresse.getText().trim();
        String role = cbRole.getValue();
        String motDePasse = pfMotDePasse.getText();
        String pointsText = tfPoints.getText().trim();

        // Validation des champs
        if (nom.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez saisir le nom de l'utilisateur", AlertType.ERROR);
            return;
        }

        if (prenom.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez saisir le prénom de l'utilisateur", AlertType.ERROR);
            return;
        }

        if (email.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez saisir l'email de l'utilisateur", AlertType.ERROR);
            return;
        }

        if (!isValidEmail(email)) {
            afficherAlerte("Erreur", "Veuillez saisir un email valide", AlertType.ERROR);
            return;
        }

        if (motDePasse.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez saisir un mot de passe", AlertType.ERROR);
            return;
        }

        if (motDePasse.length() < 4) {
            afficherAlerte("Erreur", "Le mot de passe doit contenir au moins 4 caractères", AlertType.ERROR);
            return;
        }

        // Validation des points
        int points = 0;
        try {
            points = Integer.parseInt(pointsText);
            if (points < 0) {
                afficherAlerte("Erreur", "Les points ne peuvent pas être négatifs", AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Veuillez saisir un nombre valide pour les points", AlertType.ERROR);
            return;
        }

        try {
            // Vérifier si l'email existe déjà (et n'appartient pas à l'utilisateur courant)
            if (((UtilisateurService) getUtilisateurService()).emailExiste(email) &&
                    !email.equals(utilisateurCourant.getAdrEmail())) {
                afficherAlerte("Erreur", "Cet email est déjà utilisé par un autre utilisateur", AlertType.ERROR);
                return;
            }

            // Mettre à jour l'utilisateur
            utilisateurCourant.setNom(nom);
            utilisateurCourant.setPrenom(prenom);
            utilisateurCourant.setAdrEmail(email);
            utilisateurCourant.setNumTel(telephone);
            utilisateurCourant.setAdresse(adresse);
            utilisateurCourant.setRole(role);
            utilisateurCourant.setMdp(motDePasse);
            utilisateurCourant.setPointGagne(points);

            // Mettre à jour dans la base de données
            getUtilisateurService().modifierUtilisateur(utilisateurCourant);

            afficherAlerte("Succès", "Utilisateur modifié avec succès!", AlertType.INFORMATION);

            // Rafraîchir la liste des utilisateurs
            if (gestionUtilisateursController != null) {
                gestionUtilisateursController.rafraichirListe();
            }

            // Retourner à la liste des utilisateurs
            handleRetour();

        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
            afficherAlerte("Erreur", "Erreur de base de données: " + e.getMessage(), AlertType.ERROR);
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            afficherAlerte("Erreur", "Une erreur est survenue: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void handleRetour() {
        // Fermer la fenêtre sans sauvegarder
        Stage stage = (Stage) tfNom.getScene().getWindow();
        stage.close();
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurCourant = utilisateur;
        precharger(utilisateur);
    }

    public void setGestionUtilisateursController(GestionUtilisateursController controller) {
        this.gestionUtilisateursController = controller;
    }
}
