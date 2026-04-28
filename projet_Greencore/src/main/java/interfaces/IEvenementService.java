package interfaces;

import models.Evenement;
import models.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public interface IEvenementService {
    
    void ajouterEvenement(Evenement evenement) throws SQLException;
    List<Evenement> afficherEvenements() throws SQLException;
    void modifierEvenement(Evenement evenement) throws SQLException;
    void supprimerEvenement(int idEvent) throws SQLException;
    Evenement rechercherEvenementParId(int idEvent) throws SQLException;
    List<Evenement> rechercherEvenementsParCritere(String critere) throws SQLException;
    void inscrireUtilisateurEvenement(int idUtilisateur, int idEvent) throws SQLException;
    void desinscrireUtilisateurEvenement(int idUtilisateur, int idEvent) throws SQLException;
    List<Utilisateur> getParticipantsEvenement(int idEvent) throws SQLException;
    List<Evenement> getEvenementsUtilisateur(int idUtilisateur) throws SQLException;
    boolean estInscrit(int idUtilisateur, int idEvent) throws SQLException;
    int compterParticipants(int idEvent) throws SQLException;
    List<Evenement> getEvenementsAVenir() throws SQLException;
}
