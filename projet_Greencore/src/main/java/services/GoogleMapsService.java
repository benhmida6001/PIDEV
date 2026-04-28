package services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Service pour l'intégration avec Google Maps API
 * Fournit des fonctionnalités pour afficher des cartes et géocoder des adresses
 */
public class GoogleMapsService {
    
    private static final String GOOGLE_MAPS_API_KEY = "YOUR_API_KEY_HERE";
    private static final String GOOGLE_MAPS_BASE_URL = "https://www.google.com/maps/embed/v1/place";
    private static final String GOOGLE_MAPS_SEARCH_URL = "https://www.google.com/maps/search/";
    
    /**
     * Génère l'URL d'intégration Google Maps pour une adresse spécifique
     * @param address L'adresse à afficher
     * @return L'URL d'intégration Google Maps
     */
    public static String getMapEmbedUrl(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            return GOOGLE_MAPS_BASE_URL + "?key=" + GOOGLE_MAPS_API_KEY + "&q=" + encodedAddress + "&zoom=16&maptype=roadmap";
        } catch (UnsupportedEncodingException e) {
            System.err.println("Erreur lors de l'encodage de l'adresse: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Génère l'URL de recherche Google Maps pour une adresse
     * Utilisée comme alternative si l'API key n'est pas configurée
     * @param address L'adresse à rechercher
     * @return L'URL de recherche Google Maps
     */
    public static String getMapSearchUrl(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            return GOOGLE_MAPS_SEARCH_URL + encodedAddress;
        } catch (UnsupportedEncodingException e) {
            System.err.println("Erreur lors de l'encodage de l'adresse: " + e.getMessage());
            return GOOGLE_MAPS_SEARCH_URL;
        }
    }
    
    /**
     * Génère le code HTML pour afficher une carte Google Maps dans un WebView
     * @param address L'adresse à afficher
     * @return Le code HTML complet
     */
    public static String generateMapHtml(String address) {
        String mapUrl = getMapEmbedUrl(address);
        
        // Si l'API key n'est pas configurée, utiliser la recherche Google Maps
        if (mapUrl.contains("YOUR_API_KEY_HERE")) {
            return generateSearchMapHtml(address);
        }
        
        return "<!DOCTYPE html>\n" +
               "<html>\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>Google Maps</title>\n" +
               "    <style>\n" +
               "        body { margin: 0; padding: 0; font-family: Arial, sans-serif; }\n" +
               "        .container { width: 100%; height: 100vh; display: flex; flex-direction: column; }\n" +
               "        .header { padding: 10px; background-color: #4caf50; color: white; text-align: center; }\n" +
               "        .map-container { flex: 1; position: relative; }\n" +
               "        .address-bar { position: absolute; top: 10px; left: 10px; right: 10px; z-index: 1000; }\n" +
               "        .address-input { width: 70%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; }\n" +
               "        .search-btn { width: 25%; padding: 8px; background-color: #2196f3; color: white; border: none; border-radius: 4px; cursor: pointer; }\n" +
               "        iframe { width: 100%; height: 100%; border: none; }\n" +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class=\"container\">\n" +
               "        <div class=\"header\">\n" +
               "            <h2>📍 Sélectionner un lieu</h2>\n" +
               "        </div>\n" +
               "        <div class=\"map-container\">\n" +
               "            <div class=\"address-bar\">\n" +
               "                <input type=\"text\" id=\"addressInput\" class=\"address-input\" value=\"" + escapeHtml(address) + "\" placeholder=\"Entrez une adresse...\">\n" +
               "                <button class=\"search-btn\" onclick=\"searchLocation()\">Rechercher</button>\n" +
               "            </div>\n" +
               "            <iframe id=\"mapFrame\" src=\"" + mapUrl + "\" allowfullscreen></iframe>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "    <script>\n" +
               "        function searchLocation() {\n" +
               "            const address = document.getElementById('addressInput').value;\n" +
               "            if (address.trim()) {\n" +
               "                const encodedAddress = encodeURIComponent(address);\n" +
               "                const newUrl = '" + GOOGLE_MAPS_BASE_URL + "?key=" + GOOGLE_MAPS_API_KEY + "&q=' + encodedAddress + '&zoom=16&maptype=roadmap';\n" +
               "                document.getElementById('mapFrame').src = newUrl;\n" +
               "            }\n" +
               "        }\n" +
               "        \n" +
               "        // Communication avec JavaFX\n" +
               "        function selectLocation() {\n" +
               "            const address = document.getElementById('addressInput').value;\n" +
               "            if (address.trim()) {\n" +
               "                // Envoyer l'adresse sélectionnée à JavaFX\n" +
               "                if (window.javaInterface) {\n" +
               "                    window.javaInterface.onLocationSelected(address);\n" +
               "                }\n" +
               "            }\n" +
               "        }\n" +
               "        \n" +
               "        // Permettre à l'application JavaFX de récupérer l'adresse\n" +
               "        function getCurrentAddress() {\n" +
               "            return document.getElementById('addressInput').value;\n" +
               "        }\n" +
               "    </script>\n" +
               "</body>\n" +
               "</html>";
    }
    
    /**
     * Génère le code HTML pour une carte de recherche alternative (sans API key)
     * @param address L'adresse à rechercher
     * @return Le code HTML complet
     */
    private static String generateSearchMapHtml(String address) {
        String searchUrl = getMapSearchUrl(address);
        
        return "<!DOCTYPE html>\n" +
               "<html>\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>Google Maps</title>\n" +
               "    <style>\n" +
               "        body { margin: 0; padding: 0; font-family: Arial, sans-serif; }\n" +
               "        .container { width: 100%; height: 100vh; display: flex; flex-direction: column; }\n" +
               "        .header { padding: 10px; background-color: #4caf50; color: white; text-align: center; }\n" +
               "        .search-container { padding: 15px; background-color: #f5f5f5; border-bottom: 1px solid #ddd; }\n" +
               "        .search-input { width: 70%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; font-size: 14px; }\n" +
               "        .search-btn { width: 25%; padding: 10px; background-color: #2196f3; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }\n" +
               "        .webview-container { flex: 1; }\n" +
               "        iframe { width: 100%; height: 100%; border: none; }\n" +
               "        .instructions { padding: 10px; background-color: #e3f2fd; text-align: center; color: #1976d2; }\n" +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class=\"container\">\n" +
               "        <div class=\"header\">\n" +
               "            <h2>📍 Recherche de lieu</h2>\n" +
               "        </div>\n" +
               "        <div class=\"instructions\">\n" +
               "            <p>💡 Utilisez la recherche ci-dessous pour trouver une adresse, puis copiez-la dans le champ de lieu.</p>\n" +
               "        </div>\n" +
               "        <div class=\"search-container\">\n" +
               "            <input type=\"text\" id=\"addressInput\" class=\"search-input\" value=\"" + escapeHtml(address) + "\" placeholder=\"Entrez une adresse...\">\n" +
               "            <button class=\"search-btn\" onclick=\"searchLocation()\">Rechercher sur Google Maps</button>\n" +
               "        </div>\n" +
               "        <div class=\"webview-container\">\n" +
               "            <iframe id=\"mapFrame\" src=\"" + searchUrl + "\" allowfullscreen></iframe>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "    <script>\n" +
               "        function searchLocation() {\n" +
               "            const address = document.getElementById('addressInput').value;\n" +
               "            if (address.trim()) {\n" +
               "                const encodedAddress = encodeURIComponent(address);\n" +
               "                const newUrl = 'https://www.google.com/maps/search/' + encodedAddress;\n" +
               "                document.getElementById('mapFrame').src = newUrl;\n" +
               "            }\n" +
               "        }\n" +
               "        \n" +
               "        function getCurrentAddress() {\n" +
               "            return document.getElementById('addressInput').value;\n" +
               "        }\n" +
               "    </script>\n" +
               "</body>\n" +
               "</html>";
    }
    
    /**
     * Échappe les caractères HTML pour éviter les injections
     * @param text Le texte à échapper
     * @return Le texte échappé
     */
    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    /**
     * Vérifie si l'API key est configurée
     * @return true si l'API key est configurée, false sinon
     */
    public static boolean isApiKeyConfigured() {
        return !GOOGLE_MAPS_API_KEY.equals("YOUR_API_KEY_HERE");
    }
    
    /**
     * Formate une adresse pour l'affichage
     * @param address L'adresse à formater
     * @return L'adresse formatée
     */
    public static String formatAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return "";
        }
        
        // Supprimer les espaces multiples et normaliser
        return address.trim().replaceAll("\\s+", " ");
    }
}
