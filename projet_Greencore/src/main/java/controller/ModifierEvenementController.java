package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import services.EvenementService;
import models.Evenement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ModifierEvenementController {
    
    @FXML private TextField tfNomEvent;
    @FXML private DatePicker dpDateEvent;
    @FXML private TextField tfLieuEvent;
    @FXML private TextField tfDescription;
    @FXML private TextField tfCapaciteMax;
    @FXML private TextField tfPointsOfferts;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;
    @FXML private Button btnRetour;
    @FXML private Label lblStatus;
    
    private EvenementService evenementService;
    private Evenement evenementToModify;
    
    @FXML
    public void initialize() {
        try {
            evenementService = new EvenementService();
            lblStatus.setText("Prêt à modifier l'événement");
        } catch (Exception e) {
            lblStatus.setText("⚠️ Mode démo - Base de données non disponible");
            System.err.println("EvenementService initialization failed: " + e.getMessage());
        }
    }
    
    public void setEvenementToModify(Evenement evenement) {
        this.evenementToModify = evenement;
        
        if (evenement != null) {
            // Fill form with event data
            tfNomEvent.setText(evenement.getNomEvent());
            
            // Convert Date to LocalDate for DatePicker
            if (evenement.getDateEvent() != null) {
                LocalDate localDate = evenement.getDateEvent().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
                dpDateEvent.setValue(localDate);
            }
            
            tfLieuEvent.setText(evenement.getLieuEvent());
            tfDescription.setText(evenement.getDescription());
            tfCapaciteMax.setText(String.valueOf(evenement.getCapaciteMax()));
            tfPointsOfferts.setText(String.valueOf(evenement.getPointsOfferts()));
            
            lblStatus.setText("Modification de: " + evenement.getNomEvent());
        }
    }
    
    @FXML
    public void handleModifierEvenement() {
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
            
            // Parse numeric values
            int capacite = 0;
            int points = 0;
            
            try {
                capacite = Integer.parseInt(tfCapaciteMax.getText());
                if (capacite <= 0) {
                    afficherAlerte("Erreur", "La capacité doit être supérieure à 0", AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                afficherAlerte("Erreur", "Veuillez entrer une capacité valide", AlertType.ERROR);
                return;
            }
            
            try {
                points = Integer.parseInt(tfPointsOfferts.getText());
                if (points < 0) {
                    afficherAlerte("Erreur", "Les points ne peuvent pas être négatifs", AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                afficherAlerte("Erreur", "Veuillez entrer des points valides", AlertType.ERROR);
                return;
            }
            
            // Update event object
            evenementToModify.setNomEvent(tfNomEvent.getText().trim());
            evenementToModify.setDateEvent(java.sql.Date.valueOf(dpDateEvent.getValue()));
            evenementToModify.setLieuEvent(tfLieuEvent.getText().trim());
            evenementToModify.setDescription(tfDescription.getText().trim());
            evenementToModify.setCapaciteMax(capacite);
            evenementToModify.setPointsOfferts(points);
            
            // Update event in database
            evenementService.modifierEvenement(evenementToModify);
            
            // Show success message
            afficherAlerte("Succès", "Événement modifié avec succès!", AlertType.INFORMATION);
            
            lblStatus.setText("✅ Événement modifié avec succès");
            
            // Close window after successful modification
            handleRetour();
            
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors de la modification de l'événement: " + e.getMessage(), AlertType.ERROR);
            lblStatus.setText("❌ Erreur lors de la modification");
        }
    }
    
    @FXML
    public void handleAnnuler() {
        // Reset form to original values
        setEvenementToModify(evenementToModify);
        lblStatus.setText("Formulaire réinitialisé");
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
    
    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
