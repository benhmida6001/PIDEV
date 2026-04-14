package com.greencore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Point d'entrée pour la mise à jour des points ({@code mettreAJourPoints.fxml}). */
public class MainApp3 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                MainApp3.class.getResource("/views/mettreAJourPoints.fxml")
        );

        Scene scene = new Scene(loader.load(), 400, 300);
        stage.setTitle("GreenCore — Mettre à jour les Points");
        stage.setMinWidth(350);
        stage.setMinHeight(250);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
