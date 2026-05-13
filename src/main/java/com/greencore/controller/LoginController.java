package com.greencore.controller;

import com.greencore.model.entity.User;
import com.greencore.model.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    
    private final UserService userService = new UserService();
    
    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!userService.isValidEmail(newVal)) {
                emailField.setStyle("-fx-border-color: red;");
            } else {
                emailField.setStyle("");
            }
        });
    }
    
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        if (!userService.isValidEmail(email)) {
            showError("Email invalide");
            return;
        }
        
        Optional<User> userOpt = userService.authenticate(email, password);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            showDashboard(user);
        } else {
            showError("Email ou mot de passe incorrect");
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/register.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Créer un compte");
            stage.setScene(new Scene(root, 400, 500));
            stage.show();
            
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture du formulaire d'inscription");
        }
    }
    
    private void showDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/main-app.fxml"));
            Parent root = loader.load();
            
            MainAppController controller = loader.getController();
            controller.setCurrentUser(user);
            
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setTitle("GREENCORE - Main App");
            stage.setScene(new Scene(root, 1200, 800));
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Impossible d'ouvrir l'application principale: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
    }
}
