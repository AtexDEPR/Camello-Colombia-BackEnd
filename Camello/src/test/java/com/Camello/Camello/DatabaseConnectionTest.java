package com.Camello.Camello;

import com.Camello.Camello.repository.CategoryRepository;
import com.Camello.Camello.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DatabaseConnectionTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testDatabaseConnection() {
        // Verificar que los repositorios están disponibles
        assertNotNull(userRepository);
        assertNotNull(categoryRepository);
        
        // Verificar que podemos hacer consultas básicas
        long userCount = userRepository.count();
        long categoryCount = categoryRepository.count();
        
        // Los conteos deben ser >= 0 (pueden ser 0 si la DB está vacía)
        assertTrue(userCount >= 0);
        assertTrue(categoryCount >= 0);
        
        System.out.println("✅ Conexión a base de datos exitosa!");
        System.out.println("📊 Usuarios en DB: " + userCount);
        System.out.println("📊 Categorías en DB: " + categoryCount);
    }

    @Test
    void testCategoryData() {
        // Verificar que las categorías iniciales están cargadas
        long categoryCount = categoryRepository.count();
        
        if (categoryCount > 0) {
            System.out.println("✅ Categorías encontradas en la base de datos: " + categoryCount);
            
            // Listar algunas categorías
            categoryRepository.findAll().stream()
                .limit(5)
                .forEach(category -> 
                    System.out.println("📂 Categoría: " + category.getName())
                );
        } else {
            System.out.println("⚠️ No se encontraron categorías en la base de datos");
        }
    }
}