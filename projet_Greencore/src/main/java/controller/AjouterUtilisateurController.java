package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Utilisateur;
import services.UtilisateurService;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AjouterUtilisateurController {

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
    private PasswordField pfMotDePasse;

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private TextField tfPoints;

    @FXML
    private Label lblMessage;

    private UtilisateurService utilisateurService;

    @FXML
    public void initialize() {
        utilisateurService = new UtilisateurService();

        // Initialiser la ComboBox avec les rôles
        cbRole.getItems().addAll("ADMIN", "USER");
        cbRole.getSelectionModel().selectFirst();
    }

    @FXML
    void handleAjouterUtilisateur(ActionEvent event) {
        try {
            // Valider les champs
            if (tfNom.getText().isEmpty() || tfPrenom.getText().isEmpty() ||
                    tfEmail.getText().isEmpty() || pfMotDePasse.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires");
                return;
            }

            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(tfNom.getText());
            utilisateur.setPrenom(tfPrenom.getText());
            utilisateur.setAdrEmail(tfEmail.getText());
            utilisateur.setMdp(pfMotDePasse.getText());
            utilisateur.setNumTel(tfTelephone.getText());
            utilisateur.setAdresse(tfAdresse.getText());
            utilisateur.setRole(cbRole.getValue());

            // Ajouter l'utilisateur
            utilisateurService.ajouterUtilisateur(utilisateur);

            showAlert("Succès", "Utilisateur ajouté avec succès !");

            // Rafraîchir la liste des utilisateurs
            if (gestionUtilisateursController != null) {
                gestionUtilisateursController.rafraichirListe();
            }

            // Fermer la fenêtre
            Stage stage = (Stage) tfNom.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @FXML
    void handleAnnuler(ActionEvent event) {
        // Fermer la fenêtre sans sauvegarder
        Stage stage = (Stage) tfNom.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setGestionUtilisateursController(GestionUtilisateursController controller) {
        this.gestionUtilisateursController = controller;
    }
}
