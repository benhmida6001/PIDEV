package services;

import models.Utilisateur;
import tools.MyConnection;
import interfaces.IUtilisateurService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UtilisateurService implements IUtilisateurService {
    
    private Connection cnx;
    
    public UtilisateurService() {
        try {
            cnx = MyConnection.getInstance().getCnx();
            if (cnx == null) {
                System.out.println("🔄 UtilisateurService initialisé en mode démo (sans base de données)");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur de connexion à la base de données : " + ex.getMessage());
            System.out.println("🔄 UtilisateurService initialisé en mode démo (sans base de données)");
            cnx = null;
        }
    }
    
    @Override
    public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        String req = "INSERT INTO utilisateur (nom, prenom, adrEmail, numTel, adresse, role, mdp, pointGagne) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, utilisateur.getNom());
            pst.setString(2, utilisateur.getPrenom());
            pst.setString(3, utilisateur.getAdrEmail());
            pst.setString(4, utilisateur.getNumTel());
            pst.setString(5, utilisateur.getAdresse());
            pst.setString(6, utilisateur.getRole());
            pst.setString(7, utilisateur.getMdp());
            pst.setInt(8, utilisateur.getPointGagne());
            
            pst.executeUpdate();
            
            // Récupérer l'ID généré
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                utilisateur.setId(rs.getInt(1));
            }
            
            System.out.println("Utilisateur ajouté avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + ex.getMessage());
            throw ex;
        }
    }
    
    // Méthode de compatibilité
    public void ajouterUtilisateurCompat(Utilisateur utilisateur) {
        try {
            ajouterUtilisateur(utilisateur);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + ex.getMessage());
        }
    }
    
    @Override
    public List<Utilisateur> afficherUtilisateurs() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String req = "SELECT * FROM utilisateur ORDER BY nom, prenom ASC";
        
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            
            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("adrEmail"),
                    rs.getString("numTel"),
                    rs.getString("adresse"),
                    rs.getString("role"),
                    rs.getString("mdp"),
                    rs.getInt("pointGagne")
                );
                utilisateurs.add(u);
            }
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'affichage des utilisateurs : " + ex.getMessage());
            throw ex;
        }
        
        return utilisateurs;
    }
    
    // Méthode de compatibilité
    public List<Utilisateur> afficherUtilisateursCompat() {
        try {
            return afficherUtilisateurs();
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'affichage des utilisateurs : " + ex.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public void modifierUtilisateur(Utilisateur utilisateur) throws SQLException {
        String req = "UPDATE utilisateur SET nom=?, prenom=?, adrEmail=?, numTel=?, adresse=?, role=?, mdp=?, pointGagne=? WHERE id=?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, utilisateur.getNom());
            pst.setString(2, utilisateur.getPrenom());
            pst.setString(3, utilisateur.getAdrEmail());
            pst.setString(4, utilisateur.getNumTel());
            pst.setString(5, utilisateur.getAdresse());
            pst.setString(6, utilisateur.getRole());
            pst.setString(7, utilisateur.getMdp());
            pst.setInt(8, utilisateur.getPointGagne());
            pst.setInt(9, utilisateur.getId());
            
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucun utilisateur trouvé avec l'ID : " + utilisateur.getId());
            }
            
            System.out.println("Utilisateur modifié avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la modification de l'utilisateur : " + ex.getMessage());
            throw ex;
        }
    }
    
    // Méthode de compatibilité
    public void modifierUtilisateurCompat(Utilisateur utilisateur) {
        try {
            modifierUtilisateur(utilisateur);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la modification de l'utilisateur : " + ex.getMessage());
        }
    }
    
    @Override
    public void supprimerUtilisateur(int id) throws SQLException {
        String deleteUser = "DELETE FROM utilisateur WHERE id=?";
        
        try {
            // Tenter d'abord de supprimer les participations si la table existe
            try {
                String deleteParticipations = "DELETE FROM participation WHERE idUtilisateur=?";
                PreparedStatement pst1 = cnx.prepareStatement(deleteParticipations);
                pst1.setInt(1, id);
                pst1.executeUpdate();
                System.out.println("Participations supprimées avec succès !");
            } catch (SQLException participationEx) {
                // La table participation n'existe pas, c'est normal
                System.out.println("Table participation non trouvée, continuation de la suppression de l'utilisateur...");
            }
            
            // Supprimer l'utilisateur
            PreparedStatement pst2 = cnx.prepareStatement(deleteUser);
            pst2.setInt(1, id);
            int rowsAffected = pst2.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Aucun utilisateur trouvé avec l'ID : " + id);
            }
            
            System.out.println("Utilisateur supprimé avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + ex.getMessage());
            throw ex;
        }
    }
    
    // Méthode de compatibilité
    public void supprimerUtilisateurCompat(int id) {
        try {
            supprimerUtilisateur(id);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + ex.getMessage());
        }
    }
    
    @Override
    public List<Utilisateur> rechercherUtilisateurParId(int id) throws SQLException {
        String req = "SELECT * FROM utilisateur WHERE id=?";

        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("adrEmail"),
                        rs.getString("numTel"),
                        rs.getString("adresse"),
                        rs.getString("role"),
                        rs.getString("mdp"),
                        rs.getInt("pointGagne")
                );
                return Collections.singletonList(u);
            }

            return Collections.emptyList();
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche de l'utilisateur : " + ex.getMessage());
            throw ex;
        }
    }

    /** Retourne l'utilisateur ou {@code null} si absent (compatibilité code existant). */
    public Utilisateur rechercherUtilisateurParIdCompat(int id) {
        try {
            List<Utilisateur> r = rechercherUtilisateurParId(id);
            return r.isEmpty() ? null : r.get(0);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche de l'utilisateur : " + ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Utilisateur> authentifier(String email, String motDePasse) throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        
        // Mode démo si pas de connexion à la base de données
        if (cnx == null) {
            System.out.println(" Authentification en mode démo");
            
            // Comptes de démonstration
            if ((email.equals("admin@greencore.com") && motDePasse.equals("admin")) ||
                (email.equals("user@greencore.com") && motDePasse.equals("user")) ||
                (email.equals("test@greencore.com") && motDePasse.equals("test"))) {
                
                String role = email.equals("admin@greencore.com") ? "ADMIN" : "USER";
                String nom = email.equals("admin@greencore.com") ? "Admin" : 
                           email.equals("user@greencore.com") ? "User" : "Test";
                String prenom = email.equals("admin@greencore.com") ? "System" : 
                              email.equals("user@greencore.com") ? "Demo" : "Account";
                
                Utilisateur demoUser = new Utilisateur(
                    1, // ID fictif
                    nom,
                    prenom,
                    email,
                    "1234567890", // Numéro de téléphone fictif
                    "Adresse de démonstration",
                    role,
                    motDePasse,
                    1000 // Points de départ
                );
                utilisateurs.add(demoUser);
                System.out.println(" Utilisateur démo authentifié: " + nom + " (" + role + ")");
            }
            return utilisateurs;
        }
        
        // Mode normal avec base de données
        String req = "SELECT * FROM utilisateur WHERE adrEmail = ? AND mdp = ?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, email);
            pst.setString(2, motDePasse);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("adrEmail"),
                    rs.getString("numTel"),
                    rs.getString("adresse"),
                    rs.getString("role"),
                    rs.getString("mdp"),
                    rs.getInt("pointGagne")
                );
                utilisateurs.add(u);
            }
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'authentification : " + ex.getMessage());
        }
        
        return utilisateurs;
    }
    
