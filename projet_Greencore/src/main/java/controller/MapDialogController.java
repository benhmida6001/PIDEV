package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import services.GoogleMapsService;

/**
 * Contrôleur pour la fenêtre de dialogue de sélection de lieu avec Google Maps
 */
public class MapDialogController {
    
    @FXML private TextField tfAddress;
    @FXML private Button btnSearch;
    @FXML private Button btnUseAddress;
    @FXML private Button btnClose;
    @FXML private WebView webView;
    
    private WebEngine webEngine;
    private String selectedAddress;
    private Stage dialogStage;
    
    // Interface pour communiquer avec le contrôleur parent
    public interface LocationSelectionListener {
        void onLocationSelected(String address);
    }
    
    private LocationSelectionListener listener;
    
    @FXML
    public void initialize() {
        webEngine = webView.getEngine();
        
        // Activer JavaScript
        webEngine.setJavaScriptEnabled(true);
        
        // Charger la page initiale avec une adresse par défaut
        loadMap("Paris, France");
        
        // Configurer les champs
        tfAddress.setText("Paris, France");
    }
    
    /**
     * Définit le stage de la dialogue
     * @param stage Le stage de la fenêtre
     */
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }
    
    /**
     * Définit le listener pour la sélection de lieu
     * @param listener Le listener
     */
    public void setLocationSelectionListener(LocationSelectionListener listener) {
        this.listener = listener;
    }
    
    /**
     * Définit l'adresse initiale à afficher
     * @param address L'adresse initiale
     */
    public void setInitialAddress(String address) {
        if (address != null && !address.trim().isEmpty()) {
            tfAddress.setText(address);
            loadMap(address);
        }
    }
    
    @FXML
    public void handleSearch() {
        String address = tfAddress.getText().trim();
        if (!address.isEmpty()) {
            loadMap(address);
        } else {
            showAlert("Information", "Veuillez entrer une adresse à rechercher.");
        }
    }
    
    @FXML
    public void handleUseAddress() {
        String address = tfAddress.getText().trim();
        if (!address.isEmpty()) {
            selectedAddress = GoogleMapsService.formatAddress(address);
            
            // Notifier le listener
            if (listener != null) {
                listener.onLocationSelected(selectedAddress);
            }
            
            // Fermer la fenêtre
            handleClose();
        } else {
            showAlert("Erreur", "Veuillez entrer une adresse valide.");
        }
    }
    
    @FXML
    public void handleClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
    
    /**
     * Charge la carte avec l'adresse spécifiée
     * @param address L'adresse à afficher
     */
    private void loadMap(String address) {
        try {
            String htmlContent = GoogleMapsService.generateMapHtml(address);
            webEngine.loadContent(htmlContent);
            
            // Ajouter un listener pour récupérer les résultats de JavaScript
            webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                    // La carte est chargée avec succès
                    System.out.println("Carte Google Maps chargée pour: " + address);
                }
            });
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de la carte: " + e.getMessage());
            showAlert("Erreur", "Impossible de charger la carte. Vérifiez votre connexion internet.");
        }
    }
    
    /**
     * Affiche une alerte simple
     * @param title Le titre
     * @param message Le message
     */
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Retourne l'adresse sélectionnée
     * @return L'adresse sélectionnée ou null si aucune sélection
     */
    public String getSelectedAddress() {
        return selectedAddress;
    }
}
