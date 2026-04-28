package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/pidev";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static Connection connection;
    private static MyConnection instance;
    
    private MyConnection() {}
    
    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Essayer de se connecter à la base de données
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion à la base de données établie");
                
                // Test if table exists and has data
                testDatabaseConnection();
                
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("⚠️ Base de données non disponible: " + e.getMessage());
                System.out.println("🔄 Passage en mode démo sans base de données");
                // Ne pas lancer d'exception - permettre à l'application de fonctionner en mode démo
                return null;
            }
        }
        return connection;
    }
    
    private void testDatabaseConnection() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM utilisateur");
            
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("📊 Found " + count + " users in utilisateur table");
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Error testing database connection: " + e.getMessage());
        }
    }
    
    public Connection getCnx() throws SQLException {
        return getConnection();
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Connexion à la base de données fermée");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
}
