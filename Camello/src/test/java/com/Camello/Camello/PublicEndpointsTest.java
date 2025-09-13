package com.Camello.Camello;

import com.Camello.Camello.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PublicEndpointsTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void testCategoryService() {
        // Verificar que el servicio está disponible
        assertNotNull(categoryService);
        
        // Verificar que podemos obtener categorías
        var categories = categoryService.getAllActiveCategories();
        assertNotNull(categories);
        assertEquals(10, categories.size());
        
        System.out.println("✅ CategoryService funciona correctamente");
        System.out.println("📊 Categorías activas: " + categories.size());
        
        // Mostrar algunas categorías
        categories.stream()
            .limit(3)
            .forEach(category -> 
                System.out.println("📂 " + category.getName() + " - " + category.getDescription())
            );
    }
}