package com.greencore.model.repository;

import com.greencore.model.entity.Materiel;
import com.greencore.model.entity.Status;
import com.greencore.model.entity.User;
import com.greencore.model.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class MaterielRepository extends BaseRepository<Materiel, Long> {
    
    public MaterielRepository() {
        super(Materiel.class);
    }
    
    public List<Materiel> findByOwner(User owner) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Materiel> query = em.createQuery(
                "SELECT m FROM Materiel m WHERE m.owner = :owner", Materiel.class);
            query.setParameter("owner", owner);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Materiel> findByCategory(Category category) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Materiel> query = em.createQuery(
                "SELECT m FROM Materiel m WHERE m.category = :category", Materiel.class);
            query.setParameter("category", category);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Materiel> findByStatus(Status status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Materiel> query = em.createQuery(
                "SELECT m FROM Materiel m WHERE m.status = :status", Materiel.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Materiel> findAvailable() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Materiel> query = em.createQuery(
                "SELECT m FROM Materiel m WHERE m.availableQuantity > 0 AND m.visible = true", Materiel.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Materiel> findVisible() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Materiel> query = em.createQuery(
                "SELECT m FROM Materiel m WHERE m.visible = true ORDER BY m.name", Materiel.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Materiel> searchByName(String searchTerm) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Materiel> query = em.createQuery(
                "SELECT m FROM Materiel m WHERE m.name LIKE :searchTerm AND m.visible = true", Materiel.class);
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Optional<Materiel> findByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Materiel> query = em.createQuery(
                "SELECT m FROM Materiel m WHERE m.name = :name", Materiel.class);
            query.setParameter("name", name);
            List<Materiel> materiels = query.getResultList();
            return materiels.isEmpty() ? Optional.empty() : Optional.of(materiels.get(0));
        } finally {
            em.close();
        }
    }
}
