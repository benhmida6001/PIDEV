package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherController {
    
    @FXML
    private TextField txtVille;
    
    @FXML
    private Button btnRechercher;
    
    @FXML
    private Button btnActualiser;
    
    @FXML
    private Label labelTemperature;
    
    @FXML
    private Label labelDescription;
    
    @FXML
    private Label labelHumidite;
    
    @FXML
    private Label labelVent;
    
    @FXML
    private Label labelPression;
    
    @FXML
    private Label labelErreur;
    
    @FXML
    private VBox vboxMeteo;
    
    @FXML
    private ImageView iconMeteo;
    
    @FXML
    private Label labelTitre;
    
    @FXML
    public void initialize() {
        // Initialiser l'interface
        labelErreur.setVisible(false);
        vboxMeteo.setVisible(false);
        
        // Message par défaut
        labelTitre.setText("Service Météo Greencore");
        labelTemperature.setText("--°C");
        labelDescription.setText("Entrez une ville pour voir la météo");
        labelHumidite.setText("--%");
        labelVent.setText("-- km/h");
        labelPression.setText("-- hPa");
    }
    
    @FXML
    public void handleRechercher() {
        String ville = txtVille.getText().trim();
        
        if (ville.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez entrer un nom de ville", AlertType.WARNING);
            return;
        }
        
        try {
            // Appeler l'API OpenWeatherMap
            String meteoData = getWeatherData(ville);
            
            if (meteoData.startsWith("Erreur:")) {
                labelErreur.setText(meteoData.substring(7));
                labelErreur.setVisible(true);
                vboxMeteo.setVisible(false);
            } else {
                // Parser les données météo
                afficherMeteo(meteoData, ville);
                labelErreur.setVisible(false);
                vboxMeteo.setVisible(true);
            }
            
        } catch (Exception e) {
            labelErreur.setText("Erreur de connexion: " + e.getMessage());
            labelErreur.setVisible(true);
            vboxMeteo.setVisible(false);
            System.err.println("Erreur météo: " + e.getMessage());
        }
    }
    
    @FXML
    public void handleActualiser() {
        String ville = txtVille.getText().trim();
        if (!ville.isEmpty()) {
            handleRechercher();
        }
    }
    
    private void afficherMeteo(String meteoData, String ville) {
        try {
            // Parser les données JSON simplifiées
            String[] parts = meteoData.split("\\|");
            
            if (parts.length >= 6) {
                double temperature = parseDouble(parts[0]);
                String description = parts[1];
                int humidite = Integer.parseInt(parts[2]);
                double ventVitesse = parseDouble(parts[3]);
                double pression = parseDouble(parts[4]);
                String iconCode = parts[5];
                
                // Mettre à jour les labels
                labelTemperature.setText(String.format("%.1f°C", temperature));
                labelDescription.setText(description);
                labelHumidite.setText(humidite + "%");
                labelVent.setText(ventVitesse + " km/h");
                labelPression.setText(pression + " hPa");
                
                // Mettre à jour l'icône
                setWeatherIcon(iconCode);
                
                // Mettre à jour le titre
                labelTitre.setText("Météo à " + ville);
            }
        } catch (Exception e) {
            System.err.println("Erreur parsing météo: " + e.getMessage());
        }
    }
    
    private void setWeatherIcon(String iconCode) {
        try {
            // Icônes météo simples basées sur le code
            String iconPath = getWeatherIconPath(iconCode);
            
            if (iconPath != null) {
                var inputStream = getClass().getResourceAsStream(iconPath);
                if (inputStream != null) {
                    Image image = new Image(inputStream);
                    iconMeteo.setImage(image);
                } else {
                    System.err.println("Erreur icône météo: Input stream must not be null - icône non trouvée: " + iconPath);
                    iconMeteo.setImage(null);
                }
            } else {
                iconMeteo.setImage(null);
            }
        } catch (Exception e) {
            System.err.println("Erreur icône météo: " + e.getMessage());
        }
    }
    
    private String getWeatherIconPath(String iconCode) {
        // Mapper les codes météo aux icônes
        switch (iconCode) {
            case "01d": return "/images/weather/sunny.png";
            case "01n": return "/images/weather/sunny.png";
            case "02d": return "/images/weather/partly_cloudy.png";
            case "02n": return "/images/weather/partly_cloudy.png";
            case "03d": return "/images/weather/cloudy.png";
            case "03n": return "/images/weather/cloudy.png";
            case "09d": return "/images/weather/rain.png";
            case "09n": return "/images/weather/rain.png";
            case "10d": return "/images/weather/rain_heavy.png";
            case "10n": return "/images/weather/rain_heavy.png";
            case "13d": return "/images/weather/snow.png";
            case "13n": return "/images/weather/snow.png";
            default: return "/images/weather/default.png";
        }
    }
    
    private String getWeatherData(String ville) throws Exception {
        // Simulation d'API météo (remplacer par un vrai appel API)
        // Format: temperature|description|humidite|vent|pression|iconCode
        
        // Simuler différentes réponses selon la ville
        switch (ville.toLowerCase()) {
            case "paris":
                return "15.2|Ensoleillé avec quelques nuages|65|12|1013|01d";
            case "london":
                return "12.5|Nuageux|78|8|1008|02d";
            case "new york":
                return "18.0|Pluvieux|82|15|1005|09d";
            case "tokyo":
                return "22.1|Partiellement nuageux|70|5|1010|02d";
            case "sydney":
                return "25.3|Dégagé|55|10|1012|01d";
            case "marrakech":
                return "28.7|Très chaud|45|5|1008|01d";
            case "montreal":
                return "8.5|Neigeux|88|20|1015|13d";
            default:
                // Pour les autres villes, générer des données aléatoires
                double temp = 10 + Math.random() * 20;
                String[] descriptions = {"Dégagé", "Nuageux", "Ensoleillé", "Pluvieux", "Orageux"};
                String desc = descriptions[(int)(Math.random() * descriptions.length)];
                int humidite = 40 + (int)(Math.random() * 40);
                double vent = 5 + Math.random() * 15;
                double pression = 1000 + Math.random() * 50;
                String[] icons = {"01d", "02d", "03d", "09d", "10d", "13d"};
                String icon = icons[(int)(Math.random() * icons.length)];
                
                return String.format("%.1f|%s|%d|%.1f|%.0f|%s", temp, desc, humidite, vent, pression, icon);
        }
    }
    
    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // Handle comma decimal separator
            return Double.parseDouble(value.replace(',', '.'));
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
