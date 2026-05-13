package com.greencore.model.repository;

import com.greencore.model.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CategoryRepository extends BaseRepository<Category, Long> {
    
    public CategoryRepository() {
        super(Category.class);
    }
    
    public Optional<Category> findByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c WHERE c.name = :name", Category.class);
            query.setParameter("name", name);
            List<Category> categories = query.getResultList();
            return categories.isEmpty() ? Optional.empty() : Optional.of(categories.get(0));
        } finally {
            em.close();
        }
    }
    
    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }
    
    public List<Category> findAllOrderByName() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c ORDER BY c.name", Category.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
