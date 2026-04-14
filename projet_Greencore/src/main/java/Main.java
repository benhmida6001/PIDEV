import services.UtilisateurService;
import services.EvenementService;
import models.Utilisateur;
import models.Evenement;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            UtilisateurService su = new UtilisateurService();
            EvenementService se = new EvenementService();

            Utilisateur u = new Utilisateur("Ali", "Mahad", "ali" + System.currentTimeMillis() + "@gmail.com", "12345678",
                    "Tunis", "client", "1234", 0);

            su.ajouterUtilisateur(u);
            
            // Ajouter un deuxième utilisateur
            Utilisateur u2 = new Utilisateur("Sarah", "Ben", "sarah" + System.currentTimeMillis() + "@gmail.com", "98765432",
                    "Sfax", "admin", "azerty", 50);

            su.ajouterUtilisateur(u2);

            Evenement e = new Evenement("Nettoyage plage", new Date(), "Sousse",
                    "Evenement écologique", 100, 20);

            se.ajouterEvenement(e);

            System.out.println(" LISTE DES UTILISATEURS");
            List<Utilisateur> utilisateurs = su.afficherUtilisateurs();
            for (Utilisateur utilisateur : utilisateurs) {
                System.out.println(utilisateur);
            }
            
            System.out.println(" LISTE DES ÉVÉNEMENTS");
            List<Evenement> evenements = se.afficherEvenements();
            for (Evenement evenement : evenements) {
                System.out.println(evenement);
            }
            
        } catch (Exception ex) {
            System.err.println("Erreur lors de l'exécution : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
