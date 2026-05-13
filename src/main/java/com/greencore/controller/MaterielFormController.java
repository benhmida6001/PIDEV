package com.greencore.controller;

import com.greencore.model.entity.Category;
import com.greencore.model.entity.Materiel;
import com.greencore.model.entity.Status;
import com.greencore.model.entity.User;
import com.greencore.model.service.CategoryService;
import com.greencore.model.service.MaterielService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MaterielFormController implements Initializable {
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField descriptionField;
    
    @FXML
    private TextField quantityField;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    private Materiel materiel;
    private User currentUser;
    private MaterielService materielService;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        materielService = new MaterielService();
        errorLabel.setVisible(false);
    }
    
    public void setMateriel(Materiel materiel) {
        this.materiel = materiel;
        if (materiel != null) {
            nameField.setText(materiel.getName());
            descriptionField.setText(materiel.getDescription());
            quantityField.setText(String.valueOf(materiel.getQuantity()));
        }
    }
    
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
    @FXML
    private void handleSave() {
        try {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String quantityText = quantityField.getText().trim();
            
            if (name.isEmpty() || description.isEmpty() || quantityText.isEmpty()) {
                showError("Tous les champs sont obligatoires");
                return;
            }
            
            int quantity = Integer.parseInt(quantityText);
            
            if (materiel == null) {
                // Create new materiel with default category
                CategoryService categoryService = new CategoryService();
                Category defaultCategory = categoryService.findByName("Autre")
                    .orElseGet(() -> categoryService.create("Autre"));
                
                materiel = materielService.create(name, description, quantity, Status.GOOD, quantity, defaultCategory, currentUser);
                showSuccess("Matériel créé avec succès!");
            } else {
                // Update existing materiel
                materiel.setName(name);
                materiel.setDescription(description);
                materiel.setQuantity(quantity);
                materiel.setAvailableQuantity(quantity);
                materielService.update(materiel);
                showSuccess("Matériel mis à jour avec succès!");
            }
            
            closeWindow();
            
        } catch (NumberFormatException e) {
            showError("La quantité doit être un nombre valide");
        } catch (Exception e) {
            showError("Erreur lors de l'enregistrement: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel() {
        closeWindow();
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: green;");
    }
    
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