// ...
    public void mettreAJourPoints(int id, int points) throws SQLException {
        String req = "UPDATE utilisateur SET pointGagne = pointGagne + ? WHERE id = ?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, points);
            pst.setInt(2, id);
            
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucun utilisateur trouvé avec l'ID : " + id);
            }
            
            System.out.println("Points de l'utilisateur mis à jour avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la mise à jour des points : " + ex.getMessage());
            throw ex;
        }
    }
    
    // Méthodes supplémentaires utiles
    
    public List<Utilisateur> rechercherUtilisateursParCritere(String critere) throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String req = "SELECT * FROM utilisateur WHERE nom LIKE ? OR prenom LIKE ? OR adrEmail LIKE ? ORDER BY nom, prenom ASC";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            String searchPattern = "%" + critere + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("adrEmail"),
                    rs.getString("numTel"),
                    rs.getString("adresse"),
                    rs.getString("role"),
                    rs.getString("mdp"),
                    rs.getInt("pointGagne")
                );
                utilisateurs.add(u);
            }
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche des utilisateurs : " + ex.getMessage());
            throw ex;
        }
        
        return utilisateurs;
    }
    
    public List<Utilisateur> getUtilisateursParRole(String role) throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String req = "SELECT * FROM utilisateur WHERE role = ? ORDER BY nom, prenom ASC";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, role);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("adrEmail"),
                    rs.getString("numTel"),
                    rs.getString("adresse"),
                    rs.getString("role"),
                    rs.getString("mdp"),
                    rs.getInt("pointGagne")
                );
                utilisateurs.add(u);
            }
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des utilisateurs par rôle : " + ex.getMessage());
            throw ex;
        }
        
        return utilisateurs;
    }
    
    public boolean emailExiste(String email) throws SQLException {
        String req = "SELECT COUNT(*) FROM utilisateur WHERE adrEmail = ?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, email);
            
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la vérification de l'email : " + ex.getMessage());
            throw ex;
        }
    }
}
