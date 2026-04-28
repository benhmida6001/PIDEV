package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private int id;
    private int idUtilisateurEmetteur;
    private int idUtilisateurRecepteur;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private boolean lu;
    
    // Informations additionnelles pour l'affichage
    private String nomEmetteur;
    private String prenomEmetteur;
    
    public Message() {
        this.dateEnvoi = LocalDateTime.now();
        this.lu = false;
    }
    
    public Message(int idUtilisateurEmetteur, int idUtilisateurRecepteur, String contenu) {
        this();
        this.idUtilisateurEmetteur = idUtilisateurEmetteur;
        this.idUtilisateurRecepteur = idUtilisateurRecepteur;
        this.contenu = contenu;
    }
    
    public Message(int id, int idUtilisateurEmetteur, int idUtilisateurRecepteur, 
                  String contenu, LocalDateTime dateEnvoi, boolean lu) {
        this.id = id;
        this.idUtilisateurEmetteur = idUtilisateurEmetteur;
        this.idUtilisateurRecepteur = idUtilisateurRecepteur;
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
        this.lu = lu;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdUtilisateurEmetteur() {
        return idUtilisateurEmetteur;
    }
    
    public void setIdUtilisateurEmetteur(int idUtilisateurEmetteur) {
        this.idUtilisateurEmetteur = idUtilisateurEmetteur;
    }
    
    public int getIdUtilisateurRecepteur() {
        return idUtilisateurRecepteur;
    }
    
    public void setIdUtilisateurRecepteur(int idUtilisateurRecepteur) {
        this.idUtilisateurRecepteur = idUtilisateurRecepteur;
    }
    
    public String getContenu() {
        return contenu;
    }
    
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }
    
    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
    
    public boolean isLu() {
        return lu;
    }
    
    public void setLu(boolean lu) {
        this.lu = lu;
    }
    
    public String getNomEmetteur() {
        return nomEmetteur;
    }
    
    public void setNomEmetteur(String nomEmetteur) {
        this.nomEmetteur = nomEmetteur;
    }
    
    public String getPrenomEmetteur() {
        return prenomEmetteur;
    }
    
    public void setPrenomEmetteur(String prenomEmetteur) {
        this.prenomEmetteur = prenomEmetteur;
    }
    
    // Méthodes utilitaires
    public String getNomCompletEmetteur() {
        if (nomEmetteur != null && prenomEmetteur != null) {
            return prenomEmetteur + " " + nomEmetteur;
        }
        return "Utilisateur inconnu";
    }
    
    public String getDateFormatee() {
        if (dateEnvoi != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return dateEnvoi.format(formatter);
        }
        return "";
    }
    
    public String getDateCompleteFormatee() {
        if (dateEnvoi != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return dateEnvoi.format(formatter);
        }
        return "";
    }
    
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", idUtilisateurEmetteur=" + idUtilisateurEmetteur +
                ", idUtilisateurRecepteur=" + idUtilisateurRecepteur +
                ", contenu='" + contenu + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", lu=" + lu +
                '}';
    }
}
