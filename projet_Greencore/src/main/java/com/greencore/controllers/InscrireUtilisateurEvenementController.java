package com.greencore.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.EvenementService;

public class InscrireUtilisateurEvenementController {
    
    @FXML
    private TextField idUtilisateurField;
    
    @FXML
    private TextField idEventField;
    
    @FXML
    private Button inscrireButton;
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private Label titreLabel;
    
    private EvenementService evenementService;
    
    public InscrireUtilisateurEvenementController() {
        this.evenementService = new EvenementService();
    }
    
    public void setEvenementId(int idEvent) {
        idEventField.setText(String.valueOf(idEvent));
        titreLabel.setText("Inscription à l'Événement ID: " + idEvent);
    }
    
    @FXML
    private void handleInscrire(ActionEvent event) {
        String idUtilisateurStr = idUtilisateurField.getText().trim();
        String idEventStr = idEventField.getText().trim();
        
        // Validation des champs
        if (idUtilisateurStr.isEmpty() || idEventStr.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Veuillez remplir tous les champs");
            return;
        }
        
        try {
            int idUtilisateur = Integer.parseInt(idUtilisateurStr);
            int idEvent = Integer.parseInt(idEventStr);
            
            // Vérifier si l'utilisateur est déjà inscrit
            if (evenementService.estInscrit(idUtilisateur, idEvent)) {
                messageLabel.setStyle("-fx-text-fill: orange;");
                messageLabel.setText("L'utilisateur est déjà inscrit à cet événement");
                return;
            }
            
            // Vérifier le nombre de participants
            int nombreParticipants = evenementService.compterParticipants(idEvent);
            // Note: Pour vérifier la capacité, il faudrait récupérer les détails de l'événement
            
            // Inscrire l'utilisateur à l'événement
            evenementService.inscrireUtilisateurEvenement(idUtilisateur, idEvent);
            
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Utilisateur inscrit avec succès à l'événement !");
            
            // Vider les champs après succès
            handleAnnuler();
            
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Les ID doivent être des nombres valides");
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur lors de l'inscription: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAnnuler() {
        idUtilisateurField.clear();
        // Ne pas vider idEventField si pré-rempli
        if (titreLabel.getText().contains("ID:")) {
            // Garder l'ID de l'événement si défini
        } else {
            idEventField.clear();
        }
        messageLabel.setText("");
    }
    
    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/afficherEvenements.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) idUtilisateurField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestion des Événements");
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du retour à la liste des événements: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
