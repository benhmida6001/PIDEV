package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import services.EvenementService;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapController implements Initializable {
    
    @FXML
    private WebView webView;
    
    @FXML
    private Label infoLabel;
    
    private WebEngine webEngine;
    private EvenementService evenementService;
    private final StringProperty javaScriptReady = new SimpleStringProperty("false");
    
    private static final Logger LOGGER = Logger.getLogger(MapController.class.getName());
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            evenementService = new EvenementService();
            webEngine = webView.getEngine();
            
            // Load the HTML file with Google Maps
            URL mapUrl = getClass().getResource("/views/map.html");
            if (mapUrl == null) {
                LOGGER.severe("Could not find map.html file");
                return;
            }
            
            webEngine.load(mapUrl.toExternalForm());
            
            // Set up JavaScript bridge
            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    // Make Java methods available to JavaScript
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("javaApp", this);
                    javaScriptReady.set("true");
                    
                    // Load events once JavaScript is ready
                    loadEventsOnMap();
                }
            });
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing map controller", e);
        }
    }
    
    /**
     * Load all events and display them on the map
     */
    public void loadEventsOnMap() {
        try {
            var events = evenementService.afficherEvenements();
            
            for (var event : events) {
                if (event.getLieuEvent() != null && !event.getLieuEvent().trim().isEmpty()) {
                    addEventMarker(event.getIdEvent(), 
                               event.getNomEvent(), 
                               event.getLieuEvent(), 
                               event.getDescription());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading events on map", e);
        }
    }
    
    /**
     * Called from JavaScript to add a marker to the map
     */
    public void addEventMarker(int id, String title, String location, String description) {
        try {
            // Geocode the location to get coordinates
            String script = String.format(
                "geocodeAndAddMarker('%d', '%s', '%s', '%s');", 
                id, escapeJavaScript(title), escapeJavaScript(location), escapeJavaScript(description)
            );
            webEngine.executeScript(script);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error adding marker for event: " + title, e);
        }
    }
    
    /**
     * Called from JavaScript when a marker is clicked
     */
    public void onMarkerClicked(int eventId) {
        LOGGER.info("Marker clicked for event ID: " + eventId);
        // You can open event details dialog here
        // showEventDetails(eventId);
    }
    
    /**
     * Center map on specific location
     */
    public void centerMapOnLocation(String location) {
        try {
            String script = String.format(
                "centerMapOnLocation('%s');", 
                escapeJavaScript(location)
            );
            webEngine.executeScript(script);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error centering map on location: " + location, e);
        }
    }
    
    /**
     * Refresh the map with current events
     */
    public void refreshMap() {
        if ("true".equals(javaScriptReady.get())) {
            // Clear existing markers
            webEngine.executeScript("clearMarkers();");
            // Reload events
            loadEventsOnMap();
        }
    }
    
    /**
     * Escape JavaScript strings to prevent injection
     */
    private String escapeJavaScript(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                  .replace("'", "\\'")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * Refresh the map with current events
     */
    @FXML
    public void refreshMap(ActionEvent event) {
        infoLabel.setText("Actualisation de la carte...");
        refreshMap();
        infoLabel.setText("Carte actualisée avec succès!");
    }
    
    /**
     * Center map on Tunis
     */
    @FXML
    public void centerOnTunis(ActionEvent event) {
        centerMapOnLocation("Tunis, Tunisie");
        infoLabel.setText("Carte centrée sur Tunis");
    }
    
    /**
     * Get JavaScript ready status
     */
    public String getJavaScriptReady() {
        return javaScriptReady.get();
    }
}
