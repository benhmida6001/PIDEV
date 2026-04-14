package services;

import models.Evenement;
import models.Utilisateur;
import tools.MyConnection;
import interfaces.IEvenementService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements IEvenementService {
    
    private Connection cnx;
    
    public EvenementService() {
        cnx = MyConnection.getInstance().getCnx();
    }
    
    public void ajouterEvenement(Evenement evenement) throws SQLException {
        String req = "INSERT INTO evenement (nomEvent, dateEvent, lieuEvent, description, capaciteMax, pointsOfferts) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, evenement.getNomEvent());
            pst.setTimestamp(2, new Timestamp(evenement.getDateEvent().getTime()));
            pst.setString(3, evenement.getLieuEvent());
            pst.setString(4, evenement.getDescription());
            pst.setInt(5, evenement.getCapaciteMax());
            pst.setInt(6, evenement.getPointsOfferts());
            
            pst.executeUpdate();
            System.out.println("Événement ajouté avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout de l'événement : " + ex.getMessage());
            throw ex;
        }
    }
    
    public List<Evenement> afficherEvenements() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String req = "SELECT * FROM evenement";
        
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            
            while (rs.next()) {
                Evenement e = new Evenement(
                    rs.getInt("idEvent"),
                    rs.getString("nomEvent"),
                    rs.getTimestamp("dateEvent"),
                    rs.getString("lieuEvent"),
                    rs.getString("description"),
                    rs.getInt("capaciteMax"),
                    rs.getInt("pointsOfferts")
                );
                evenements.add(e);
            }
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'affichage des événements : " + ex.getMessage());
            throw ex;
        }
        
        return evenements;
    }
    
    public void modifierEvenement(Evenement evenement) throws SQLException {
        String req = "UPDATE evenement SET nomEvent=?, dateEvent=?, lieuEvent=?, description=?, capaciteMax=?, pointsOfferts=? WHERE idEvent=?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, evenement.getNomEvent());
            pst.setTimestamp(2, new Timestamp(evenement.getDateEvent().getTime()));
            pst.setString(3, evenement.getLieuEvent());
            pst.setString(4, evenement.getDescription());
            pst.setInt(5, evenement.getCapaciteMax());
            pst.setInt(6, evenement.getPointsOfferts());
            pst.setInt(7, evenement.getIdEvent());
            
            pst.executeUpdate();
            System.out.println("Événement modifié avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la modification de l'événement : " + ex.getMessage());
            throw ex;
        }
    }
    
    public void supprimerEvenement(int idEvent) throws SQLException {
        String req = "DELETE FROM evenement WHERE idEvent=?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, idEvent);
            
            pst.executeUpdate();
            System.out.println("Événement supprimé avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression de l'événement : " + ex.getMessage());
            throw ex;
        }
    }
    
    public List<Evenement> rechercherEvenementParId(int idEvent) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String req = "SELECT * FROM evenement WHERE idEvent=?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, idEvent);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Evenement e = new Evenement(
                    rs.getInt("idEvent"),
                    rs.getString("nomEvent"),
                    rs.getTimestamp("dateEvent"),
                    rs.getString("lieuEvent"),
                    rs.getString("description"),
                    rs.getInt("capaciteMax"),
                    rs.getInt("pointsOfferts")
                );
                evenements.add(e);
            }
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche de l'événement : " + ex.getMessage());
            throw ex;
        }
        
        return evenements;
    }
    
    public List<Evenement> rechercherEvenementsParCritere(String critere) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String req = "SELECT * FROM evenement WHERE nomEvent LIKE ? OR lieuEvent LIKE ? OR description LIKE ?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            String searchPattern = "%" + critere + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Evenement e = new Evenement(
                    rs.getInt("idEvent"),
                    rs.getString("nomEvent"),
                    rs.getTimestamp("dateEvent"),
                    rs.getString("lieuEvent"),
                    rs.getString("description"),
                    rs.getInt("capaciteMax"),
                    rs.getInt("pointsOfferts")
                );
                evenements.add(e);
            }
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche des événements par critère : " + ex.getMessage());
            throw ex;
        }
        
        return evenements;
    }
    
    public void inscrireUtilisateurEvenement(int idUtilisateur, int idEvent) throws SQLException {
        String req = "INSERT INTO participation (idUtilisateur, idEvent) VALUES (?, ?)";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, idUtilisateur);
            pst.setInt(2, idEvent);
            
            pst.executeUpdate();
            System.out.println("Utilisateur inscrit à l'événement avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'inscription à l'événement : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void desinscrireUtilisateurEvenement(int idUtilisateur, int idEvent) throws SQLException {

    }

    @Override
    public List<Utilisateur> getParticipantsEvenement(int idEvent) throws SQLException {
        return List.of();
    }

    @Override
    public List<Evenement> getEvenementsUtilisateur(int idUtilisateur) throws SQLException {
        return List.of();
    }

    @Override
    public boolean estInscrit(int idUtilisateur, int idEvent) throws SQLException {
        return false;
    }

    @Override
    public int compterParticipants(int idEvent) throws SQLException {
        return 0;
    }

    @Override
    public List<Evenement> getEvenementsAVenir() throws SQLException {
        return List.of();
    }
}
