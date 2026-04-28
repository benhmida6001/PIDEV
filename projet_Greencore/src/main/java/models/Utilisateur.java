package models;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

    private int id;
    private String nom;
    private String prenom;
    private String adrEmail;
    private String numTel;
    private String adresse;
    private String role;
    private String mdp;
    private int pointGagne;

    // relation avec Evenement
    private List<Evenement> evenements;

    // constructeur vide
    public Utilisateur() {
        this.evenements = new ArrayList<>();
    }

    // constructeur sans id
    public Utilisateur(String nom, String prenom, String adrEmail, String numTel,
                       String adresse, String role, String mdp, int pointGagne) {
        this.nom = nom;
        this.prenom = prenom;
        this.adrEmail = adrEmail;
        this.numTel = numTel;
        this.adresse = adresse;
        this.role = role;
        this.mdp = mdp;
        this.pointGagne = pointGagne;
        this.evenements = new ArrayList<>();
    }

    // constructeur avec id
    public Utilisateur(int id, String nom, String prenom, String adrEmail, String numTel,
                       String adresse, String role, String mdp, int pointGagne) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adrEmail = adrEmail;
        this.numTel = numTel;
        this.adresse = adresse;
        this.role = role;
        this.mdp = mdp;
        this.pointGagne = pointGagne;
        this.evenements = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdrEmail() {
        return adrEmail;
    }

    public void setAdrEmail(String adrEmail) {
        this.adrEmail = adrEmail;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public int getPointGagne() {
        return pointGagne;
    }

    public void setPointGagne(int pointGagne) {
        this.pointGagne = pointGagne;
    }

    public List<Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
    }

    public void participerEvenement(Evenement e) {
        if (e != null && !evenements.contains(e)) {
            evenements.add(e);
        }
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adrEmail='" + adrEmail + '\'' +
                ", numTel='" + numTel + '\'' +
                ", adresse='" + adresse + '\'' +
                ", role='" + role + '\'' +
                ", pointGagne=" + pointGagne +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
