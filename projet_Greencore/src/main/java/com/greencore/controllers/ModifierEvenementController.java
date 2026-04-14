package com.greencore.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Evenement;
import services.EvenementService;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifierEvenementController {
    
    @FXML
    private TextField nomField;
    
    @FXML
    private TextField dateField;
    
    @FXML
    private TextField lieuField;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private TextField capaciteField;
    
    @FXML
    private TextField pointsField;
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private Label titreLabel;
    
    private EvenementService evenementService;
    private Evenement evenementAModifier;
    
    public ModifierEvenementController() {
        this.evenementService = new EvenementService();
    }
    
    public void setEvenement(Evenement evenement) {
        this.evenementAModifier = evenement;
        chargerDonnees();
    }
    
    private void chargerDonnees() {
        if (evenementAModifier != null) {
            nomField.setText(evenementAModifier.getNomEvent());
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateField.setText(formatter.format(evenementAModifier.getDateEvent()));
            
            lieuField.setText(evenementAModifier.getLieuEvent());
            descriptionArea.setText(evenementAModifier.getDescription());
            capaciteField.setText(String.valueOf(evenementAModifier.getCapaciteMax()));
            pointsField.setText(String.valueOf(evenementAModifier.getPointsOfferts()));
            
            titreLabel.setText("Modifier l'événement: " + evenementAModifier.getNomEvent());
        }
    }
    
    @FXML
    private void handleModifier(ActionEvent event) {
        if (evenementAModifier == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Aucun événement à modifier");
            return;
        }
        
        String nom = nomField.getText().trim();
        String dateStr = dateField.getText().trim();
        String lieu = lieuField.getText().trim();
        String description = descriptionArea.getText().trim();
        String capaciteStr = capaciteField.getText().trim();
        String pointsStr = pointsField.getText().trim();
        
        // Validation des champs
        if (nom.isEmpty() || dateStr.isEmpty() || lieu.isEmpty() || description.isEmpty() || capaciteStr.isEmpty() || pointsStr.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Veuillez remplir tous les champs");
            return;
        }
        
        try {
            // Validation de la date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date dateEvent = formatter.parse(dateStr);
            
            // Validation des nombres
            int capaciteMax = Integer.parseInt(capaciteStr);
            int pointsOfferts = Integer.parseInt(pointsStr);
            
            // Mise à jour de l'événement
            evenementAModifier.setNomEvent(nom);
            evenementAModifier.setDateEvent(dateEvent);
            evenementAModifier.setLieuEvent(lieu);
            evenementAModifier.setDescription(description);
            evenementAModifier.setCapaciteMax(capaciteMax);
            evenementAModifier.setPointsOfferts(pointsOfferts);
            
            // Modification dans la base de données
            evenementService.modifierEvenement(evenementAModifier);
            
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Événement modifié avec succès !");
            
            // Fermer la fenêtre après succès
            handleAnnuler();
            
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("La capacité et les points doivent être des nombres valides");
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAnnuler() {
        nomField.clear();
        dateField.clear();
        lieuField.clear();
        descriptionArea.clear();
        capaciteField.clear();
        pointsField.clear();
        messageLabel.setText("");
    }
    
    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/afficherEvenements.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) nomField.getScene().getWindow();
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
