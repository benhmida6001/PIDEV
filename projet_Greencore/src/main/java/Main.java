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
            System.out.println("Démarrage de l'application Greencore...");
            
            // Charger le fichier FXML du login simplifié
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            // Créer la scène avec une taille initiale
            Scene scene = new Scene(root, 600, 400);
            
            // Configuration de la fenêtre avec redimensionnement
            primaryStage.setTitle("Greencore - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true); // Permettre le redimensionnement
            primaryStage.setMinWidth(500);   // Taille minimale
            primaryStage.setMinHeight(350);
            primaryStage.show();
            
            System.out.println("Application démarrée avec succès !");
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Configuration JVM minimale
        System.out.println("Configuration JVM: " + System.getProperty("java.vm.name"));
        System.out.println("Mémoire disponible: " + (Runtime.getRuntime().freeMemory() / 1024 / 1024) + "MB");
        
        launch(args);
    }
}
