package com.greencore.model.repository;

import com.greencore.config.DatabaseConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class BaseRepository<T, ID> {
    private final Class<T> entityClass;
    
    public BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    protected EntityManager getEntityManager() {
        return DatabaseConfig.getEntityManager();
    }
    
    public T save(T entity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (em.contains(entity)) {
                entity = em.merge(entity);
            } else {
                em.persist(entity);
            }
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public Optional<T> findById(ID id) {
        EntityManager em = getEntityManager();
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } finally {
            em.close();
        }
    }
    
    public List<T> findAll() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public void delete(T entity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (!em.contains(entity)) {
                entity = em.merge(entity);
            }
            em.remove(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public void deleteById(ID id) {
        Optional<T> entity = findById(id);
        if (entity.isPresent()) {
            delete(entity.get());
        }
    }
    
    public long count() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
