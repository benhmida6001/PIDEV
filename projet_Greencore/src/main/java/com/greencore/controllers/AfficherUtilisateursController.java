package com.greencore.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Utilisateur;
import services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherUtilisateursController {

    @FXML
    private TableView<Utilisateur> utilisateursTable;
    
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
    private TableColumn<Utilisateur, Integer> pointsColumn;
    
    private UtilisateurService utilisateurService;
    private ObservableList<Utilisateur> utilisateurList;
    
    @FXML
    public void initialize() {
        utilisateurService = new UtilisateurService();
        utilisateurList = FXCollections.observableArrayList();
        
        // Configurer les colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("adrEmail"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("pointGagne"));

        utilisateursTable.setRowFactory(tv -> {
            TableRow<Utilisateur> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()
                        && event.getButton() == MouseButton.PRIMARY) {
                    ouvrirFormulaireModification(row.getItem());
                }
            });
            return row;
        });
        
        // Charger les utilisateurs
        loadUtilisateurs();
    }
    
    private void loadUtilisateurs() {
        try {
            List<Utilisateur> utilisateurs = utilisateurService.afficherUtilisateurs();
            utilisateurList.clear();
            utilisateurList.addAll(utilisateurs);
            utilisateursTable.setItems(utilisateurList);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des utilisateurs : " + e.getMessage());
        }
    }
    
    @FXML
    private void handleActualiser(ActionEvent event) {
        loadUtilisateurs();
    }
    
    @FXML
    private void handleRechercherParId(ActionEvent event) {
        try {
            java.net.URL location = getClass().getResource("/views/rechercherUtilisateur.fxml");
            if (location == null) {
                showAlert("Erreur", "FXML introuvable : /views/rechercherUtilisateur.fxml");
                return;
            }
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(location);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Recherche par ID");
            stage.setScene(new Scene(root, 780, 480));
            stage.setMinWidth(640);
            stage.setMinHeight(400);
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture : " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterUtilisateur(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ajouterUtilisateur.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter Utilisateur");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> loadUtilisateurs());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire : " + e.getMessage());
        }
    }
    

    
    @FXML
    private void handleModifier(ActionEvent event) {
        Utilisateur selectedUtilisateur = utilisateursTable.getSelectionModel().getSelectedItem();
        if (selectedUtilisateur == null) {
            showAlert("Attention",
                    "Aucune ligne sélectionnée.\nSélectionnez une ligne puis « Modifier », ou double-cliquez sur une ligne.");
            return;
        }
        ouvrirFormulaireModification(selectedUtilisateur);
    }

    /** Charge {@code /views/modifierUtilisateur.fxml} et préremplit le formulaire. */
    private void ouvrirFormulaireModification(Utilisateur utilisateur) {
        try {
            java.net.URL location = getClass().getResource("/views/modifierUtilisateur.fxml");
            if (location == null) {
                showAlert("Erreur", "FXML introuvable : /views/modifierUtilisateur.fxml");
                return;
            }
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(location);
            Parent root = loader.load();
            ModifierUtilisateurController controller = loader.getController();
            controller.precharger(utilisateur);

            Stage stage = new Stage();
            stage.setTitle("Modifier Utilisateur");
            stage.setScene(new Scene(root, 480, 560));
            stage.setMinWidth(420);
            stage.setMinHeight(480);
            stage.setOnHidden(e -> loadUtilisateurs());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire : " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSupprimer(ActionEvent event) {
        Utilisateur selectedUtilisateur = utilisateursTable.getSelectionModel().getSelectedItem();
        if (selectedUtilisateur == null) {
            showAlert("Attention", "Veuillez sélectionner un utilisateur à supprimer");
            return;
        }
        ouvrirFormulaireSuppression(selectedUtilisateur);
    }

    private void ouvrirFormulaireSuppression(Utilisateur utilisateur) {
        try {
            java.net.URL location = getClass().getResource("/views/supprimerUtilisateur.fxml");
            if (location == null) {
                showAlert("Erreur", "FXML introuvable : /views/supprimerUtilisateur.fxml");
                return;
            }
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(location);
            Parent root = loader.load();
            SupprimerUtilisateurController controller = loader.getController();
            controller.precharger(utilisateur);

            Stage stage = new Stage();
            stage.setTitle("Supprimer utilisateur");
            stage.setScene(new Scene(root, 460, 280));
            stage.setMinWidth(400);
            stage.setMinHeight(220);
            stage.setOnHidden(e -> loadUtilisateurs());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire : " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDetails(ActionEvent event) {
        Utilisateur selectedUtilisateur = utilisateursTable.getSelectionModel().getSelectedItem();
        if (selectedUtilisateur == null) {
            showAlert("Attention", "Veuillez sélectionner un utilisateur");
            return;
        }
        
        Alert detailsDialog = new Alert(Alert.AlertType.INFORMATION);
        detailsDialog.setTitle("Détails de l'utilisateur");
        detailsDialog.setHeaderText(null);
        detailsDialog.setContentText(
            "ID: " + selectedUtilisateur.getId() + "\n" +
            "Nom: " + selectedUtilisateur.getNom() + "\n" +
            "Prénom: " + selectedUtilisateur.getPrenom() + "\n" +
            "Email: " + selectedUtilisateur.getAdrEmail() + "\n" +
            "Téléphone: " + selectedUtilisateur.getNumTel() + "\n" +
            "Adresse: " + selectedUtilisateur.getAdresse() + "\n" +
            "Rôle: " + selectedUtilisateur.getRole() + "\n" +
            "Points: " + selectedUtilisateur.getPointGagne()
        );
        detailsDialog.showAndWait();
    }
    
    @FXML
    private void handleMettreAJourPoints(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mettreAJourPoints.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);
            stage.setTitle("Mettre à jour les points");
            stage.setMinWidth(400);
            stage.setMinHeight(300);
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de mise à jour des points : " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) utilisateursTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Application Greencore");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du retour au menu principal : " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        Label label = new Label(message);
        label.setWrapText(true);
        label.setMaxWidth(520);
        VBox box = new VBox(label);
        box.setPadding(new Insets(8, 0, 0, 0));
        alert.getDialogPane().setContent(box);
        alert.getDialogPane().setMinWidth(440);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
