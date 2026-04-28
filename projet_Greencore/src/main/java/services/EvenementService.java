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
        try {
            cnx = MyConnection.getInstance().getCnx();
        } catch (SQLException ex) {
            System.err.println("Erreur de connexion à la base de données : " + ex.getMessage());
            throw new RuntimeException("Impossible de se connecter à la base de données", ex);
        }
    }
    
    public void ajouterEvenement(Evenement evenement) throws SQLException {
        String req = "INSERT INTO evenement (nomEvent, dateEvent, lieuEvent, description, capaciteMax, pointsOfferts) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, evenement.getNomEvent());
            pst.setTimestamp(2, new Timestamp(evenement.getDateEvent().getTime()));
            pst.setString(3, evenement.getLieuEvent());
            pst.setString(4, evenement.getDescription());
            pst.setInt(5, evenement.getCapaciteMax());
            pst.setInt(6, evenement.getPointsOfferts());
            
            pst.executeUpdate();
            
            // Récupérer l'ID généré
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                evenement.setIdEvent(rs.getInt(1));
            }
            
            System.out.println("Événement ajouté avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout de l'événement : " + ex.getMessage());
            throw ex;
        }
    }
    
    public List<Evenement> afficherEvenements() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String req = "SELECT * FROM evenement ORDER BY dateEvent ASC";
        
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
            
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucun événement trouvé avec l'ID : " + evenement.getIdEvent());
            }
            
            System.out.println("Événement modifié avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la modification de l'événement : " + ex.getMessage());
            throw ex;
        }
    }
    
    public void supprimerEvenement(int idEvent) throws SQLException {
        // D'abord supprimer les participations associées
        String deleteParticipations = "DELETE FROM participation WHERE idEvent=?";
        String deleteEvent = "DELETE FROM evenement WHERE idEvent=?";
        
        try {
            cnx.setAutoCommit(false);
            
            // Supprimer les participations
            PreparedStatement pst1 = cnx.prepareStatement(deleteParticipations);
            pst1.setInt(1, idEvent);
            pst1.executeUpdate();
            
            // Supprimer l'événement
            PreparedStatement pst2 = cnx.prepareStatement(deleteEvent);
            pst2.setInt(1, idEvent);
            int rowsAffected = pst2.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Aucun événement trouvé avec l'ID : " + idEvent);
            }
            
            cnx.commit();
            System.out.println("Événement supprimé avec succès !");
            
        } catch (SQLException ex) {
            cnx.rollback();
            System.err.println("Erreur lors de la suppression de l'événement : " + ex.getMessage());
            throw ex;
        } finally {
            cnx.setAutoCommit(true);
        }
    }
    
    public Evenement rechercherEvenementParId(int idEvent) throws SQLException {
        String req = "SELECT * FROM evenement WHERE idEvent=?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, idEvent);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return new Evenement(
                    rs.getInt("idEvent"),
                    rs.getString("nomEvent"),
                    rs.getTimestamp("dateEvent"),
                    rs.getString("lieuEvent"),
                    rs.getString("description"),
                    rs.getInt("capaciteMax"),
                    rs.getInt("pointsOfferts")
                );
            }
            
            return null;
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche de l'événement : " + ex.getMessage());
            throw ex;
        }
    }
    
    public List<Evenement> rechercherEvenementsParCritere(String critere) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String req = "SELECT * FROM evenement WHERE nomEvent LIKE ? OR lieuEvent LIKE ? OR description LIKE ? ORDER BY dateEvent ASC";
        
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
        // Vérifier si l'événement existe et n'est pas complet
        Evenement evenement = rechercherEvenementParId(idEvent);
        if (evenement == null) {
            throw new SQLException("Événement non trouvé avec l'ID : " + idEvent);
        }
        
        if (evenement.estComplet()) {
            throw new SQLException("L'événement est complet");
        }
        
        // Vérifier si l'utilisateur est déjà inscrit
        if (estInscrit(idUtilisateur, idEvent)) {
            throw new SQLException("L'utilisateur est déjà inscrit à cet événement");
        }
        
        String req = "INSERT INTO participation (idUtilisateur, idEvent, dateInscription) VALUES (?, ?, NOW())";
        
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
        String req = "DELETE FROM participation WHERE idUtilisateur=? AND idEvent=?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, idUtilisateur);
            pst.setInt(2, idEvent);
            
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucune inscription trouvée pour cet utilisateur et cet événement");
            }
            
            System.out.println("Utilisateur désinscrit de l'événement avec succès !");
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la désinscription de l'événement : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Utilisateur> getParticipantsEvenement(int idEvent) throws SQLException {
        List<Utilisateur> participants = new ArrayList<>();
        String req = "SELECT u.* FROM utilisateur u " +
                    "JOIN participation p ON u.id = p.idUtilisateur " +
                    "WHERE p.idEvent = ? ORDER BY u.nom, u.prenom";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, idEvent);
            
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
                participants.add(u);
            }
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des participants : " + ex.getMessage());
            throw ex;
        }
        
        return participants;
    }

    @Override
    public List<Evenement> getEvenementsUtilisateur(int idUtilisateur) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String req = "SELECT e.* FROM evenement e " +
                    "JOIN participation p ON e.idEvent = p.idEvent " +
                    "WHERE p.idUtilisateur = ? ORDER BY e.dateEvent ASC";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, idUtilisateur);
            
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
            System.err.println("Erreur lors de la récupération des événements de l'utilisateur : " + ex.getMessage());
            throw ex;
        }
        
        return evenements;
    }

    @Override
    public boolean estInscrit(int idUtilisateur, int idEvent) throws SQLException {
        String req = "SELECT COUNT(*) FROM participation WHERE idUtilisateur=? AND idEvent=?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, idUtilisateur);
            pst.setInt(2, idEvent);
            
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la vérification de l'inscription : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public int compterParticipants(int idEvent) throws SQLException {
        String req = "SELECT COUNT(*) FROM participation WHERE idEvent=?";
        
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, idEvent);
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors du comptage des participants : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Evenement> getEvenementsAVenir() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String req = "SELECT * FROM evenement WHERE dateEvent >= NOW() ORDER BY dateEvent ASC";
        
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
            System.err.println("Erreur lors de la récupération des événements à venir : " + ex.getMessage());
            throw ex;
        }
        
        return evenements;
    }
}
