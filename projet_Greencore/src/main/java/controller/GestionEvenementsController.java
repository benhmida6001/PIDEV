package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.EvenementService;
import models.Evenement;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.util.Optional;

public class GestionEvenementsController {
    
    @FXML private TableView<Evenement> tableViewEvenements;
    @FXML private TableColumn<Evenement, Integer> colIdEvent;
    @FXML private TableColumn<Evenement, String> colNomEvent;
    @FXML private TableColumn<Evenement, Date> colDateEvent;
    @FXML private TableColumn<Evenement, String> colLieuEvent;
    @FXML private TableColumn<Evenement, Integer> colCapaciteMax;
    @FXML private TableColumn<Evenement, Integer> colPointsOfferts;
    @FXML private Button btnAjouter;
    @FXML private Button btnRechercher;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnCarte;
    @FXML private Label lblStatus;
    
    private final EvenementService evenementService = new EvenementService();
    
    @FXML
    public void initialize() {
        lblStatus.setText("Chargement des événements...");
        
        // Configure table columns
        colIdEvent.setCellValueFactory(new PropertyValueFactory<>("idEvent"));
        colNomEvent.setCellValueFactory(new PropertyValueFactory<>("nomEvent"));
        colDateEvent.setCellValueFactory(new PropertyValueFactory<>("dateEvent"));
        colDateEvent.setCellFactory(column -> new TableCell<Evenement, Date>() {
            private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            
            @Override
            protected void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(format.format(date));
                }
            }
        });
        colLieuEvent.setCellValueFactory(new PropertyValueFactory<>("lieuEvent"));
        colCapaciteMax.setCellValueFactory(new PropertyValueFactory<>("capaciteMax"));
        colPointsOfferts.setCellValueFactory(new PropertyValueFactory<>("pointsOfferts"));
        
        // Load events
        loadEvents();
        
        lblStatus.setText("Prêt - " + tableViewEvenements.getItems().size() + " événements chargés");
    }
    
    private void loadEvents() {
        try {
            List<Evenement> evenements = evenementService.afficherEvenements();
            tableViewEvenements.getItems().clear();
            tableViewEvenements.getItems().addAll(evenements);
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors du chargement des événements: " + e.getMessage(), AlertType.ERROR);
            lblStatus.setText("❌ Erreur de chargement");
        }
    }
    
    @FXML
    public void handleAjouterEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/ajouter_evenement.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Événement");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Refresh the table after adding
            loadEvents();
            lblStatus.setText("✅ Événement ajouté et liste rafraîchie");
            
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors de l'ouverture du formulaire: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleModifierEvenement() {
        Evenement selectedEvent = tableViewEvenements.getSelectionModel().getSelectedItem();
        
        if (selectedEvent == null) {
            afficherAlerte("Information", "Veuillez sélectionner un événement à modifier", AlertType.INFORMATION);
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/modifier_evenement.fxml"));
            Parent root = loader.load();
            
            // Get the controller and pass the selected event
            ModifierEvenementController controller = loader.getController();
            controller.setEvenementToModify(selectedEvent);
            
            Stage stage = new Stage();
            stage.setTitle("Modifier un Événement");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Refresh the table after modification
            loadEvents();
            lblStatus.setText("✅ Événement modifié et liste rafraîchie");
            
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors de l'ouverture du formulaire: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleSupprimerEvenement() {
        Evenement selectedEvent = tableViewEvenements.getSelectionModel().getSelectedItem();
        
        if (selectedEvent == null) {
            afficherAlerte("Information", "Veuillez sélectionner un événement à supprimer", AlertType.INFORMATION);
            return;
        }
        
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de Suppression");
        confirmAlert.setHeaderText("Supprimer l'événement");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer l'événement \"" + selectedEvent.getNomEvent() + "\" ?");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                evenementService.supprimerEvenement(selectedEvent.getIdEvent());
                tableViewEvenements.getItems().remove(selectedEvent);
                lblStatus.setText("✅ Événement supprimé avec succès");
            } catch (Exception e) {
                afficherAlerte("Erreur", "Erreur lors de la suppression: " + e.getMessage(), AlertType.ERROR);
                lblStatus.setText("❌ Erreur de suppression");
            }
        }
    }
    
    @FXML
    public void handleRechercherEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/rechercher_evenement.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Rechercher un Événement");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            lblStatus.setText("🔍 Recherche d'événements terminée");
            
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors de l'ouverture de la fenêtre de recherche: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleAfficherCarte() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/map_view.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("🗺️ Carte des Événements");
            stage.setScene(new Scene(root));
            stage.show();
            
            lblStatus.setText("🗺️ Carte des événements ouverte");
            
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors de l'ouverture de la carte: " + e.getMessage(), AlertType.ERROR);
            lblStatus.setText("❌ Erreur d'ouverture de la carte");
        }
    }
    
    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
