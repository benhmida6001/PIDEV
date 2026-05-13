package com.greencore.model.repository;

import com.greencore.model.entity.Operation;
import com.greencore.model.entity.OperationStatus;
import com.greencore.model.entity.OperationType;
import com.greencore.model.entity.User;
import com.greencore.model.entity.Materiel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class OperationRepository extends BaseRepository<Operation, Long> {
    
    public OperationRepository() {
        super(Operation.class);
    }
    
    public List<Operation> findByRequester(User requester) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Operation> query = em.createQuery(
                "SELECT o FROM Operation o WHERE o.requester = :requester ORDER BY o.createdAt DESC", Operation.class);
            query.setParameter("requester", requester);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Operation> findByMateriel(Materiel materiel) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Operation> query = em.createQuery(
                "SELECT o FROM Operation o WHERE o.materiel = :materiel ORDER BY o.createdAt DESC", Operation.class);
            query.setParameter("materiel", materiel);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Operation> findByStatus(OperationStatus status) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Operation> query = em.createQuery(
                "SELECT o FROM Operation o WHERE o.status = :status ORDER BY o.createdAt DESC", Operation.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Operation> findByType(OperationType type) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Operation> query = em.createQuery(
                "SELECT o FROM Operation o WHERE o.type = :type ORDER BY o.createdAt DESC", Operation.class);
            query.setParameter("type", type);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Operation> findPendingOperations() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Operation> query = em.createQuery(
                "SELECT o FROM Operation o WHERE o.status = :status ORDER BY o.createdAt ASC", Operation.class);
            query.setParameter("status", OperationStatus.PENDING);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Operation> findOperationsBetweenDates(LocalDate startDate, LocalDate endDate) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Operation> query = em.createQuery(
                "SELECT o FROM Operation o WHERE o.startDate >= :startDate AND o.endDate <= :endDate ORDER BY o.startDate", Operation.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Operation> findActiveOperations() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Operation> query = em.createQuery(
                "SELECT o FROM Operation o WHERE o.status = :status AND o.startDate <= :currentDate AND o.endDate >= :currentDate", Operation.class);
            query.setParameter("status", OperationStatus.APPROVED);
            query.setParameter("currentDate", LocalDate.now());
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
