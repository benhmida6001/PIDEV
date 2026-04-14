package com.greencore.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.UtilisateurService;

public class MettreAJourPointsController {
    
    @FXML
    private TextField idField;
    
    @FXML
    private TextField pointsField;
    
    @FXML
    private Label messageLabel;
    
    private UtilisateurService utilisateurService;
    
    @FXML
    public void initialize() {
        utilisateurService = new UtilisateurService();
    }
    
    @FXML
    void handleMettreAJour(ActionEvent event) {
        String idText = idField.getText().trim();
        String pointsText = pointsField.getText().trim();
        
        // Validation des champs
        if (idText.isEmpty() || pointsText.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs");
            return;
        }
        
        try {
            int id = Integer.parseInt(idText);
            int points = Integer.parseInt(pointsText);
            
            // Appel de la méthode mettreAJourPoints
            utilisateurService.mettreAJourPoints(id, points);
            
            messageLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            messageLabel.setText("Points mis à jour avec succès !");
            
            // Vider les champs après 2 secondes
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        idField.clear();
                        pointsField.clear();
                        messageLabel.setText("");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
            messageLabel.setText("L'ID et les points doivent être des nombres entiers");
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
            messageLabel.setText("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }
    
    @FXML
    void handleAnnuler(ActionEvent event) {
        // Vider les champs
        idField.clear();
        pointsField.clear();
        messageLabel.setText("");
    }
}
