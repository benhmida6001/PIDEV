package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import services.EvenementService;
import models.Evenement;
import java.time.LocalDate;

public class AjouterEvenementController {
    
    @FXML private TextField tfNomEvent;
    @FXML private DatePicker dpDateEvent;
    @FXML private TextField tfLieuEvent;
    @FXML private TextField tfDescription;
    @FXML private TextField tfCapaciteMax;
    @FXML private TextField tfPointsOfferts;
    @FXML private Button btnAjouter;
    @FXML private Button btnRetour;
    @FXML private Button btnMap;
    @FXML private Label lblStatus;
    
    private EvenementService evenementService;
    
    @FXML
    public void initialize() {
        try {
            evenementService = new EvenementService();
            lblStatus.setText("Prêt à ajouter un nouvel événement");
        } catch (Exception e) {
            lblStatus.setText("⚠️ Mode démo - Base de données non disponible");
            System.err.println("EvenementService initialization failed: " + e.getMessage());
        }
        
        // Set default values
        dpDateEvent.setValue(LocalDate.now());
        tfCapaciteMax.setText("50");
        tfPointsOfferts.setText("10");
    }
    
        
    @FXML
    public void handleAjouterEvenement() {
        try {
            // Validate inputs
            if (tfNomEvent.getText().trim().isEmpty()) {
                afficherAlerte("Erreur", "Le nom de l'événement est obligatoire", AlertType.ERROR);
                return;
            }
            
            if (dpDateEvent.getValue() == null) {
                afficherAlerte("Erreur", "La date de l'événement est obligatoire", AlertType.ERROR);
                return;
            }
            
            if (tfLieuEvent.getText().trim().isEmpty()) {
                afficherAlerte("Erreur", "Le lieu de l'événement est obligatoire", AlertType.ERROR);
                return;
            }
            
            int capacite;
            try {
                capacite = Integer.parseInt(tfCapaciteMax.getText());
                if (capacite <= 0) {
                    afficherAlerte("Erreur", "La capacité doit être un nombre positif", AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                afficherAlerte("Erreur", "La capacité doit être un nombre valide", AlertType.ERROR);
                return;
            }
            
            int points;
            try {
                points = Integer.parseInt(tfPointsOfferts.getText());
                if (points < 0) {
                    afficherAlerte("Erreur", "Les points doivent être un nombre positif", AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                afficherAlerte("Erreur", "Les points doivent être un nombre valide", AlertType.ERROR);
                return;
            }
            
            // Create event object
            Evenement evenement = new Evenement();
            evenement.setNomEvent(tfNomEvent.getText().trim());
            evenement.setDateEvent(java.sql.Date.valueOf(dpDateEvent.getValue()));
            evenement.setLieuEvent(tfLieuEvent.getText().trim());
            evenement.setDescription(tfDescription.getText().trim());
            evenement.setCapaciteMax(capacite);
            evenement.setPointsOfferts(points);
            
            // Save event
            evenementService.ajouterEvenement(evenement);
            
            // Show success message
            afficherAlerte("Succès", "Événement ajouté avec succès!", AlertType.INFORMATION);
            
            // Clear form
            clearForm();
            lblStatus.setText("✅ Événement ajouté avec succès");
            
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors de l'ajout de l'événement: " + e.getMessage(), AlertType.ERROR);
            lblStatus.setText("❌ Erreur lors de l'ajout");
        }
    }
    
    @FXML
    public void handleAnnulerEvent() {
        clearForm();
        lblStatus.setText("Formulaire annulé");
    }
    
    @FXML
    public void handleOuvrirMap() {
        try {
            // Ouvrir la fenêtre de sélection de lieu avec Google Maps
            ouvrirMapDialog();
            
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors de l'ouverture de la carte: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Ouvre la fenêtre de dialogue pour sélectionner un lieu avec Google Maps
     */
    private void ouvrirMapDialog() {
        try {
            // Charger le FXML de la dialogue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/map_dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("📍 Sélectionner un lieu - Google Maps");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            
            // Créer la scène
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);
            
            // Récupérer le contrôleur
            MapDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            
            // Définir l'adresse actuelle comme adresse initiale
            String currentAddress = tfLieuEvent.getText().trim();
            if (!currentAddress.isEmpty()) {
                controller.setInitialAddress(currentAddress);
            }
            
            // Définir le listener pour la sélection de lieu
            controller.setLocationSelectionListener(new MapDialogController.LocationSelectionListener() {
                @Override
                public void onLocationSelected(String address) {
                    // Mettre à jour le champ de lieu avec l'adresse sélectionnée
                    tfLieuEvent.setText(address);
                    lblStatus.setText("📍 Lieu sélectionné: " + address);
                    
                    // Afficher une confirmation
                    afficherAlerte("Succès", "Lieu sélectionné avec succès!", AlertType.INFORMATION);
                }
            });
            
            // Afficher la dialogue et attendre la fermeture
            dialogStage.showAndWait();
            
            // Vérifier si une adresse a été sélectionnée
            String selectedAddress = controller.getSelectedAddress();
            if (selectedAddress != null && !selectedAddress.isEmpty()) {
                tfLieuEvent.setText(selectedAddress);
                lblStatus.setText("📍 Lieu mis à jour: " + selectedAddress);
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ouverture de la dialogue de carte: " + e.getMessage());
            afficherAlerte("Erreur", "Impossible d'ouvrir la carte: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleRetour() {
        try {
            // Close the current window and return to events management
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.close();
            lblStatus.setText("Fenêtre fermée - Retour à la gestion des événements");
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors du retour: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    private void clearForm() {
        tfNomEvent.clear();
        tfLieuEvent.clear();
        tfDescription.clear();
        tfCapaciteMax.setText("50");
        tfPointsOfferts.setText("10");
        dpDateEvent.setValue(LocalDate.now());
    }
    
    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
