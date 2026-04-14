package com.greencore.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Utilisateur;
import services.UtilisateurService;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AjouterUtilisateurController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField nomField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField prenomField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField telephoneField;

    private UtilisateurService utilisateurService;

    @FXML
    public void initialize() {
        utilisateurService = new UtilisateurService();
        
        // Initialiser la ComboBox avec les rôles
        roleComboBox.getItems().addAll("ADMIN", "USER");
        roleComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    void handleAjouterUtilisateur(ActionEvent event) {
        try {
            // Valider les champs
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || 
                emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires");
                return;
            }

            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(nomField.getText());
            utilisateur.setPrenom(prenomField.getText());
            utilisateur.setAdrEmail(emailField.getText());
            utilisateur.setMdp(passwordField.getText());
            utilisateur.setNumTel(telephoneField.getText());
            utilisateur.setRole(roleComboBox.getValue());

            // Ajouter l'utilisateur
            utilisateurService.ajouterUtilisateur(utilisateur);
            
            showAlert("Succès", "Utilisateur ajouté avec succès !");
            
            // Fermer la fenêtre
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();
            
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @FXML
    void handleAnnuler(ActionEvent event) {
        // Fermer la fenêtre sans sauvegarder
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
