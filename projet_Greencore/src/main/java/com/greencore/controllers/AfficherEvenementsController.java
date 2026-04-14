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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import models.Evenement;
import services.EvenementService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AfficherEvenementsController {
    
    @FXML
    private TableView<Evenement> evenementsTable;
    
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
    private ObservableList<Evenement> evenementsList;
    
    public AfficherEvenementsController() {
        this.evenementService = new EvenementService();
        this.evenementsList = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idEvent"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomEvent"));
        dateColumn.setCellValueFactory(param -> {
            Evenement evenement = param.getValue();
            if (evenement != null && evenement.getDateEvent() != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(
                    formatter.format(evenement.getDateEvent())
                );
            }
            return new SimpleStringProperty("");
        });
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieuEvent"));
        capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capaciteMax"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("pointsOfferts"));
        
        // Formatage de la date pour l'affichage
        dateColumn.setCellFactory(column -> new javafx.scene.control.TableCell<Evenement, String>() {
            private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    Evenement evenement = getTableRow().getItem();
                    if (evenement != null && evenement.getDateEvent() != null) {
                        setText(formatter.format(evenement.getDateEvent()));
                    }
                }
            }
        });
        
        // Charger les événements
        chargerEvenements();
    }
    
    private void chargerEvenements() {
        try {
            List<Evenement> evenements = evenementService.afficherEvenements();
            evenementsList.clear();
            evenementsList.addAll(evenements);
            evenementsTable.setItems(evenementsList);
            
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText(evenements.size() + " événement(s) trouvé(s)");
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur lors du chargement des événements: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleActualiser(ActionEvent event) {
        chargerEvenements();
    }
    
    @FXML
    private void handleAjouterEvenement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ajouterEvenement.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.setTitle("Ajouter un Événement");
            stage.setMinWidth(500);
            stage.setMinHeight(450);
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'ajout: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleModifier(ActionEvent event) {
        Evenement selectedEvenement = evenementsTable.getSelectionModel().getSelectedItem();
        if (selectedEvenement == null) {
            showAlert("Attention", "Veuillez sélectionner un événement à modifier");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifierEvenement.fxml"));
            Parent root = loader.load();
            
            ModifierEvenementController controller = loader.getController();
            controller.setEvenement(selectedEvenement);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.setTitle("Modifier un Événement");
            stage.setMinWidth(500);
            stage.setMinHeight(450);
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de modification: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSupprimer(ActionEvent event) {
        Evenement selectedEvenement = evenementsTable.getSelectionModel().getSelectedItem();
        if (selectedEvenement == null) {
            showAlert("Attention", "Veuillez sélectionner un événement à supprimer");
            return;
        }
        
        try {
            evenementService.supprimerEvenement(selectedEvenement.getIdEvent());
            chargerEvenements(); // Rafraîchir la liste
            showAlert("Succès", "Événement supprimé avec succès");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRechercherParId(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rechercherEvenementParId.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800, 500);
            stage.setScene(scene);
            stage.setTitle("Rechercher un Événement par ID");
            stage.setMinWidth(700);
            stage.setMinHeight(450);
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de recherche: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRechercherParCritere(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rechercherEvenementsParCritere.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800, 500);
            stage.setScene(scene);
            stage.setTitle("Rechercher des Événements par Critère");
            stage.setMinWidth(700);
            stage.setMinHeight(450);
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de recherche: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleInscrireUtilisateur(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/inscrireUtilisateurEvenement.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            Scene scene = new Scene(root, 500, 350);
            stage.setScene(scene);
            stage.setTitle("Inscription à un Événement");
            stage.setMinWidth(450);
            stage.setMinHeight(300);
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'inscription: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleMettreAJourParticipations(ActionEvent event) {
        Evenement selectedEvenement = evenementsTable.getSelectionModel().getSelectedItem();
        if (selectedEvenement == null) {
            showAlert("Attention", "Veuillez sélectionner un événement");
            return;
        }
        
        // TODO: Implémenter la gestion des participations
        showAlert("Info", "Fonctionnalité de gestion des participations à implémenter");
    }
    
    @FXML
    private void handleDetails(ActionEvent event) {
        Evenement selectedEvenement = evenementsTable.getSelectionModel().getSelectedItem();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) evenementsTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Application Greencore");
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du retour au menu principal: " + e.getMessage());
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
