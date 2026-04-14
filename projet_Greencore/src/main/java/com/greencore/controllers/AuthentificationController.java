package com.greencore.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UtilisateurService;
import models.Utilisateur;
import java.util.List;

public class AuthentificationController {

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField passwordField;
    
    private UtilisateurService utilisateurService;
    private Utilisateur utilisateurAuthentifie;

    public AuthentificationController() {
        this.utilisateurService = new UtilisateurService();
    }

    @FXML
    void handleAnnuler(ActionEvent event) {
        emailField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }

    @FXML
    void handleConnexion(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        
        // Validation des champs
        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs");
            return;
        }
        
        try {
            // Utilisation de la méthode authentifier qui retourne List<Utilisateur>
            List<Utilisateur> authUsers = utilisateurService.authentifier(email, password);
            
            if (!authUsers.isEmpty()) {
                utilisateurAuthentifie = authUsers.get(0);
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Connexion réussie ! Bienvenue " + utilisateurAuthentifie.getPrenom());
                
                // Rediriger vers la page principale après 2 secondes
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        javafx.application.Platform.runLater(this::ouvrirPagePrincipale);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Email ou mot de passe incorrect");
            }
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur de connexion à la base de données");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleAnnuler() {
        // Vider les champs
        emailField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
    
    private void ouvrirPagePrincipale() {
        try {
            // Charger la page principale
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
            Parent root = loader.load();
            
            // Récupérer le contrôleur et passer l'utilisateur connecté
            MainController mainController = loader.getController();
            mainController.setUtilisateurConnecte(utilisateurAuthentifie);
            
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Application Greencore");
            stage.show();
            
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur lors du chargement de l'application");
            e.printStackTrace();
        }
    }
}
