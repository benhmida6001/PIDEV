package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evenement {

    private int idEvent;
    private String nomEvent;
    private Date dateEvent;
    private String lieuEvent;
    private String description;
    private int capaciteMax;
    private int pointsOfferts;

    // relation avec Utilisateur
    private List<Utilisateur> participants;

    // constructeur vide
    public Evenement() {
        this.participants = new ArrayList<>();
    }

    // constructeur sans id
    public Evenement(String nomEvent, Date dateEvent, String lieuEvent,
                     String description, int capaciteMax, int pointsOfferts) {
        this.nomEvent = nomEvent;
        this.dateEvent = dateEvent;
        this.lieuEvent = lieuEvent;
        this.description = description;
        this.capaciteMax = capaciteMax;
        this.pointsOfferts = pointsOfferts;
        this.participants = new ArrayList<>();
    }

    // constructeur avec id
    public Evenement(int idEvent, String nomEvent, Date dateEvent, String lieuEvent,
                     String description, int capaciteMax, int pointsOfferts) {
        this.idEvent = idEvent;
        this.nomEvent = nomEvent;
        this.dateEvent = dateEvent;
        this.lieuEvent = lieuEvent;
        this.description = description;
        this.capaciteMax = capaciteMax;
        this.pointsOfferts = pointsOfferts;
        this.participants = new ArrayList<>();
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getNomEvent() {
        return nomEvent;
    }

    public void setNomEvent(String nomEvent) {
        this.nomEvent = nomEvent;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getLieuEvent() {
        return lieuEvent;
    }

    public void setLieuEvent(String lieuEvent) {
        this.lieuEvent = lieuEvent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public int getPointsOfferts() {
        return pointsOfferts;
    }

    public void setPointsOfferts(int pointsOfferts) {
        this.pointsOfferts = pointsOfferts;
    }

    public List<Utilisateur> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Utilisateur> participants) {
        this.participants = participants;
    }

    public void ajouterParticipant(Utilisateur u) {
        if (u != null && !participants.contains(u)) {
            participants.add(u);
        }
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "idEvent=" + idEvent +
                ", nomEvent='" + nomEvent + '\'' +
                ", dateEvent=" + dateEvent +
                ", lieuEvent='" + lieuEvent + '\'' +
                ", description='" + description + '\'' +
                ", capaciteMax=" + capaciteMax +
                ", pointsOfferts=" + pointsOfferts +
                '}';
    }
}
