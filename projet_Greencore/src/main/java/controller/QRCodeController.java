package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class QRCodeController {
    
    @FXML
    private ComboBox<String> cbQRType;
    
    @FXML
    private ComboBox<String> cbEntity;
    
    @FXML
    private TextField tfId;
    
    @FXML
    private Button btnGenerate;
    
    @FXML
    private VBox vboxQRDisplay;
    
    @FXML
    private Label lblQRCodeData;
    
    @FXML
    private Label lblQRCodeImage;
    
    @FXML
    private Button btnDownload;
    
    @FXML
    private Label lblStatus;
    
    private Map<String, String[]> entityData;
    private String currentQRCodeData;
    private String currentFileName;
    
    @FXML
    public void initialize() {
        // Initialiser les données des entités
        entityData = new HashMap<>();
        entityData.put("Utilisateur", new String[]{
            "Jean Dupont - ID: 001",
            "Marie Martin - ID: 002", 
            "Pierre Bernard - ID: 003",
            "Sophie Petit - ID: 004",
            "Lucas Dubois - ID: 005"
        });
        
        entityData.put("Événement", new String[]{
            "Conférence GreenTech 2024 - ID: EVT001",
            "Atelier Recyclage - ID: EVT002",
            "Forum Développement Durable - ID: EVT003",
            "Journée Portes Ouvertes - ID: EVT004",
            "Formation Écologie - ID: EVT005"
        });
        
        entityData.put("Produit", new String[]{
            "Sac Écologique - ID: PRD001",
            "Gourde Réutilisable - ID: PRD002",
            "Kit Jardinage Urbain - ID: PRD003",
            "Panneau Solaire Portable - ID: PRD004",
            "Composteur Domestique - ID: PRD005"
        });
        
        entityData.put("Ticket", new String[]{
            "Ticket Accès Journée - ID: TKT001",
            "Ticket VIP - ID: TKT002",
            "Ticket Étudiant - ID: TKT003",
            "Ticket Famille - ID: TKT004",
            "Ticket Sponsor - ID: TKT005"
        });
        
        // Configurer les ComboBox
        cbQRType.getItems().addAll(
            "URL",
            "Texte",
            "Email",
            "Téléphone",
            "WiFi",
            "Carte de visite",
            "Localisation"
        );
        
        cbEntity.getItems().addAll(
            "Utilisateur",
            "Événement", 
            "Produit",
            "Ticket"
        );
        
        // Ajouter les listeners
        cbQRType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateEntityOptions());
        cbEntity.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateEntityList());
        
        // Masquer les éléments initialement
        vboxQRDisplay.setVisible(false);
        btnDownload.setVisible(false);
        lblStatus.setText("");
    }
    
    @FXML
    public void handleGenerateQRCode() {
        String qrType = cbQRType.getValue();
        String entity = cbEntity.getValue();
        String id = tfId.getText().trim();
        
        if (qrType == null || entity == null || id.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez remplir tous les champs", AlertType.WARNING);
            return;
        }
        
        try {
            // Générer les données du QR Code
            currentQRCodeData = generateQRCodeData(qrType, entity, id);
            currentFileName = generateFileName(entity, id);
            
            // Afficher les données du QR Code
            lblQRCodeData.setText("Données du QR Code:\n" + currentQRCodeData);
            
            // Simuler la génération de l'image du QR Code
            String qrImagePath = generateQRCodeImage(currentQRCodeData);
            lblQRCodeImage.setText("Image du QR Code:\n" + qrImagePath);
            
            // Afficher la zone de résultat
            vboxQRDisplay.setVisible(true);
            btnDownload.setVisible(true);
            
            lblStatus.setText("✅ QR Code généré avec succès");
            lblStatus.setStyle("-fx-text-fill: white;");
            
        } catch (Exception e) {
            lblStatus.setText("❌ Erreur lors de la génération: " + e.getMessage());
            lblStatus.setStyle("-fx-text-fill: #ff6b6b;");
        }
    }
    
    @FXML
    public void handleDownloadQRCode() {
        if (currentQRCodeData == null) {
            afficherAlerte("Erreur", "Veuillez d'abord générer un QR Code", AlertType.WARNING);
            return;
        }
        
        try {
            // Simuler le téléchargement
            String downloadPath = simulateDownload(currentQRCodeData, currentFileName);
            
            afficherAlerte("Succès", "QR Code téléchargé avec succès!\nChemin: " + downloadPath, AlertType.INFORMATION);
            
            lblStatus.setText("✅ QR Code téléchargé: " + currentFileName);
            
        } catch (Exception e) {
            afficherAlerte("Erreur", "Échec du téléchargement: " + e.getMessage(), AlertType.ERROR);
            lblStatus.setText("❌ Erreur de téléchargement");
        }
    }
    
    private void updateEntityOptions() {
        String qrType = cbQRType.getValue();
        
        if (qrType != null) {
            cbEntity.setDisable(false);
            cbEntity.getSelectionModel().clearSelection();
            cbEntity.getItems().clear();
            cbEntity.getItems().addAll("Utilisateur", "Événement", "Produit", "Ticket");
        }
    }
    
    private void updateEntityList() {
        String entity = cbEntity.getValue();
        
        if (entity != null && entityData.containsKey(entity)) {
            tfId.setPromptText("Entrez ID ou sélectionnez: " + entityData.get(entity)[0]);
        }
    }
    
    private String generateQRCodeData(String qrType, String entity, String id) {
        String baseData = "";
        
        // Déterminer les données de base selon l'entité
        switch (entity) {
            case "Utilisateur":
                baseData = "USER:" + id + "|NAME:" + getUserName(id) + "|EMAIL:" + getUserEmail(id);
                break;
            case "Événement":
                baseData = "EVENT:" + id + "|NAME:" + getEventName(id) + "|DATE:" + getCurrentDate();
                break;
            case "Produit":
                baseData = "PRODUCT:" + id + "|NAME:" + getProductName(id) + "|PRICE:" + getProductPrice(id);
                break;
            case "Ticket":
                baseData = "TICKET:" + id + "|TYPE:" + getTicketType(id) + "|VALID:" + getCurrentDate();
                break;
        }
        
        // Ajouter le préfixe selon le type de QR Code
        switch (qrType) {
            case "URL":
                return "https://greencore.com/verify/" + baseData;
            case "Email":
                return "mailto:contact@greencore.com?subject=Vérification&body=" + baseData;
            case "Téléphone":
                return "tel:+33612345678";
            case "WiFi":
                return "WIFI:T:WPA;S:GreenCore_Guest;P:ecologie2024;;";
            case "Carte de visite":
                return "BEGIN:VCARD\nVERSION:3.0\nFN:GreenCore\nORG:Écologie\nTEL:+33612345678\nEMAIL:contact@greencore.com\nEND:VCARD";
            case "Localisation":
                return "geo:48.8566,2.3522"; // Coordonnées Paris
            default: // Texte
                return baseData;
        }
    }
    
    private String generateQRCodeImage(String data) {
        // Simuler la génération d'image QR Code
        return "[QR Code Image]\nTaille: 200x200 pixels\nFormat: PNG\nEncodage: UTF-8\n\n" +
               "█████████████████████████████████\n" +
               "████ ▄▄▄▄▄ █ ▀▀▄▀ ▄▄▄▄▄ █ ████\n" +
               "████ █ █ █ █▀▄█ ▀ █ █ █ █ ████\n" +
               "████ ▄▄▄▄▄ █▄▄▀▄▄ ▄▄▄▄▄ █ ████\n" +
               "████ ▄▄▄ ▄▄ ▀ ▄▄▀▄▀▄▄▄▄▀ █ ████\n" +
               "████ ▄▄▄▄▄ █▄▀██▄▄▀▀▄▄▄▄▄ █ ████\n" +
               "████ ▄▄▄▄▄ ▄▄▄▄▄▄▄▄▄▄▄▄▄▄ █ ████\n" +
               "████ ▄▄▄▄▄ █ ▀▄▀▄▄▄▀▄▀▄▄▄▄ █ ████\n" +
               "████ ▄▄▄▄▄ █▄▄▄▄▄▄▄▄▄▄▄▄▄▄ █ ████\n" +
               "█████████████████████████████████";
    }
    
    private String simulateDownload(String data, String fileName) {
        // Simuler le téléchargement en créant un fichier
        String downloadsPath = System.getProperty("user.home") + "/Downloads";
        String fullPath = downloadsPath + "/" + fileName + ".png";
        
        // Simuler la création du fichier
        File qrFile = new File(fullPath);
        try {
            // Simuler l'écriture du fichier
            qrFile.createNewFile();
            return fullPath;
        } catch (Exception e) {
            return "Simulation: " + fullPath;
        }
    }
    
    private String generateFileName(String entity, String id) {
        return "QRCode_" + entity.toUpperCase() + "_" + id + "_" + System.currentTimeMillis();
    }
    
    // Méthodes utilitaires pour obtenir les données
    private String getUserName(String id) {
        switch (id) {
            case "001": return "Jean Dupont";
            case "002": return "Marie Martin";
            case "003": return "Pierre Bernard";
            case "004": return "Sophie Petit";
            case "005": return "Lucas Dubois";
            default: return "Utilisateur " + id;
        }
    }
    
    private String getUserEmail(String id) {
        return "user" + id + "@greencore.com";
    }
    
    private String getEventName(String id) {
        switch (id) {
            case "EVT001": return "Conférence GreenTech 2024";
            case "EVT002": return "Atelier Recyclage";
            case "EVT003": return "Forum Développement Durable";
            default: return "Événement " + id;
        }
    }
    
    private String getProductName(String id) {
        switch (id) {
            case "PRD001": return "Sac Écologique";
            case "PRD002": return "Gourde Réutilisable";
            default: return "Produit " + id;
        }
    }
    
    private String getProductPrice(String id) {
        switch (id) {
            case "PRD001": return "15.99€";
            case "PRD002": return "12.50€";
            default: return "10.00€";
        }
    }
    
    private String getTicketType(String id) {
        switch (id) {
            case "TKT001": return "Accès Journée";
            case "TKT002": return "VIP";
            default: return "Standard";
        }
    }
    
    private String getCurrentDate() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
