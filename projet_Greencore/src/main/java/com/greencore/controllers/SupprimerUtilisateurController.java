package com.greencore.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Utilisateur;
import services.UtilisateurService;

import java.sql.SQLException;

public class SupprimerUtilisateurController {

    @FXML
    private Label messageLabel;

    private int utilisateurId;
    private UtilisateurService utilisateurService;

    @FXML
    public void initialize() {
        utilisateurService = new UtilisateurService();
    }

    /** À appeler après {@code FXMLLoader#load()} ; mémorise l’id pour {@link UtilisateurService#supprimerUtilisateur(int)}. */
    public void precharger(Utilisateur utilisateur) {
        this.utilisateurId = utilisateur.getId();
        messageLabel.setText(
                "Supprimer définitivement  utilisateur ?\n\n"
                        + "ID : " + utilisateur.getId() + "\n"
                        + "Nom : " + utilisateur.getNom() + " " + utilisateur.getPrenom() + "\n"
                        + "Email : " + utilisateur.getAdrEmail());
    }

    @FXML
    void handleConfirmer(ActionEvent event) {
        try {
            utilisateurService.supprimerUtilisateur(utilisateurId);
            showAlert("Succès", "Utilisateur supprimé.");
            fermer();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @FXML
    void handleAnnuler(ActionEvent event) {
        fermer();
    }

    private void fermer() {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
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
