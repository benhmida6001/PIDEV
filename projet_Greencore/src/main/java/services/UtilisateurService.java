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
        cnx = MyConnection.getInstance().getCnx();
    }
    
    @Override
    public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        String req = "INSERT INTO utilisateur (nom, prenom, adrEmail, numTel, adresse, role, mdp, pointGagne) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement pst = cnx.prepareStatement(req);
        pst.setString(1, utilisateur.getNom());
        pst.setString(2, utilisateur.getPrenom());
        pst.setString(3, utilisateur.getAdrEmail());
        pst.setString(4, utilisateur.getNumTel());
        pst.setString(5, utilisateur.getAdresse());
        pst.setString(6, utilisateur.getRole());
        pst.setString(7, utilisateur.getMdp());
        pst.setInt(8, utilisateur.getPointGagne());
        
        pst.executeUpdate();
        System.out.println("Utilisateur ajouté avec succès !");
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
        String req = "SELECT * FROM utilisateur";
        
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
        
        pst.executeUpdate();
        System.out.println("Utilisateur modifié avec succès !");
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
        String req = "DELETE FROM utilisateur WHERE id=?";
        
        PreparedStatement pst = cnx.prepareStatement(req);
        pst.setInt(1, id);
        
        pst.executeUpdate();
        System.out.println("Utilisateur supprimé avec succès !");
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
        String req = "SELECT * FROM utilisateur WHERE adrEmail = ? AND mdp = ?";
        
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
        
        return utilisateurs;
    }
    
    @Override
    public void mettreAJourPoints(int id, int points) throws SQLException {
        String req = "UPDATE utilisateur SET pointGagne = pointGagne + ? WHERE id = ?";
        
        PreparedStatement pst = cnx.prepareStatement(req);
        pst.setInt(1, points);
        pst.setInt(2, id);
        
        pst.executeUpdate();
        System.out.println("Points de l'utilisateur mis à jour avec succès !");
    }
}
