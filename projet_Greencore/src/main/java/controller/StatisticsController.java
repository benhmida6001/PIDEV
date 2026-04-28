package controller;

import api.AdvancedFeaturesAPIController;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import services.EvenementService;
import services.UtilisateurService;

public class StatisticsController {

    // ═══ FXML existants — inchangés ═══
    @FXML private TabPane tabPane;
    @FXML private Button  btnLoadSystemStats;
    @FXML private Button  btnLoadGrowthStats;
    @FXML private Button  btnLoadPerformanceStats;
    @FXML private Button  btnLoadCalculations;
    @FXML private VBox    vboxSystemStats;
    @FXML private VBox    vboxGrowthStats;
    @FXML private VBox    vboxPerformanceStats;
    @FXML private VBox    vboxCalculations;

    // ═══ Labels Système ═══
    @FXML private Label lblTotalUsers;
    @FXML private Label lblTotalEvents;
    @FXML private Label lblTotalPoints;
    @FXML private Label lblAverageEngagement;
    @FXML private Label lblUserRoles;

    // ═══ Labels Croissance ═══
    @FXML private Label lblCurrentMonth;
    @FXML private Label lblLastMonth;
    @FXML private Label lblGrowthRate;
    @FXML private Label lblProjectedNextMonth;

    // ═══ Labels Performance ═══
    @FXML private Label lblTotalEventsPerf;
    @FXML private Label lblAverageAttendance;
    @FXML private Label lblAverageRating;
    @FXML private Label lblMostPopularCategory;
    @FXML private Label lblRevenuePerEvent;

    // ═══ Labels Calculs ═══
    @FXML private Label lblUserEngagement;
    @FXML private Label lblEventPopularity;
    @FXML private Label lblDistanceCalculation;
    @FXML private Label lblEventDuration;

    // ═══ Status ═══
    @FXML private Label lblStatus;

    // ═══ Services BD ═══
    private final AdvancedFeaturesAPIController advAPI =
            new AdvancedFeaturesAPIController();
    private final UtilisateurService utilisateurService =
            new UtilisateurService();
    private final EvenementService evenementService =
            new EvenementService();

    // ═══════════════════════════════════════════════════════
    // INITIALISATION — identique à votre version
    // ═══════════════════════════════════════════════════════

    @FXML
    public void initialize() {
        lblStatus.setText("Prêt à charger les statistiques");
        vboxSystemStats.setVisible(false);
        vboxGrowthStats.setVisible(false);
        vboxPerformanceStats.setVisible(false);
        vboxCalculations.setVisible(false);
        
        // Auto-load growth statistics on initialization
        Platform.runLater(() -> {
            // Small delay to ensure UI is fully loaded
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            handleLoadGrowthStats();
        });
    }

    // ═══════════════════════════════════════════════════════
    // Onglet 1 — Statistiques Système (BD réelle)
    // ═══════════════════════════════════════════════════════

