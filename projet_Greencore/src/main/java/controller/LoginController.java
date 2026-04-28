package controller;

import models.Utilisateur;
import services.UtilisateurService;
import interfaces.IUtilisateurService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class LoginController {
    
    @FXML
    private TextField tfEmail;
    
    @FXML
    private PasswordField pfMotDePasse;
    
    @FXML
    private Label lblMessage;
    
    @FXML
    private Button btnConnexion;
    
    @FXML
    private Button btnEffacer;
    
    private IUtilisateurService utilisateurService;
    private Utilisateur utilisateurConnecte;
    
    public LoginController() {
        this.utilisateurService = new UtilisateurService();
        this.utilisateurConnecte = null;
    }
    
    /**
     * Authentifie un utilisateur avec email et mot de passe
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     * @return true si authentification réussie, false sinon
     */
    public boolean authentifier(String email, String motDePasse) {
        try {
            // Validation des entrées
            if (email == null || email.trim().isEmpty() || motDePasse == null || motDePasse.trim().isEmpty()) {
                System.err.println("Email et mot de passe sont obligatoires");
                return false;
            }
            
            List<Utilisateur> utilisateurs = utilisateurService.authentifier(email.trim(), motDePasse);
            
            if (!utilisateurs.isEmpty()) {
                this.utilisateurConnecte = utilisateurs.get(0);
                System.out.println("Authentification réussie pour : " + utilisateurConnecte.getNom() + " " + utilisateurConnecte.getPrenom());
                return true;
            } else {
                System.err.println("Email ou mot de passe incorrect");
                return false;
            }
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'authentification : " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Vérifie si un utilisateur est actuellement connecté
     * @return true si un utilisateur est connecté, false sinon
     */
    public boolean estConnecte() {
        return utilisateurConnecte != null;
    }
    
    /**
     * Retourne l'utilisateur actuellement connecté
     * @return L'utilisateur connecté ou null si personne n'est connecté
     */
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    
    /**
     * Déconnecte l'utilisateur actuellement connecté
     */
    public void deconnecter() {
        if (utilisateurConnecte != null) {
            System.out.println("Déconnexion de : " + utilisateurConnecte.getNom() + " " + utilisateurConnecte.getPrenom());
            utilisateurConnecte = null;
        }
    }
    
    /**
     * Vérifie si l'utilisateur connecté a un rôle spécifique
     * @param role Le rôle à vérifier
     * @return true si l'utilisateur a le rôle spécifié, false sinon
     */
    public boolean aRole(String role) {
        if (!estConnecte()) {
            return false;
        }
        return role.equalsIgnoreCase(utilisateurConnecte.getRole());
    }
    
    /**
     * Vérifie si l'utilisateur connecté est un administrateur
     * @return true si l'utilisateur est admin, false sinon
     */
    public boolean estAdmin() {
        return aRole("ADMIN") || aRole("ADMINISTRATEUR");
    }
    
    /**
     * Vérifie si l'utilisateur connecté est un utilisateur standard
     * @return true si l'utilisateur est un utilisateur normal, false sinon
     */
    public boolean estUtilisateur() {
        return aRole("USER") || aRole("UTILISATEUR");
    }
    
    /**
     * Crée un nouveau compte utilisateur
     * @param nom Nom de l'utilisateur
     * @param prenom Prénom de l'utilisateur
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     * @param numTel Numéro de téléphone
     * @param adresse Adresse
     * @return true si création réussie, false sinon
     */
    public boolean creerCompte(String nom, String prenom, String email, String motDePasse, String numTel, String adresse) {
        try {
            // Validation des entrées
            if (nom == null || nom.trim().isEmpty() ||
                prenom == null || prenom.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                motDePasse == null || motDePasse.trim().isEmpty()) {
                
                System.err.println("Nom, prénom, email et mot de passe sont obligatoires");
                return false;
            }
            
            // Vérifier si l'email existe déjà
            if (((UtilisateurService) utilisateurService).emailExiste(email.trim())) {
                System.err.println("Cet email est déjà utilisé");
                return false;
            }
            
            // Validation basique de l'email
            if (!email.trim().contains("@") || !email.trim().contains(".")) {
                System.err.println("Format d'email invalide");
                return false;
            }
            
            // Créer le nouvel utilisateur avec le rôle USER par défaut
            Utilisateur nouvelUtilisateur = new Utilisateur(
                nom.trim(),
                prenom.trim(),
                email.trim(),
                numTel != null ? numTel.trim() : "",
                adresse != null ? adresse.trim() : "",
                "USER", // Rôle par défaut
                motDePasse,
                0 // Points par défaut
            );
            
            utilisateurService.ajouterUtilisateur(nouvelUtilisateur);
            System.out.println("Compte créé avec succès pour : " + nom + " " + prenom);
            return true;
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la création du compte : " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Modifie le mot de passe de l'utilisateur connecté
     * @param ancienMotDePasse Ancien mot de passe
     * @param nouveauMotDePasse Nouveau mot de passe
     * @return true si modification réussie, false sinon
     */
    public boolean modifierMotDePasse(String ancienMotDePasse, String nouveauMotDePasse) {
        if (!estConnecte()) {
            System.err.println("Aucun utilisateur connecté");
            return false;
        }
        
        try {
            // Vérifier l'ancien mot de passe
            List<Utilisateur> utilisateurs = utilisateurService.authentifier(
                utilisateurConnecte.getAdrEmail(), ancienMotDePasse);
            
            if (utilisateurs.isEmpty()) {
                System.err.println("Ancien mot de passe incorrect");
                return false;
            }
            
            // Validation du nouveau mot de passe
            if (nouveauMotDePasse == null || nouveauMotDePasse.trim().isEmpty()) {
                System.err.println("Le nouveau mot de passe ne peut pas être vide");
                return false;
            }
            
            if (nouveauMotDePasse.length() < 6) {
                System.err.println("Le mot de passe doit contenir au moins 6 caractères");
                return false;
            }
            
            // Mettre à jour le mot de passe
            utilisateurConnecte.setMdp(nouveauMotDePasse);
            utilisateurService.modifierUtilisateur(utilisateurConnecte);
            
            System.out.println("Mot de passe modifié avec succès");
            return true;
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la modification du mot de passe : " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Met à jour le profil de l'utilisateur connecté
     * @param nom Nouveau nom
     * @param prenom Nouveau prénom
     * @param numTel Nouveau numéro de téléphone
     * @param adresse Nouvelle adresse
     * @return true si modification réussie, false sinon
     */
    public boolean modifierProfil(String nom, String prenom, String numTel, String adresse) {
        if (!estConnecte()) {
            System.err.println("Aucun utilisateur connecté");
            return false;
        }
        
        try {
            // Validation des entrées
            if (nom == null || nom.trim().isEmpty() || prenom == null || prenom.trim().isEmpty()) {
                System.err.println("Nom et prénom sont obligatoires");
                return false;
            }
            
            // Mettre à jour les informations
            utilisateurConnecte.setNom(nom.trim());
            utilisateurConnecte.setPrenom(prenom.trim());
            utilisateurConnecte.setNumTel(numTel != null ? numTel.trim() : "");
            utilisateurConnecte.setAdresse(adresse != null ? adresse.trim() : "");
            
            utilisateurService.modifierUtilisateur(utilisateurConnecte);
            
            System.out.println("Profil modifié avec succès");
            return true;
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la modification du profil : " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Retourne le rôle de l'utilisateur connecté
     * @return Le rôle de l'utilisateur ou null si personne n'est connecté
     */
    public String getRoleUtilisateurConnecte() {
        return estConnecte() ? utilisateurConnecte.getRole() : null;
    }
    
    /**
     * Retourne les points de l'utilisateur connecté
     * @return Les points de l'utilisateur ou 0 si personne n'est connecté
     */
    public int getPointsUtilisateurConnecte() {
        return estConnecte() ? utilisateurConnecte.getPointGagne() : 0;
    }
    
    /**
     * Ajoute des points à l'utilisateur connecté
     * @param points Nombre de points à ajouter
     * @return true si succès, false sinon
     */
    public boolean ajouterPoints(int points) {
        if (!estConnecte()) {
            System.err.println("Aucun utilisateur connecté");
            return false;
        }
        
        if (points <= 0) {
            System.err.println("Les points doivent être positifs");
            return false;
        }
        
        try {
            utilisateurService.mettreAJourPoints(utilisateurConnecte.getId(), points);
            utilisateurConnecte.setPointGagne(utilisateurConnecte.getPointGagne() + points);
            
            System.out.println(points + " points ajoutés à " + utilisateurConnecte.getNom());
            return true;
            
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout des points : " + ex.getMessage());
            return false;
        }
    }
    
    // FXML Event Handlers
    @FXML
    private void handleConnexion() {
        String email = tfEmail.getText();
        String motDePasse = pfMotDePasse.getText();
        
        if (authentifier(email, motDePasse)) {
            lblMessage.setText("Connexion réussie !");
            System.out.println("Utilisateur connecté: " + utilisateurConnecte.getNom());
            
            // Naviguer vers le dashboard
            try {
                // Charger le dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                Parent dashboardRoot = loader.load();
                
                // Obtenir le contrôleur du dashboard
                DashboardController dashboardController = loader.getController();
                
                // Mettre à jour les infos utilisateur dans le dashboard
                if (utilisateurConnecte != null) {
                    // Mettre à jour les labels du dashboard avec les infos utilisateur
                    javafx.application.Platform.runLater(() -> {
                        try {
                            // Utiliser réflexion pour accéder aux labels privés
                            java.lang.reflect.Field labelUtilisateurField = DashboardController.class.getDeclaredField("labelUtilisateur");
                            labelUtilisateurField.setAccessible(true);
                            Label labelUtilisateur = (Label) labelUtilisateurField.get(dashboardController);
                            labelUtilisateur.setText(utilisateurConnecte.getNom() + " " + utilisateurConnecte.getPrenom());
                            
                            java.lang.reflect.Field labelRoleField = DashboardController.class.getDeclaredField("labelRole");
                            labelRoleField.setAccessible(true);
                            Label labelRole = (Label) labelRoleField.get(dashboardController);
                            labelRole.setText(utilisateurConnecte.getRole());
                        } catch (Exception e) {
                            System.err.println("Erreur lors de la mise à jour des infos utilisateur: " + e.getMessage());
                        }
                    });
                }
                
                // Obtenir la stage actuelle et changer la scène
                Stage stage = (Stage) btnConnexion.getScene().getWindow();
                Scene dashboardScene = new Scene(dashboardRoot);
                stage.setScene(dashboardScene);
                stage.setTitle("Greencore - Dashboard");
                stage.centerOnScreen();
                stage.show();
                
            } catch (IOException e) {
                System.err.println("Erreur lors du chargement du dashboard: " + e.getMessage());
                lblMessage.setText("Erreur de chargement du dashboard");
                e.printStackTrace();
            }
            
        } else {
            lblMessage.setText("Email ou mot de passe incorrect");
        }
    }
    
    @FXML
    private void handleEffacer() {
        tfEmail.clear();
        pfMotDePasse.clear();
        lblMessage.setText("");
    }
    
    @FXML
    private void handleMotDePasseOublie() {
        String email = tfEmail.getText();
        
        if (email == null || email.trim().isEmpty()) {
            lblMessage.setText("Veuillez entrer votre adresse email");
            return;
        }
        
        // Simuler l'envoi d'un email de réinitialisation
        lblMessage.setText("Email de réinitialisation envoyé à " + email);
        System.out.println("📧 Email de réinitialisation du mot de passe envoyé à: " + email);
        
        // Dans une vraie application, vous enverriez un email avec un lien de réinitialisation
        // Pour la démo, nous affichons simplement un message
    }
    
    // Getter pour le service (utile pour les tests)
    public IUtilisateurService getUtilisateurService() {
        return utilisateurService;
    }
}
