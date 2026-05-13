package com.greencore.model.repository;

import com.greencore.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UserRepository extends BaseRepository<User, Long> {
    
    public UserRepository() {
        super(User.class);
    }
    
    public Optional<User> findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            List<User> users = query.getResultList();
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } finally {
            em.close();
        }
    }
    
    public List<User> findByRole(String role) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE :role MEMBER OF u.roles", User.class);
            query.setParameter("role", role);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
