package com.greencore.controller;

import com.greencore.model.entity.User;
import com.greencore.model.entity.Materiel;
import com.greencore.model.service.MaterielService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class DashboardController {
    @FXML private BorderPane mainBorderPane;
    @FXML private TableView<Materiel> materielTable;
    @FXML private TableColumn<Materiel, String> nameColumn;
    @FXML private TableColumn<Materiel, String> categoryColumn;
    @FXML private TableColumn<Materiel, Integer> quantityColumn;
    @FXML private TableColumn<Materiel, Integer> availableColumn;
    @FXML private TextField searchField;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button borrowButton;
    @FXML private Button refreshButton;
    @FXML private Label welcomeLabel;
    @FXML private ImageView materielImageView;
    
    private final MaterielService materielService = new MaterielService();
    private ObservableList<Materiel> materielList = FXCollections.observableArrayList();
    private User currentUser;
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Bienvenue, " + user.getEmail());
        loadMateriels();
    }
    
    @FXML
    public void initialize() {
        setupTableColumns();
        setupButtons();
        setupSearch();
    }
    
    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        
        materielTable.setItems(materielList);
        materielTable.setRowFactory(tv -> {
            TableRow<Materiel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showMaterielDetails(row.getItem());
                }
            });
            return row;
        });
    }
    
    private void setupButtons() {
        addButton.setOnAction(e -> handleNewMateriel());
        editButton.setOnAction(e -> handleEditMateriel());
        borrowButton.setOnAction(e -> handleRequestBorrow());
        refreshButton.setOnAction(e -> loadMateriels());
    }
    
    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                loadMateriels();
            } else {
                searchMateriels(newVal);
            }
        });
    }
    
    private void loadMateriels() {
        try {
            List<Materiel> materiels = materielService.findAllVisible();
            materielList.clear();
            materielList.addAll(materiels);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les matériels: " + e.getMessage());
        }
    }
    
    private void searchMateriels(String searchTerm) {
        try {
            List<Materiel> materiels = materielService.findAllVisible();
            materielList.clear();
            materielList.addAll(materiels.stream()
                .filter(m -> m.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList());
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la recherche: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleNewMateriel() {
        showMaterielForm(null);
    }
    
    @FXML
    private void handleEditMateriel() {
        Materiel selected = materielTable.getSelectionModel().getSelectedItem();
        if (selected != null && canEdit(selected)) {
            showMaterielForm(selected);
        } else if (selected != null) {
            showAlert("Erreur", "Vous ne pouvez modifier que vos propres matériels");
        }
    }
    
    @FXML
    private void handleRequestBorrow() {
        Materiel selected = materielTable.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getAvailableQuantity() > 0) {
            showOperationForm(selected);
        } else if (selected != null) {
            showAlert("Erreur", "Ce matériel n'est pas disponible pour l'emprunt");
        }
    }
    
    private boolean canEdit(Materiel materiel) {
        return currentUser != null && currentUser.getId().equals(materiel.getOwner().getId());
    }
    
    private void showMaterielForm(Materiel materiel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/materiel-form.fxml"));
            Parent root = loader.load();
            
            MaterielFormController controller = loader.getController();
            controller.setMateriel(materiel);
            controller.setCurrentUser(currentUser);
            
            Stage stage = new Stage();
            stage.setTitle(materiel == null ? "Nouveau Matériel" : "Modifier Matériel");
            stage.setScene(new Scene(root, 500, 600));
            stage.showAndWait();
            
            loadMateriels();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire: " + e.getMessage());
        }
    }
    
    private void showOperationForm(Materiel materiel) {
        showAlert("Info", "Formulaire d'emprunt à implémenter pour: " + materiel.getName());
    }
    
    private void showMaterielDetails(Materiel materiel) {
        showAlert("Détails", "Nom: " + materiel.getName() + 
                 "\nDescription: " + materiel.getDescription() +
                 "\nQuantité: " + materiel.getQuantity() +
                 "\nDisponible: " + materiel.getAvailableQuantity() +
                 "\nPropriétaire: " + materiel.getOwner().getEmail());
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
