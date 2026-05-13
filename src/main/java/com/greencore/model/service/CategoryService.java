package com.greencore.model.service;

import com.greencore.model.entity.Category;
import com.greencore.model.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;

public class CategoryService {
    private final CategoryRepository categoryRepository;
    
    public CategoryService() {
        this.categoryRepository = new CategoryRepository();
    }
    
    public Category create(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
    
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    public Category update(Category category) {
        return categoryRepository.save(category);
    }
    
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}