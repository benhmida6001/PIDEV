package com.greencore.controller;

import com.greencore.model.entity.Category;
import com.greencore.model.entity.Materiel;
import com.greencore.model.entity.User;
import com.greencore.model.service.MaterielService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class MaterielController {
    @FXML private TableView<Materiel> materielTable;
    @FXML private TableColumn<Materiel, String> nameColumn;
    @FXML private TableColumn<Materiel, String> descriptionColumn;
    @FXML private TableColumn<Materiel, String> categoryColumn;
    @FXML private TableColumn<Materiel, Integer> quantityColumn;
    @FXML private TableColumn<Materiel, Integer> availableColumn;
    @FXML private TableColumn<Materiel, String> statusColumn;
    @FXML private TableColumn<Materiel, String> ownerColumn;
    @FXML private TableColumn<Materiel, Boolean> transportColumn;
    @FXML private TableColumn<Materiel, Boolean> visibleColumn;
    
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;
    @FXML private Button backButton;
    @FXML private TextField searchField;
    
    private MaterielService materielService;
    private ObservableList<Materiel> materielList = FXCollections.observableArrayList();
    private User currentUser;
    
    @FXML
    public void initialize() {
        materielService = new MaterielService();
        setupTableColumns();
        setupButtons();
        setupSearch();
        loadMateriels();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryColumn.setCellValueFactory(cellData -> {
            Category category = cellData.getValue().getCategory();
            return new SimpleStringProperty(category != null ? category.getName() : "N/A");
        });
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        ownerColumn.setCellValueFactory(cellData -> {
            User owner = cellData.getValue().getOwner();
            return new SimpleStringProperty(owner != null ? owner.getEmail() : "N/A");
        });
        transportColumn.setCellValueFactory(new PropertyValueFactory<>("transport"));
        visibleColumn.setCellValueFactory(new PropertyValueFactory<>("visible"));
        
        materielTable.setItems(materielList);
        materielTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> updateButtonStates()
        );
    }
    
    private void setupButtons() {
        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        refreshButton.setOnAction(e -> loadMateriels());
        backButton.setOnAction(e -> handleBack());
        
        updateButtonStates();
    }
    
    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                materielTable.setItems(materielList);
            } else {
                ObservableList<Materiel> filteredList = FXCollections.observableArrayList();
                String searchTerm = newVal.toLowerCase().trim();
                
                for (Materiel materiel : materielList) {
                    if (materiel.getName().toLowerCase().contains(searchTerm) ||
                        (materiel.getDescription() != null && 
                         materiel.getDescription().toLowerCase().contains(searchTerm)) ||
                        (materiel.getCategory() != null && 
                         materiel.getCategory().getName().toLowerCase().contains(searchTerm))) {
                        filteredList.add(materiel);
                    }
                }
                materielTable.setItems(filteredList);
            }
        });
    }
    
    private void loadMateriels() {
        try {
            List<Materiel> materiels = materielService.findAllVisible();
            materielList.clear();
            materielList.addAll(materiels);
            materielTable.setItems(materielList);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les matériels: " + e.getMessage());
        }
    }
    
    private void updateButtonStates() {
        Materiel selected = materielTable.getSelectionModel().getSelectedItem();
        boolean isSelected = selected != null;
        boolean canEdit = isSelected && (currentUser != null && 
            (selected.getOwner() != null && selected.getOwner().getId().equals(currentUser.getId())) ||
            currentUser.hasRole("ADMIN"));
        
        editButton.setDisable(!canEdit);
        deleteButton.setDisable(!canEdit);
    }
    
    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/materiel-form.fxml"));
            Parent root = loader.load();
            
            MaterielFormController controller = loader.getController();
            controller.setMateriel(null);
            controller.setCurrentUser(currentUser);
            
            Stage stage = new Stage();
            stage.setTitle("Ajouter un matériel");
            stage.setScene(new Scene(root, 500, 600));
            stage.showAndWait();
            
            // Refresh list after adding
            loadMateriels();
            
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEdit() {
        Materiel selected = materielTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/materiel-form.fxml"));
            Parent root = loader.load();
            
            MaterielFormController controller = loader.getController();
            controller.setMateriel(selected);
            controller.setCurrentUser(currentUser);
            
            Stage stage = new Stage();
            stage.setTitle("Modifier un matériel");
            stage.setScene(new Scene(root, 500, 600));
            stage.showAndWait();
            
            // Refresh list after editing
            loadMateriels();
            
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDelete() {
        Materiel selected = materielTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer le matériel");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer '" + selected.getName() + "' ?");
        
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                materielService.delete(selected.getId());
                showAlert("Succès", "Matériel supprimé avec succès");
                loadMateriels();
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de supprimer le matériel: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/main-app.fxml"));
            Parent root = loader.load();
            
            MainAppController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setTitle("GREENCORE - Main App");
            stage.setScene(new Scene(root, 1200, 800));
            stage.show();
            
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de revenir en arrière: " + e.getMessage());
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
