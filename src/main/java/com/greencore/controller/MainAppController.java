package com.greencore.controller;

import com.greencore.model.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainAppController {
    @FXML private Button resourcesButton;
    @FXML private Button dashboardButton;
    @FXML private Button profileButton;
    @FXML private Button logoutButton;
    @FXML private TextField searchField;
    @FXML private VBox contentArea;
    
    private User currentUser;
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        // Load default view (Resources)
        loadResourcesView();
    }
    
    @FXML
    public void initialize() {
        setupNavigationButtons();
        setupSearch();
    }
    
    private void setupNavigationButtons() {
        resourcesButton.setOnAction(e -> {
            setActiveNav(resourcesButton);
            loadResourcesView();
        });
        
        dashboardButton.setOnAction(e -> {
            setActiveNav(dashboardButton);
            loadDashboardView();
        });
        
        profileButton.setOnAction(e -> {
            setActiveNav(profileButton);
            loadProfileView();
        });
        
        logoutButton.setOnAction(e -> handleLogout());
    }
    
    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                // Trigger search in materiels view
                // This will be implemented when dashboard view is loaded
            }
        });
    }
    
    private void setActiveNav(Button activeButton) {
        // Reset all buttons to default style
        resourcesButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #2E7D32; -fx-border-color: transparent;");
        dashboardButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #2E7D32; -fx-border-color: transparent;");
        profileButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #2E7D32; -fx-border-color: transparent;");
        
        // Set active button with underline
        activeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #2E7D32; -fx-border-color: transparent; -fx-underline: true;");
    }
    
    private void loadResourcesView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/resources.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (Exception e) {
            System.err.println("Error loading resources view: " + e.getMessage());
        }
    }
    
    public void loadDashboardView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/user-dashboard.fxml"));
            Parent root = loader.load();
            
            // Inject current user into dashboard controller
            UserDashboardController controller = loader.getController();
            if (controller != null) {
                controller.setCurrentUser(currentUser);
            }
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (Exception e) {
            System.err.println("Error loading dashboard view: " + e.getMessage());
        }
    }
    
    private void loadProfileView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/profile.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (Exception e) {
            System.err.println("Error loading profile view: " + e.getMessage());
        }
    }
    
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setTitle("GREENCORE - Login");
            stage.setScene(new Scene(root, 800, 600));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error during logout: " + e.getMessage());
        }
    }
}
