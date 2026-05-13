package com.greencore.model.service;

import com.greencore.model.entity.*;
import com.greencore.model.repository.MaterielRepository;
import java.util.*;

public class MaterielService {
    private final MaterielRepository materielRepository;
    
    public MaterielService() {
        this.materielRepository = new MaterielRepository();
    }
    
    public Materiel create(String name, String description, Integer quantity, Status status, 
                          Integer availableQuantity, Category category, User owner) {
        Materiel materiel = new Materiel(name, description, quantity, status, availableQuantity, category, owner);
        return materielRepository.save(materiel);
    }
    
    public List<Materiel> findAllVisible() {
        return materielRepository.findVisible();
    }
    
    public List<Materiel> findAvailable() {
        return materielRepository.findAvailable();
    }
    
    public Optional<Materiel> findById(Long id) {
        return materielRepository.findById(id);
    }
    
    public Materiel update(Materiel materiel) {
        return materielRepository.save(materiel);
    }
    
    public void delete(Long id) {
        materielRepository.deleteById(id);
    }
}
