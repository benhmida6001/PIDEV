package com.greencore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Point d'entrée principal : page d'accueil ({@code main.fxml}). */
public class MainApp2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                MainApp2.class.getResource("/views/main.fxml")
        );

        Scene scene = new Scene(loader.load(), 900, 600);
        stage.setTitle("GreenCore — Page Principale");
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
