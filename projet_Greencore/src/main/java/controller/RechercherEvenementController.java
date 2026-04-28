package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Evenement;
import services.EvenementService;
import java.text.SimpleDateFormat;

public class RechercherEvenementController {

    @FXML
    private TextField tfIdRecherche;

    @FXML
    private Label lblResultat;

    @FXML
    private VBox vboxEventInfo;

    @FXML
    private Label lblNomEvent;

    @FXML
    private Label lblDateEvent;

    @FXML
    private Label lblLieuEvent;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblCapaciteMax;

    @FXML
    private Label lblPointsOfferts;

    private final EvenementService evenementService = new EvenementService();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // Initially hide the event info section
        vboxEventInfo.setVisible(false);
        vboxEventInfo.setManaged(false);
    }

    @FXML
    void handleRechercher(ActionEvent event) {
        String idText = tfIdRecherche.getText().trim();
        
        if (idText.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un ID d'événement");
            return;
        }

        try {
            int eventId = Integer.parseInt(idText);
            Evenement evenement = evenementService.rechercherEvenementParId(eventId);
            
            if (evenement != null) {
                afficherEvenement(evenement);
                lblResultat.setText("Événement trouvé avec succès!");
                lblResultat.setStyle("-fx-text-fill: #4caf50;");
            } else {
                cacherEvenement();
                lblResultat.setText("Aucun événement trouvé avec l'ID: " + eventId);
                lblResultat.setStyle("-fx-text-fill: #f44336;");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID doit être un nombre valide");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la recherche: " + e.getMessage());
        }
    }

    @FXML
    void handleEffacer(ActionEvent event) {
        tfIdRecherche.clear();
        cacherEvenement();
        lblResultat.setText("");
    }

    private void afficherEvenement(Evenement evenement) {
        lblNomEvent.setText("Nom: " + evenement.getNomEvent());
        lblDateEvent.setText("Date: " + dateFormat.format(evenement.getDateEvent()));
        lblLieuEvent.setText("Lieu: " + evenement.getLieuEvent());
        lblDescription.setText("Description: " + (evenement.getDescription() != null ? evenement.getDescription() : "N/A"));
        lblCapaciteMax.setText("Capacité Max: " + evenement.getCapaciteMax());
        lblPointsOfferts.setText("Points Offerts: " + evenement.getPointsOfferts());

        vboxEventInfo.setVisible(true);
        vboxEventInfo.setManaged(true);
    }

    private void cacherEvenement() {
        vboxEventInfo.setVisible(false);
        vboxEventInfo.setManaged(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
