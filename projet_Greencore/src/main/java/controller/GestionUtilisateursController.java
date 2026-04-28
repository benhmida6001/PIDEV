package controller;

import interfaces.IUtilisateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Utilisateur;
import services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GestionUtilisateursController {

    @FXML
    private TableView<Utilisateur> tableViewUtilisateurs;

    @FXML
    private TableColumn<Utilisateur, Integer> idColumn;

    @FXML
    private TableColumn<Utilisateur, String> nomColumn;

    @FXML
    private TableColumn<Utilisateur, String> prenomColumn;

    @FXML
    private TableColumn<Utilisateur, String> emailColumn;

    @FXML
    private TableColumn<Utilisateur, String> telephoneColumn;

    @FXML
    private TableColumn<Utilisateur, String> roleColumn;

    @FXML
    private TableColumn<Utilisateur, String> mdpColumn;

    @FXML
    private TableColumn<Utilisateur, Integer> pointGagneColumn;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnRechercher;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Label lblStatus;

    private IUtilisateurService utilisateurService;
    private ObservableList<Utilisateur> utilisateurList;

    @FXML
    public void initialize() {
        utilisateurService = new UtilisateurService();
        utilisateurList = FXCollections.observableArrayList();

        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("adrEmail"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        mdpColumn.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        pointGagneColumn.setCellValueFactory(new PropertyValueFactory<>("pointGagne"));

        // Lier la liste à la TableView
        tableViewUtilisateurs.setItems(utilisateurList);

        // Charger les utilisateurs au démarrage
        chargerUtilisateurs();

        // Activer/désactiver les boutons selon la sélection
        tableViewUtilisateurs.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean isSelected = newSelection != null;
                    btnModifier.setDisable(!isSelected);
                    btnSupprimer.setDisable(!isSelected);
                });

        // État initial des boutons
        btnModifier.setDisable(true);
        btnSupprimer.setDisable(true);
    }

    private void chargerUtilisateurs() {
        try {
            List<Utilisateur> utilisateurs = utilisateurService.afficherUtilisateurs();
            utilisateurList.clear();
            utilisateurList.addAll(utilisateurs);
            lblStatus.setText("Utilisateurs chargés: " + utilisateurs.size());
        } catch (SQLException e) {
            afficherErreur("Erreur lors du chargement des utilisateurs", e.getMessage());
            lblStatus.setText("Erreur de chargement");
        }
    }

    @FXML
    void handleAjouterUtilisateur(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouter_utilisateur.fxml"));
            Parent root = loader.load();

            AjouterUtilisateurController controller = loader.getController();
            controller.setGestionUtilisateursController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un utilisateur");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            afficherErreur("Erreur d'interface", "Impossible d'ouvrir la fenêtre d'ajout: " + e.getMessage());
        }
    }

    @FXML
    void handleRechercherUtilisateur(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rechercher_utilisateur.fxml"));
            Parent root = loader.load();

            RechercherUtilisateurController controller = loader.getController();
            controller.setGestionUtilisateursController(this);

            Stage stage = new Stage();
            stage.setTitle("Rechercher un utilisateur");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            afficherErreur("Erreur d'interface", "Impossible d'ouvrir la fenêtre de recherche: " + e.getMessage());
        }
    }

    @FXML
    void handleModifierUtilisateur(ActionEvent event) {
        Utilisateur selectedUtilisateur = tableViewUtilisateurs.getSelectionModel().getSelectedItem();
        
        if (selectedUtilisateur == null) {
            afficherInformation("Aucune sélection", "Veuillez sélectionner un utilisateur à modifier");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifier_utilisateur.fxml"));
            Parent root = loader.load();

            ModifierUtilisateurController controller = loader.getController();
            controller.setUtilisateur(selectedUtilisateur);
            controller.setGestionUtilisateursController(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier un utilisateur");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            afficherErreur("Erreur d'interface", "Impossible d'ouvrir la fenêtre de modification: " + e.getMessage());
        }
    }

    @FXML
    void handleSupprimerUtilisateur(ActionEvent event) {
        Utilisateur selectedUtilisateur = tableViewUtilisateurs.getSelectionModel().getSelectedItem();
        
        if (selectedUtilisateur == null) {
            afficherInformation("Aucune sélection", "Veuillez sélectionner un utilisateur à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'utilisateur");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer l'utilisateur \"" + 
                           selectedUtilisateur.getNom() + " " + selectedUtilisateur.getPrenom() + "\" ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                utilisateurService.supprimerUtilisateur(selectedUtilisateur.getId());
                utilisateurList.remove(selectedUtilisateur);
                lblStatus.setText("Utilisateur supprimé avec succès");
                afficherInformation("Succès", "Utilisateur supprimé avec succès");
            } catch (SQLException e) {
                afficherErreur("Erreur de suppression", e.getMessage());
                lblStatus.setText("Erreur de suppression");
            }
        }
    }

    // Méthodes utilitaires pour les fenêtres de dialogue
    public void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void afficherInformation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour rafraîchir la liste (appelée par les autres contrôleurs)
    public void rafraichirListe() {
        chargerUtilisateurs();
    }

    // Méthode pour afficher des résultats de recherche
    public void afficherResultatsRecherche(List<Utilisateur> resultats) {
        utilisateurList.clear();
        utilisateurList.addAll(resultats);
        lblStatus.setText("Résultats de recherche: " + resultats.size() + " utilisateur(s)");
    }

    // Getters pour les autres contrôleurs
    public IUtilisateurService getUtilisateurService() {
        return utilisateurService;
    }

    public Label getLblStatus() {
        return lblStatus;
    }
}
