import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class test_maps extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load the map view directly
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/map_view.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("🗺️ Test Carte des Événements");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            System.err.println("Error loading map: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
