package com.greencore.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import models.Evenement;
import services.EvenementService;
import java.text.SimpleDateFormat;
import java.util.List;

public class RechercherEvenementParIdController {
    
    @FXML
    private TextField idField;
    
    @FXML
    private Button rechercherButton;
    
    @FXML
    private TableView<Evenement> resultatsTable;
    
    @FXML
    private TableColumn<Evenement, Integer> idColumn;
    
    @FXML
    private TableColumn<Evenement, String> nomColumn;
    
    @FXML
    private TableColumn<Evenement, String> dateColumn;
    
    @FXML
    private TableColumn<Evenement, String> lieuColumn;
    
    @FXML
    private TableColumn<Evenement, Integer> capaciteColumn;
    
    @FXML
    private TableColumn<Evenement, Integer> pointsColumn;
    
    @FXML
    private Label messageLabel;
    
    private EvenementService evenementService;
    private ObservableList<Evenement> resultatsList;
    
    public RechercherEvenementParIdController() {
        this.evenementService = new EvenementService();
        this.resultatsList = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idEvent"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomEvent"));
        
        // Configuration de la colonne date avec formatage
        dateColumn.setCellValueFactory(param -> {
            Evenement evenement = param.getValue();
            if (evenement != null && evenement.getDateEvent() != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(formatter.format(evenement.getDateEvent()));
            }
            return new SimpleStringProperty("");
        });
        
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieuEvent"));
        capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capaciteMax"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("pointsOfferts"));
        
        // Lier la table à la liste observable
        resultatsTable.setItems(resultatsList);
    }
    
    @FXML
    private void handleRechercher(ActionEvent event) {
        String idStr = idField.getText().trim();
        
        if (idStr.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Veuillez entrer un ID d'événement");
            return;
        }
        
        try {
            int idEvent = Integer.parseInt(idStr);
            
            // Rechercher l'événement par ID
            List<Evenement> evenements = evenementService.rechercherEvenementParId(idEvent);
            
            // Vider les résultats précédents
            resultatsList.clear();
            
            if (evenements.isEmpty()) {
                messageLabel.setStyle("-fx-text-fill: orange;");
                messageLabel.setText("Aucun événement trouvé avec l'ID: " + idEvent);
            } else {
                resultatsList.addAll(evenements);
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(evenements.size() + " événement(s) trouvé(s)");
            }
            
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("L'ID doit être un nombre valide");
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur lors de la recherche: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleVider(ActionEvent event) {
        idField.clear();
        resultatsList.clear();
        messageLabel.setText("");
    }
    
    @FXML
    private void handleDetails(ActionEvent event) {
        Evenement selectedEvenement = resultatsTable.getSelectionModel().getSelectedItem();
        if (selectedEvenement == null) {
            showAlert("Attention", "Veuillez sélectionner un événement");
            return;
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String details = "Détails de l'événement:\n\n" +
            "ID: " + selectedEvenement.getIdEvent() + "\n" +
            "Nom: " + selectedEvenement.getNomEvent() + "\n" +
            "Date: " + formatter.format(selectedEvenement.getDateEvent()) + "\n" +
            "Lieu: " + selectedEvenement.getLieuEvent() + "\n" +
            "Description: " + selectedEvenement.getDescription() + "\n" +
            "Capacité maximale: " + selectedEvenement.getCapaciteMax() + "\n" +
            "Points offerts: " + selectedEvenement.getPointsOfferts();
        
        showAlert("Détails de l'événement", details);
    }
    
    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/afficherEvenements.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) idField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestion des Événements");
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du retour à la liste des événements: " + e.getMessage());
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
