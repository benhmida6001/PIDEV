package com.greencore.controller;

import com.greencore.model.entity.User;
import com.greencore.model.entity.Materiel;
import com.greencore.model.service.MaterielService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserDashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label subtitleLabel;
    @FXML private Button refreshButton;
    
    @FXML private Label totalItemsLabel;
    @FXML private Label sharedItemsLabel;
    @FXML private Label borrowedItemsLabel;
    @FXML private Label pendingRequestsLabel;
    
    @FXML private ProgressBar totalProgress;
    @FXML private ProgressBar sharedProgress;
    @FXML private ProgressBar borrowedProgress;
    @FXML private ProgressBar pendingProgress;
    
    @FXML private TableView<ActivityItem> activityTable;
    @FXML private TableColumn<ActivityItem, String> activityTypeColumn;
    @FXML private TableColumn<ActivityItem, String> activityItemColumn;
    @FXML private TableColumn<ActivityItem, String> activityUserColumn;
    @FXML private TableColumn<ActivityItem, String> activityDateColumn;
    @FXML private TableColumn<ActivityItem, String> activityStatusColumn;
    
    @FXML private Button viewAllButton;
    
    private User currentUser;
    private final MaterielService materielService = new MaterielService();
    private ObservableList<ActivityItem> activityList = FXCollections.observableArrayList();
    
    public static class ActivityItem {
        private String type;
        private String item;
        private String user;
        private String date;
        private String status;
        
        public ActivityItem(String type, String item, String user, String date, String status) {
            this.type = type;
            this.item = item;
            this.user = user;
            this.date = date;
            this.status = status;
        }
        
        public String getType() { return type; }
        public String getItem() { return item; }
        public String getUser() { return user; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateWelcomeMessage();
        loadStats();
        loadRecentActivity();
    }
    
    @FXML
    public void initialize() {
        setupActivityTable();
        setupButtons();
    }
    
    private void setupActivityTable() {
        activityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        activityItemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        activityUserColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        activityDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        activityStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        activityTable.setItems(activityList);
    }
    
    private void setupButtons() {
        refreshButton.setOnAction(e -> {
            loadStats();
            loadRecentActivity();
        });
        
        viewAllButton.setOnAction(e -> {
            // Navigate to full activity view (placeholder)
            showAlert("Info", "Full activity view coming soon!");
        });
    }
    
    private void updateWelcomeMessage() {
        if (currentUser != null) {
            String userName = currentUser.getEmail().split("@")[0];
            welcomeLabel.setText("Welcome back, " + userName + "!");
            subtitleLabel.setText("Manage your equipment sharing activities");
        }
    }
    
    private void loadStats() {
        try {
            // Get all visible materiels for stats
            java.util.List<Materiel> allMateriels = materielService.findAllVisible();
            
            // Total items
            long totalItems = allMateriels.size();
            totalItemsLabel.setText(String.valueOf(totalItems));
            totalProgress.setProgress(totalItems > 0 ? 0.75 : 0.0); // Mock progress
            
            // Shared items (visible materiels)
            long sharedItems = allMateriels.stream()
                .filter(m -> m.getVisible() != null && m.getVisible())
                .count();
            sharedItemsLabel.setText(String.valueOf(sharedItems));
            sharedProgress.setProgress(sharedItems > 0 ? 0.6 : 0.0); // Mock progress
            
            // Borrowed items (mock for now)
            long borrowedItems = 3; // Placeholder
            borrowedItemsLabel.setText(String.valueOf(borrowedItems));
            borrowedProgress.setProgress(borrowedItems > 0 ? 0.4 : 0.0); // Mock progress
            
            // Pending requests (mock for now)
            long pendingRequests = 2; // Placeholder
            pendingRequestsLabel.setText(String.valueOf(pendingRequests));
            pendingProgress.setProgress(pendingRequests > 0 ? 0.3 : 0.0); // Mock progress
            
        } catch (Exception e) {
            System.err.println("Error loading stats: " + e.getMessage());
        }
    }
    
    private void loadRecentActivity() {
        try {
            activityList.clear();
            
            // Add some sample activity items
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
            
            activityList.add(new ActivityItem(
                "BORROW", "Perceuse", "John Doe", today, "APPROVED"
            ));
            
            activityList.add(new ActivityItem(
                "SHARE", "Tondeuse", currentUser.getEmail().split("@")[0], today, "ACTIVE"
            ));
            
            activityList.add(new ActivityItem(
                "REPAIR", "Multimètre", "Jane Smith", today, "PENDING"
            ));
            
            activityList.add(new ActivityItem(
                "BORROW", "Outils", currentUser.getEmail().split("@")[0], "May 10, 2024", "RETURNED"
            ));
            
        } catch (Exception e) {
            System.err.println("Error loading activity: " + e.getMessage());
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
