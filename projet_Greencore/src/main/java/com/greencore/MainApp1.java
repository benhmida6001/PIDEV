package com.greencore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Point d’entrée alternatif : liste des utilisateurs ({@code afficherUtilisateurs.fxml}). */
public class MainApp1 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                MainApp1.class.getResource("/views/afficherUtilisateurs.fxml")
        );

        Scene scene = new Scene(loader.load(), 900, 600);
        stage.setTitle("GreenCore — Liste des utilisateurs");
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
