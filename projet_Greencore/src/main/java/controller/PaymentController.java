package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PaymentController {
    
    @FXML
    private ComboBox<String> comboUtilisateur;
    
    @FXML
    private ComboBox<String> comboEvenement;
    
    @FXML
    private TextField txtMontant;
    
    @FXML
    private Label labelResumeUser;
    
    @FXML
    private Label labelResumeEvent;
    
    @FXML
    private Label labelResumeMontant;
    
    @FXML
    private Label labelResumePoints;
    
    @FXML
    private Label labelResultatPaiement;
    
    @FXML
    private TextField txtTransactionId;
    
    @FXML
    private ComboBox<String> comboUtilisateurHistorique;
    
    @FXML
    private TableView<Transaction> tableHistorique;
    
    @FXML
    private TableColumn<Transaction, String> colTransaction;
    
    @FXML
    private TableColumn<Transaction, Double> colMontant;
    
    @FXML
    private TableColumn<Transaction, String> colDevise;
    
    @FXML
    private TableColumn<Transaction, String> colDate;
    
    @FXML
    private TableColumn<Transaction, String> colStatut;
    
    @FXML
    private TableColumn<Transaction, String> colActions;
    
    @FXML
    private Label labelTotalTx;
    
    @FXML
    private Label labelTotalMontant;
    
    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private Random random = new Random();
    
    @FXML
    public void initialize() {
        // Initialiser les combobox avec des données de test
        comboUtilisateur.getItems().addAll(
            "Jean Dupont - jean.dupont@email.com",
            "Marie Martin - marie.martin@email.com",
            "Pierre Bernard - pierre.bernard@email.com",
            "Sophie Petit - sophie.petit@email.com",
            "Lucas Dubois - lucas.dubois@email.com"
        );
        
        comboEvenement.getItems().addAll(
            "Conférence GreenTech 2024 - 50.00€",
            "Atelier Recyclage - 25.00€",
            "Forum Développement Durable - 75.00€",
            "Journée Portes Ouvertes - Gratuit",
            "Formation Écologie - 100.00€"
        );
        
        comboUtilisateurHistorique.getItems().addAll(
            "Tous les utilisateurs",
            "Jean Dupont",
            "Marie Martin",
            "Pierre Bernard",
            "Sophie Petit",
            "Lucas Dubois"
        );
        
        // Configurer les colonnes du tableau
        colTransaction.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colDevise.setCellValueFactory(new PropertyValueFactory<>("devise"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colActions.setCellValueFactory(new PropertyValueFactory<>("actions"));
        
        // Ajouter des données de test
        ajouterTransactionsTest();
        
        // Ajouter les listeners pour mettre à jour le résumé
        comboUtilisateur.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateResume());
        comboEvenement.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateResume());
        txtMontant.textProperty().addListener((obs, oldVal, newVal) -> updateResume());
        
        // Initialiser les labels
        updateStats();
    }
    
    @FXML
    public void handlePayer() {
        String utilisateur = comboUtilisateur.getValue();
        String evenement = comboEvenement.getValue();
        String montantStr = txtMontant.getText().trim();
        
        if (utilisateur == null || evenement == null || montantStr.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez remplir tous les champs", AlertType.WARNING);
            return;
        }
        
        try {
            double montant = Double.parseDouble(montantStr);
            if (montant <= 0) {
                afficherAlerte("Erreur", "Le montant doit être positif", AlertType.WARNING);
                return;
            }
            
            // Simulation de paiement Stripe
            String transactionId = genererTransactionId();
            boolean paiementReussi = simulerPaiementStripe(montant);
            
            if (paiementReussi) {
                // Créer la transaction
                Transaction transaction = new Transaction(
                    transactionId,
                    montant,
                    "EUR",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    "Complété",
                    utilisateur.split(" - ")[0],
                    evenement.split(" - ")[0]
                );
                
                transactions.add(transaction);
                tableHistorique.setItems(transactions);
                
                // Calculer les points gagnés (1 point par euro dépensé)
                int pointsGagnes = (int) montant;
                
                labelResultatPaiement.setText("✅ Paiement réussi ! Transaction ID: " + transactionId + 
                                           "\n🎉 Points gagnés: " + pointsGagnes + " points");
                labelResultatPaiement.setStyle("-fx-text-fill: #4caf50;");
                
                // Vider les champs
                comboUtilisateur.getSelectionModel().clearSelection();
                comboEvenement.getSelectionModel().clearSelection();
                txtMontant.clear();
                updateResume();
                
                updateStats();
                
            } else {
                labelResultatPaiement.setText("❌ Échec du paiement. Veuillez réessayer.");
                labelResultatPaiement.setStyle("-fx-text-fill: #f44336;");
            }
            
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Montant invalide", AlertType.WARNING);
        }
    }
    
    @FXML
    public void handleRembourser() {
        String transactionId = txtTransactionId.getText().trim();
        
        if (transactionId.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez entrer un ID de transaction", AlertType.WARNING);
            return;
        }
        
        // Chercher la transaction
        Transaction transactionATrouver = null;
        for (Transaction tx : transactions) {
            if (tx.getTransactionId().equals(transactionId)) {
                transactionATrouver = tx;
                break;
            }
        }
        
        if (transactionATrouver == null) {
            afficherAlerte("Erreur", "Transaction non trouvée", AlertType.WARNING);
            return;
        }
        
        if ("Remboursé".equals(transactionATrouver.getStatut())) {
            afficherAlerte("Erreur", "Transaction déjà remboursée", AlertType.WARNING);
            return;
        }
        
        // Simuler le remboursement
        boolean remboursementReussi = simulerRemboursementStripe(transactionId);
        
        if (remboursementReussi) {
            transactionATrouver.setStatut("Remboursé");
            tableHistorique.refresh();
            
            afficherAlerte("Succès", "Remboursement effectué pour la transaction " + transactionId, AlertType.INFORMATION);
            txtTransactionId.clear();
            updateStats();
        } else {
            afficherAlerte("Erreur", "Échec du remboursement", AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleVoirHistorique() {
        String utilisateurSelectionne = comboUtilisateurHistorique.getValue();
        
        if (utilisateurSelectionne == null) {
            tableHistorique.setItems(transactions);
            return;
        }
        
        if ("Tous les utilisateurs".equals(utilisateurSelectionne)) {
            tableHistorique.setItems(transactions);
        } else {
            ObservableList<Transaction> transactionsFiltrees = FXCollections.observableArrayList();
            for (Transaction tx : transactions) {
                if (tx.getUtilisateur().equals(utilisateurSelectionne)) {
                    transactionsFiltrees.add(tx);
                }
            }
            tableHistorique.setItems(transactionsFiltrees);
        }
    }
    
    @FXML
    public void handleStats() {
        int totalTransactions = transactions.size();
        double totalMontant = 0;
        int transactionsCompletees = 0;
        int transactionsRemboursees = 0;
        
        for (Transaction tx : transactions) {
            totalMontant += tx.getMontant();
            if ("Complété".equals(tx.getStatut())) {
                transactionsCompletees++;
            } else if ("Remboursé".equals(tx.getStatut())) {
                transactionsRemboursees++;
            }
        }
        
        String stats = "📊 Statistiques des paiements\n\n" +
                      "Total transactions: " + totalTransactions + "\n" +
                      "Transactions complétées: " + transactionsCompletees + "\n" +
                      "Transactions remboursées: " + transactionsRemboursees + "\n" +
                      "Montant total: " + String.format("%.2f €", totalMontant) + "\n" +
                      "Montant moyen: " + (totalTransactions > 0 ? String.format("%.2f €", totalMontant / totalTransactions) : "0.00 €");
        
        afficherAlerte("Statistiques", stats, AlertType.INFORMATION);
    }
    
    private void updateResume() {
        String utilisateur = comboUtilisateur.getValue();
        String evenement = comboEvenement.getValue();
        String montantStr = txtMontant.getText();
        
        if (utilisateur != null) {
            labelResumeUser.setText("Utilisateur : " + utilisateur.split(" - ")[0]);
        } else {
            labelResumeUser.setText("Utilisateur : --");
        }
        
        if (evenement != null) {
            labelResumeEvent.setText("Événement : " + evenement.split(" - ")[0]);
        } else {
            labelResumeEvent.setText("Événement : --");
        }
        
        if (!montantStr.isEmpty()) {
            try {
                double montant = Double.parseDouble(montantStr);
                labelResumeMontant.setText("Montant : " + String.format("%.2f €", montant));
                labelResumePoints.setText("Points gagnés : " + (int) montant);
            } catch (NumberFormatException e) {
                labelResumeMontant.setText("Montant : -- €");
                labelResumePoints.setText("Points gagnés : --");
            }
        } else {
            labelResumeMontant.setText("Montant : -- €");
            labelResumePoints.setText("Points gagnés : --");
        }
    }
    
    private void updateStats() {
        int totalTransactions = transactions.size();
        double totalMontant = 0;
        
        for (Transaction tx : transactions) {
            if (!"Remboursé".equals(tx.getStatut())) {
                totalMontant += tx.getMontant();
            }
        }
        
        labelTotalTx.setText("Transactions : " + totalTransactions);
        labelTotalMontant.setText("Total : " + String.format("%.2f €", totalMontant));
    }
    
    private String genererTransactionId() {
        return "pi_" + System.currentTimeMillis() + "_" + random.nextInt(1000);
    }
    
    private boolean simulerPaiementStripe(double montant) {
        // Simulation de paiement Stripe (90% de succès)
        return random.nextDouble() > 0.1;
    }
    
    private boolean simulerRemboursementStripe(String transactionId) {
        // Simulation de remboursement Stripe (95% de succès)
        return random.nextDouble() > 0.05;
    }
    
    private void ajouterTransactionsTest() {
        // Ajouter quelques transactions de test
        transactions.addAll(
            new Transaction("pi_1704067200000_123", 50.00, "EUR", "01/01/2024 14:30", "Complété", "Jean Dupont", "Conférence GreenTech 2024"),
            new Transaction("pi_1704153600000_456", 25.00, "EUR", "02/01/2024 10:15", "Complété", "Marie Martin", "Atelier Recyclage"),
            new Transaction("pi_1704240000000_789", 75.00, "EUR", "03/01/2024 16:45", "Remboursé", "Pierre Bernard", "Forum Développement Durable"),
            new Transaction("pi_1704326400000_321", 100.00, "EUR", "04/01/2024 09:20", "Complété", "Sophie Petit", "Formation Écologie")
        );
        
        tableHistorique.setItems(transactions);
    }
    
    private void afficherAlerte(String titre, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Classe interne pour représenter une transaction
    public static class Transaction {
        private String transactionId;
        private double montant;
        private String devise;
        private String date;
        private String statut;
        private String utilisateur;
        private String evenement;
        private String actions;
        
        public Transaction(String transactionId, double montant, String devise, String date, String statut, String utilisateur, String evenement) {
            this.transactionId = transactionId;
            this.montant = montant;
            this.devise = devise;
            this.date = date;
            this.statut = statut;
            this.utilisateur = utilisateur;
            this.evenement = evenement;
            this.actions = "Voir";
        }
        
        // Getters
        public String getTransactionId() { return transactionId; }
        public double getMontant() { return montant; }
        public String getDevise() { return devise; }
        public String getDate() { return date; }
        public String getStatut() { return statut; }
        public String getActions() { return actions; }
        public String getUtilisateur() { return utilisateur; }
        public String getEvenement() { return evenement; }
        
        // Setters
        public void setStatut(String statut) { this.statut = statut; }
    }
}
