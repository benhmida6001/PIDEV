package com.greencore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/views/authentification.fxml")
        );
        Parent root = loader.load();

        Scene scene = new Scene(root, 400, 350);
        stage.setTitle("GreenCore - Authentification");
        stage.setMinWidth(350);
        stage.setMinHeight(300);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
