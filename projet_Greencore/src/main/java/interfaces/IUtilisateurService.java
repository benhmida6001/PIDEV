package interfaces;

import models.Utilisateur;
import java.sql.SQLException;
import java.util.List;

public interface IUtilisateurService {

    void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException;
    List<Utilisateur> afficherUtilisateurs() throws SQLException;
    void modifierUtilisateur(Utilisateur utilisateur) throws SQLException;
    void supprimerUtilisateur(int id) throws SQLException;
    List<Utilisateur> rechercherUtilisateurParId(int id) throws SQLException;
    List<Utilisateur> authentifier(String email, String motDePasse) throws SQLException;
    void mettreAJourPoints(int id, int points) throws SQLException;
}