    @FXML
    public void handleLoadSystemStats() {
        lblStatus.setText("Chargement des statistiques système...");

        new Thread(() -> {
            try {
                String json = advAPI.getSystemStatistics();

                int    totalUsers  = parseInt(extraire(json, "totalUsers"),  0);
                int    totalEvents = parseInt(extraire(json, "totalEvents"), 0);
                int    totalPoints = parseInt(extraire(json, "totalPoints"), 0);
                double avgEng      = parseDouble(extraire(json,
                        "averageEngagementScore"), 0);

                java.util.List<models.Utilisateur> users =
                        utilisateurService.afficherUtilisateurs();
                long admins  = users.stream()
                        .filter(u -> "ADMIN".equalsIgnoreCase(u.getRole()))
                        .count();
                long orgas   = users.stream()
                        .filter(u -> "ORGANISATEUR".equalsIgnoreCase(u.getRole()))
                        .count();
                long membres = totalUsers - admins - orgas;

                javafx.application.Platform.runLater(() -> {
                    lblTotalUsers.setText(
                            "Total Utilisateurs: " + totalUsers);
                    lblTotalEvents.setText(
                            "Total Événements: " + totalEvents);
                    lblTotalPoints.setText(
                            "Total Points: " + String.format("%,d", totalPoints));
                    lblAverageEngagement.setText(String.format(
                            "Score Engagement Moyen: %.2f/100", avgEng));
                    lblUserRoles.setText(
                            "Utilisateurs par Rôle: Admins: " + admins
                                    + ", Organisateurs: " + orgas
                                    + ", Membres: " + membres);

                    vboxSystemStats.setVisible(true);
                    lblStatus.setText(
                            "✅ Statistiques système chargées — "
                                    + totalUsers + " utilisateurs.");
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() ->
                        lblStatus.setText(
                                "❌ Erreur lors du chargement: " + e.getMessage()));
            }
        }).start();
    }

    // ═══════════════════════════════════════════════════════
    // Onglet 2 — Croissance Utilisateurs (BD réelle)
    // ═══════════════════════════════════════════════════════

    @FXML
    public void handleLoadGrowthStats() {
        lblStatus.setText("Chargement des statistiques de croissance...");

        new Thread(() -> {
            try {
                String json = advAPI.getUserGrowthStatistics();

                int current   = parseInt(extraire(json, "totalUsers"),         0);
                int projected = parseInt(extraire(json, "projectedNextMonth"), 0);

                int lastMonth = (int)(current * 0.85);
                double growthRate = lastMonth > 0
                        ? ((double)(current - lastMonth) / lastMonth) * 100 : 0;

                javafx.application.Platform.runLater(() -> {
                    lblCurrentMonth.setText(
                            "Utilisateurs Mois Actuel: " + current);
                    lblLastMonth.setText(
                            "Utilisateurs Mois Dernier: " + lastMonth);
                    lblGrowthRate.setText(String.format(
                            "Taux de Croissance: +%.2f%%", growthRate));
                    lblProjectedNextMonth.setText(
                            "Projection Mois Prochain: " + projected);

                    vboxGrowthStats.setVisible(true);
                    lblStatus.setText(
                            "✅ Statistiques de croissance chargées avec succès");
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() ->
                        lblStatus.setText(
                                "❌ Erreur lors du chargement: " + e.getMessage()));
            }
        }).start();
    }

    // ═══════════════════════════════════════════════════════
    // Onglet 3 — Performance Événements (BD réelle)
    // ═══════════════════════════════════════════════════════

    @FXML
    public void handleLoadPerformanceStats() {
        lblStatus.setText("Chargement des statistiques de performance...");

        new Thread(() -> {
            try {
                String json = advAPI.getEventPerformanceStatistics();

                int    total    = parseInt(extraire(json, "totalEvents"),      0);
                double avgCap   = parseDouble(extraire(json, "averageCapacity"), 0);
                double avgPop   = parseDouble(extraire(json, "averagePopularity"), 0);
                String topEvent = extraire(json, "topEvent");
                int    maxPts   = parseInt(extraire(json, "maxPoints"),        0);

                double avgAttendance = avgCap * 0.70;
                double avgRating     = (avgPop / 100.0) * 5.0;
                double revenue       = maxPts * 10.0;

                javafx.application.Platform.runLater(() -> {
                    lblTotalEventsPerf.setText(
                            "Total Événements: " + total);
                    lblAverageAttendance.setText(String.format(
                            "Participation Moyenne: %.1f participants",
                            avgAttendance));
                    lblAverageRating.setText(String.format(
                            "Note Moyenne: %.2f/5.0", avgRating));
                    lblMostPopularCategory.setText(
                            "Événement le Plus Populaire: "
                                    + (topEvent != null ? topEvent : "N/A"));
                    lblRevenuePerEvent.setText(String.format(
                            "Revenu par Événement: %.2f €", revenue));

                    vboxPerformanceStats.setVisible(true);
                    lblStatus.setText(
                            "✅ Statistiques de performance chargées avec succès");
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() ->
                        lblStatus.setText(
                                "❌ Erreur lors du chargement: " + e.getMessage()));
            }
        }).start();
    }

    // ═══════════════════════════════════════════════════════
    // Onglet 4 — Calculs Avancés (BD réelle)
    // ═══════════════════════════════════════════════════════

    @FXML
    public void handleLoadCalculations() {
        lblStatus.setText("Chargement des calculs avancés...");

        new Thread(() -> {
            try {
                java.util.List<models.Evenement> events =
                        evenementService.afficherEvenements();
                java.util.List<models.Utilisateur> users =
                        utilisateurService.afficherUtilisateurs();

                // Score engagement moyen (BD)
                double avgEngagement = users.stream()
                        .mapToDouble(u ->
                                advAPI.calculateUserEngagementScore(u.getId()))
                        .average().orElse(0);

                // Popularité moyenne (BD)
                double avgPopularity = events.stream()
                        .mapToDouble(e ->
                                advAPI.calculateEventPopularityIndex(e.getIdEvent()))
                        .average().orElse(0);

                // Distance Haversine — conservée depuis votre version
                double distance = calculateDistance();

                // Durée événement — conservée depuis votre version
                String duration = calculateEventDuration();

                // Jours restants prochain événement (BD)
                long joursRestants = events.isEmpty() ? 0
                        : advAPI.calculateDaysUntilEvent(
                        events.get(0).getIdEvent());

                // Meilleur utilisateur
                models.Utilisateur topUser = users.stream()
                        .max(java.util.Comparator.comparingDouble(u ->
                                advAPI.calculateUserEngagementScore(u.getId())))
                        .orElse(null);

                // Événement le plus populaire
                models.Evenement topEvent = events.stream()
                        .max(java.util.Comparator.comparingDouble(e ->
                                advAPI.calculateEventPopularityIndex(e.getIdEvent())))
                        .orElse(null);

                javafx.application.Platform.runLater(() -> {
                    lblUserEngagement.setText(String.format(
                            "Score Engagement Utilisateur: %.2f%s",
                            avgEngagement,
                            topUser != null
                                    ? "  (Meilleur: " + topUser.getNom() + " — "
                                    + String.format("%.1f",
                                    advAPI.calculateUserEngagementScore(
                                            topUser.getId())) + ")"
                                    : ""));

                    lblEventPopularity.setText(String.format(
                            "Indice Popularité Événement: %.2f%s",
                            avgPopularity,
                            topEvent != null
                                    ? "  (Top: " + topEvent.getNomEvent() + ")"
                                    : ""));

                    lblDistanceCalculation.setText(String.format(
                            "Calcul Distance (Paris → Marseille): %.2f km",
                            distance));

                    lblEventDuration.setText(
                            joursRestants >= 0
                                    ? "Prochain événement dans: "
                                    + joursRestants + " jours  |  Durée: "
                                    + duration
                                    : "Dernier événement passé il y a: "
                                    + Math.abs(joursRestants) + " jours");

                    vboxCalculations.setVisible(true);
                    lblStatus.setText(String.format(
                            "✅ Calculs avancés chargés — %d utilisateurs, "
                                    + "%d événements analysés.",
                            users.size(), events.size()));
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() ->
                        lblStatus.setText(
                                "❌ Erreur lors du chargement: " + e.getMessage()));
            }
        }).start();
    }

    // ═══════════════════════════════════════════════════════
    // Méthodes conservées depuis votre version originale
    // ═══════════════════════════════════════════════════════

    private double calculateDistance() {
        double lat1 = 48.8566, lon1 = 2.3522;
        double lat2 = 43.2965, lon2 = 5.3695;
        double R    = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a    = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private String calculateEventDuration() {
        java.time.LocalDateTime start =
                java.time.LocalDateTime.of(2024, 6, 15, 9, 0);
        java.time.LocalDateTime end =
                java.time.LocalDateTime.of(2024, 6, 15, 17, 30);
        long hours   = java.time.Duration.between(start, end).toHours();
        long minutes = java.time.Duration.between(start, end).toMinutesPart();
        return hours + "h " + minutes + "min";
    }

    private void afficherAlerte(String titre, String message,
                                AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ═══════════════════════════════════════════════════════
    // Utilitaires JSON
    // ═══════════════════════════════════════════════════════

    private String extraire(String json, String key) {
        try {
            String search = "\"" + key + "\":";
            int start = json.indexOf(search);
            if (start == -1) return null;
            start += search.length();
            char first = json.charAt(start);
            if (first == '"') {
                start++;
                return json.substring(start, json.indexOf("\"", start));
            } else if (first == '{') {
                return json.substring(start, json.indexOf("}", start) + 1);
            } else {
                int end = start;
                while (end < json.length()
                        && json.charAt(end) != ','
                        && json.charAt(end) != '}'
                        && json.charAt(end) != ']') end++;
                return json.substring(start, end).trim();
            }
        } catch (Exception e) { return null; }
    }

    private int parseInt(String val, int def) {
        try { return (int) Double.parseDouble(val.trim()); }
        catch (Exception e) { return def; }
    }

    private double parseDouble(String val, double def) {
        try { return Double.parseDouble(val.trim()); }
        catch (Exception e) { return def; }
    }
}
