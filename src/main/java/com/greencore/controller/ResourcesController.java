package com.greencore.controller;

import com.greencore.model.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ResourcesController {
    @FXML private Button materielsButton;
    @FXML private Button borrowButton;
    @FXML private Button shareButton;
    @FXML private Button repairButton;
    
    private User currentUser;
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    @FXML
    public void initialize() {
        setupButtons();
    }
    
    private void setupButtons() {
        materielsButton.setOnAction(e -> {
            // Navigate to Materiels Management
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/materiel-list.fxml"));
                Parent root = loader.load();
                
                MaterielController controller = loader.getController();
                controller.setCurrentUser(currentUser);
                
                Stage stage = (Stage) materielsButton.getScene().getWindow();
                stage.setTitle("GREENCORE - Gestion des Matériels");
                stage.setScene(new Scene(root, 1200, 800));
                stage.setResizable(true);
                stage.setMaximized(true);
                stage.show();
            } catch (Exception ex) {
                System.err.println("Error navigating to materiels: " + ex.getMessage());
            }
        });
        
        borrowButton.setOnAction(e -> {
            // Navigate to Dashboard (which shows materiels list)
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/main-app.fxml"));
                Parent root = loader.load();
                
                MainAppController controller = loader.getController();
                controller.setCurrentUser(currentUser);
                
                // Trigger dashboard view
                controller.loadDashboardView();
                
                Stage stage = (Stage) borrowButton.getScene().getWindow();
                stage.setTitle("GREENCORE - Dashboard");
                stage.setScene(new Scene(root, 1200, 800));
                stage.setResizable(true);
                stage.setMaximized(true);
                stage.show();
            } catch (Exception ex) {
                System.err.println("Error navigating to dashboard: " + ex.getMessage());
            }
        });
        
        shareButton.setOnAction(e -> {
            // Open materiel form for adding new equipment
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/materiel-form.fxml"));
                Parent root = loader.load();
                
                MaterielFormController controller = loader.getController();
                controller.setMateriel(null);
                controller.setCurrentUser(currentUser);
                
                Stage stage = new Stage();
                stage.setTitle("Share Equipment");
                stage.setScene(new Scene(root, 500, 600));
                stage.showAndWait();
            } catch (Exception ex) {
                System.err.println("Error opening share form: " + ex.getMessage());
            }
        });
        
        repairButton.setOnAction(e -> {
            // Navigate to dashboard for repair requests
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/main-app.fxml"));
                Parent root = loader.load();
                
                MainAppController controller = loader.getController();
                controller.setCurrentUser(currentUser);
                
                // Trigger dashboard view
                controller.loadDashboardView();
                
                Stage stage = (Stage) repairButton.getScene().getWindow();
                stage.setTitle("GREENCORE - Dashboard");
                stage.setScene(new Scene(root, 1200, 800));
                stage.setResizable(true);
                stage.setMaximized(true);
                stage.show();
            } catch (Exception ex) {
                System.err.println("Error navigating to dashboard: " + ex.getMessage());
            }
        });
    }
}
