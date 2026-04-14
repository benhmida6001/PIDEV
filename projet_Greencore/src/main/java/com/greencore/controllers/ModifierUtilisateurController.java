package com.greencore.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Utilisateur;
import services.UtilisateurService;

import java.sql.SQLException;

public class ModifierUtilisateurController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField adresseField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField pointsField;

    private UtilisateurService utilisateurService;
    private String motDePasseActuel;

    @FXML
    public void initialize() {
        utilisateurService = new UtilisateurService();
        roleComboBox.getItems().addAll("ADMIN", "USER");
    }

    /**
     * Remplit le formulaire à partir de l'utilisateur sélectionné (appeler juste après {@code FXMLLoader#load()}).
     */
    public void precharger(Utilisateur u) {
        motDePasseActuel = u.getMdp() != null ? u.getMdp() : "";
        idField.setText(String.valueOf(u.getId()));
        nomField.setText(nvl(u.getNom()));
        prenomField.setText(nvl(u.getPrenom()));
        emailField.setText(nvl(u.getAdrEmail()));
        passwordField.clear();
        telephoneField.setText(nvl(u.getNumTel()));
        adresseField.setText(nvl(u.getAdresse()));

        String role = u.getRole();
        if (role != null && !roleComboBox.getItems().contains(role)) {
            roleComboBox.getItems().add(role);
        }
        roleComboBox.setValue(role != null ? role : roleComboBox.getItems().get(0));

        pointsField.setText(String.valueOf(u.getPointGagne()));
    }

    private static String nvl(String s) {
        return s != null ? s : "";
    }

    @FXML
    void handleEnregistrer(ActionEvent event) {
        try {
            if (nomField.getText().isBlank() || prenomField.getText().isBlank()
                    || emailField.getText().isBlank()) {
                showAlert("Erreur", "Veuillez remplir au moins le nom, le prénom et l'email.");
                return;
            }

            int id = Integer.parseInt(idField.getText().trim());
            int points;
            try {
                points = Integer.parseInt(pointsField.getText().trim().isEmpty() ? "0" : pointsField.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Les points doivent être un nombre entier.");
                return;
            }

            if (roleComboBox.getValue() == null) {
                showAlert("Erreur", "Veuillez choisir un rôle.");
                return;
            }

            String mdp = passwordField.getText();
            if (mdp == null || mdp.isBlank()) {
                mdp = motDePasseActuel;
            }

            Utilisateur utilisateur = new Utilisateur(
                    id,
                    nomField.getText().trim(),
                    prenomField.getText().trim(),
                    emailField.getText().trim(),
                    telephoneField.getText().trim(),
                    adresseField.getText().trim(),
                    roleComboBox.getValue(),
                    mdp,
                    points
            );

            utilisateurService.modifierUtilisateur(utilisateur);
            showAlert("Succès", "Utilisateur modifié avec succès.");
            fermerFenetre();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la modification : " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Identifiant utilisateur invalide.");
        }
    }

    @FXML
    void handleAnnuler(ActionEvent event) {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
