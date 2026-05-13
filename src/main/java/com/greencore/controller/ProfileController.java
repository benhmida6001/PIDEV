package com.greencore.controller;

import com.greencore.model.entity.User;
import com.greencore.model.service.UserService;
import com.greencore.model.service.MaterielService;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

public class ProfileController {
    @FXML private ImageView avatarImageView;
    @FXML private Button changeAvatarButton;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label userRoleBadge;
    @FXML private Label memberSinceLabel;
    @FXML private Button editProfileButton;
    @FXML private Button changePasswordButton;
    
    @FXML private Label totalMaterielsLabel;
    @FXML private Label sharedMaterielsLabel;
    @FXML private Label borrowedMaterielsLabel;
    @FXML private Label pendingRequestsLabel;
    
    @FXML private TextField emailTextField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button saveProfileButton;
    @FXML private Button cancelButton;
    
    private User currentUser;
    private final UserService userService = new UserService();
    private final MaterielService materielService = new MaterielService();
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateProfileDisplay();
        loadStats();
    }
    
    @FXML
    public void initialize() {
        setupButtons();
        setupPasswordFields();
    }
    
    private void setupButtons() {
        editProfileButton.setOnAction(e -> enableProfileEditing());
        changePasswordButton.setOnAction(e -> enablePasswordChange());
        saveProfileButton.setOnAction(e -> saveProfile());
        cancelButton.setOnAction(e -> cancelEditing());
        changeAvatarButton.setOnAction(e -> changeAvatar());
    }
    
    private void setupPasswordFields() {
        // Initially disable password fields
        currentPasswordField.setDisable(true);
        newPasswordField.setDisable(true);
        confirmPasswordField.setDisable(true);
    }
    
    private void updateProfileDisplay() {
        if (currentUser != null) {
            nameLabel.setText(currentUser.getEmail().split("@")[0]);
            emailLabel.setText(currentUser.getEmail());
            emailTextField.setText(currentUser.getEmail());
            
            // Set role badge
            String roles = String.join(", ", currentUser.getRoles());
            userRoleBadge.setText(roles.contains("ADMIN") ? "ADMIN" : "USER");
            userRoleBadge.setStyle(roles.contains("ADMIN") ? 
                "-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 6 12; -fx-font-size: 12px;" :
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 6 12; -fx-font-size: 12px;");
            
            // Set member since (placeholder)
            memberSinceLabel.setText("Member since 2024");
            
            // Initially disable editing
            emailTextField.setDisable(true);
        }
    }
    
    private void loadStats() {
        try {
            // Total materiels (using findAllVisible as approximation for now)
            long totalMateriels = materielService.findAllVisible().size();
            totalMaterielsLabel.setText(String.valueOf(totalMateriels));
            
            // Shared materiels (visible materiels)
            long sharedMateriels = materielService.findAllVisible().stream()
                .filter(m -> m.getVisible() != null && m.getVisible())
                .count();
            sharedMaterielsLabel.setText(String.valueOf(sharedMateriels));
            
            // Borrowed materiels (placeholder - using available count)
            long borrowedMateriels = materielService.findAvailable().size();
            borrowedMaterielsLabel.setText(String.valueOf(borrowedMateriels));
            
            // Pending requests (placeholder)
            pendingRequestsLabel.setText("0");
            
        } catch (Exception e) {
            System.err.println("Error loading stats: " + e.getMessage());
        }
    }
    
    private void enableProfileEditing() {
        emailTextField.setDisable(false);
        showAlert("Info", "You can now edit your email. Click Save Profile when done.");
    }
    
    private void enablePasswordChange() {
        currentPasswordField.setDisable(false);
        newPasswordField.setDisable(false);
        confirmPasswordField.setDisable(false);
        showAlert("Info", "Enter your current and new password. Click Save Profile when done.");
    }
    
    private void saveProfile() {
        try {
            // Update email if changed
            String newEmail = emailTextField.getText().trim();
            if (!newEmail.equals(currentUser.getEmail()) && !newEmail.isEmpty()) {
                if (isValidEmail(newEmail)) {
                    currentUser.setEmail(newEmail);
                    userService.update(currentUser);
                    emailLabel.setText(newEmail);
                    showAlert("Success", "Email updated successfully!");
                } else {
                    showAlert("Error", "Invalid email format!");
                    return;
                }
            }
            
            // Update password if provided
            String currentPass = currentPasswordField.getText();
            String newPass = newPasswordField.getText();
            String confirmPass = confirmPasswordField.getText();
            
            if (!currentPass.isEmpty() || !newPass.isEmpty() || !confirmPass.isEmpty()) {
                if (newPass.length() < 6) {
                    showAlert("Error", "Password must be at least 6 characters!");
                    return;
                }
                
                if (!newPass.equals(confirmPass)) {
                    showAlert("Error", "New passwords don't match!");
                    return;
                }
                
                // Verify current password
                BCrypt.Result result = BCrypt.verifyer().verify(currentPass.toCharArray(), currentUser.getPassword());
                if (!result.verified) {
                    showAlert("Error", "Current password is incorrect!");
                    return;
                }
                
                // Update password
                userService.updatePassword(currentUser, newPass);
                showAlert("Success", "Password updated successfully!");
                
                // Clear password fields
                currentPasswordField.clear();
                newPasswordField.clear();
                confirmPasswordField.clear();
            }
            
            // Disable editing again
            emailTextField.setDisable(true);
            currentPasswordField.setDisable(true);
            newPasswordField.setDisable(true);
            confirmPasswordField.setDisable(true);
            
        } catch (Exception e) {
            showAlert("Error", "Failed to update profile: " + e.getMessage());
        }
    }
    
    private void cancelEditing() {
        emailTextField.setText(currentUser.getEmail());
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
        
        emailTextField.setDisable(true);
        currentPasswordField.setDisable(true);
        newPasswordField.setDisable(true);
        confirmPasswordField.setDisable(true);
    }
    
    private void changeAvatar() {
        showAlert("Info", "Avatar upload feature coming soon!");
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
