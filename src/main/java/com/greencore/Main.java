package com.greencore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/login.fxml"));
            Scene scene = new Scene(root, 800, 600);
            
            scene.getStylesheets().add(getClass().getResource("/view/css/style.css").toExternalForm());
            
            primaryStage.setTitle("GREENCORE - Gestion Communautaire de Matériel");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void stop() {
        com.greencore.config.DatabaseConfig.close();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
