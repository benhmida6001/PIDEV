package com.greencore.controllers;



import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.scene.control.Alert;

import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;

import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;



import java.io.IOException;

import models.Utilisateur;

import services.UtilisateurService;



import java.sql.SQLException;

import java.util.List;



public class RechercherUtilisateurController {



    @FXML

    private TextField idField;



    @FXML

    private TableView<Utilisateur> resultatsTable;



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

    private final ObservableList<Utilisateur> resultats = FXCollections.observableArrayList();



    @FXML

    public void initialize() {

        utilisateurService = new UtilisateurService();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));

        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("adrEmail"));

        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("numTel"));

        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("pointGagne"));

        resultatsTable.setItems(resultats);

    }



    @FXML

    void handleRechercher(ActionEvent event) {

        String raw = idField.getText() == null ? "" : idField.getText().trim();

        if (raw.isEmpty()) {

            showAlert("Attention", "Saisissez un identifiant numérique.");

            return;

        }

        try {

            rechercherParId(Integer.parseInt(raw));

        } catch (NumberFormatException e) {

            showAlert("Erreur", "L'ID doit être un nombre entier.");

        }

    }



    /**

     * Renseigne le champ ID et appelle {@link UtilisateurService#rechercherUtilisateurParId(int)}.

     * Utile au démarrage depuis {@link com.greencore.MainApp} (argument de ligne de commande).

     */

    public void rechercherParId(int id) {

        idField.setText(String.valueOf(id));

        executerRecherche(id);

    }



    private void executerRecherche(int id) {

        try {

            List<Utilisateur> liste = utilisateurService.rechercherUtilisateurParId(id);

            resultats.setAll(liste);

            if (liste.isEmpty()) {

                showAlert("Résultat", "Aucun utilisateur trouvé pour l'ID " + id + ".");

            }

        } catch (SQLException e) {

            showAlert("Erreur", "Erreur lors de la recherche : " + e.getMessage());

        }

    }



    @FXML

    void handleMettreAJourPoints(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mettreAJourPoints.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Mettre à jour les Points");

            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {

            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire : " + e.getMessage());

        }

    }



    @FXML

    void handleFermer(ActionEvent event) {

        Stage stage = (Stage) idField.getScene().getWindow();

        stage.close();

    }



    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();

    }

}

