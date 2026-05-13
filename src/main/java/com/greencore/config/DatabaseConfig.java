package com.greencore.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConfig {
    private static final String PERSISTENCE_UNIT_NAME = "greencore-pu";
    private static EntityManagerFactory entityManagerFactory;
    
    static {
        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        properties.put("jakarta.persistence.jdbc.url", "jdbc:mysql://127.0.0.1:3306/pidev_greencore?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        properties.put("jakarta.persistence.jdbc.user", "root");
        properties.put("jakarta.persistence.jdbc.password", "");
        properties.put("eclipselink.logging.level", "FINE");
        properties.put("eclipselink.ddl-generation", "create-or-extend-tables");
        properties.put("eclipselink.ddl-generation.output-mode", "database");
        
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
    }
    
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
