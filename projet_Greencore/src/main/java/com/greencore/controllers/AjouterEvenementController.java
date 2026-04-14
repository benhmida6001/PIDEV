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

public class AjouterEvenementController {
    
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
    
    private EvenementService evenementService;
    
    public AjouterEvenementController() {
        this.evenementService = new EvenementService();
    }
    
    @FXML
    private void handleAjouter(ActionEvent event) {
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
            
            // Création de l'événement
            Evenement evenement = new Evenement(
                nom, dateEvent, lieu, description, capaciteMax, pointsOfferts
            );
            
            // Ajout de l'événement
            evenementService.ajouterEvenement(evenement);
            
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Événement ajouté avec succès !");
            
            // Vider les champs après succès
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) nomField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Application Greencore");
            stage.show();
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur lors du retour au menu principal");
        }
    }
}
