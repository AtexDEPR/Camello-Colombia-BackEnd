package com.Camello.Camello.service;

import com.Camello.Camello.entity.Category;
import com.Camello.Camello.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public List<Category> getAllActiveCategories() {
        return categoryRepository.findByIsActiveTrue();
    }
    
    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }
    
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("La categoría ya existe");
        }
        category.setIsActive(true);
        return categoryRepository.save(category);
    }
    
    public Category updateCategory(UUID id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        category.setIcon(categoryDetails.getIcon());
        
        return categoryRepository.save(category);
    }
    
    public void deactivateCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        category.setIsActive(false);
        categoryRepository.save(category);
    }
    
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }
    
    public Category getCategoryByName(String name) {
        return categoryRepository.findByNameAndIsActiveTrue(name);
    }
} 