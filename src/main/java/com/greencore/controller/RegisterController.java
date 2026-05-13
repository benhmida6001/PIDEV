package com.greencore.controller;

import com.greencore.model.entity.User;
import com.greencore.model.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Arrays;

public class RegisterController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    @FXML private Button registerButton;
    @FXML private Button cancelButton;
    
    private final UserService userService = new UserService();
    
    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        
        // Real-time email validation
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!userService.isValidEmail(newVal)) {
                emailField.setStyle("-fx-border-color: red;");
            } else {
                emailField.setStyle("");
            }
        });
        
        // Real-time password validation
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!userService.isValidPassword(newVal)) {
                passwordField.setStyle("-fx-border-color: red;");
            } else {
                passwordField.setStyle("");
            }
        });
    }
    
    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        if (!userService.isValidEmail(email)) {
            showError("Email invalide");
            return;
        }
        
        if (!userService.isValidPassword(password)) {
            showError("Le mot de passe doit contenir au moins 6 caractères");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Les mots de passe ne correspondent pas");
            return;
        }
        
        try {
            User user = userService.register(email, password, Arrays.asList("USER"));
            showSuccess("Compte créé avec succès!");
            
            // Redirect to login
            handleCancel();
            
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Erreur lors de la création du compte: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
