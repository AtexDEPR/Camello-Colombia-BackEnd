package com.Camello.Camello.repository;

import com.Camello.Camello.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true")
    Page<Service> findAllActive(Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.freelancer.id = :freelancerId AND s.isActive = true")
    Page<Service> findByFreelancerId(@Param("freelancerId") UUID freelancerId, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.category.id = :categoryId AND s.isActive = true")
    Page<Service> findByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.price BETWEEN :minPrice AND :maxPrice AND s.isActive = true")
    Page<Service> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice, 
                                   Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.rating >= :minRating AND s.isActive = true")
    Page<Service> findByMinRating(@Param("minRating") BigDecimal minRating, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.isFeatured = true AND s.isActive = true")
    Page<Service> findFeatured(Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true ORDER BY s.rating DESC")
    Page<Service> findTopRated(Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true ORDER BY s.ordersCount DESC")
    Page<Service> findMostOrdered(Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true ORDER BY s.viewsCount DESC")
    Page<Service> findMostViewed(Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE :tag MEMBER OF s.tags AND s.isActive = true")
    Page<Service> findByTag(@Param("tag") String tag, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.title ILIKE %:searchTerm% OR s.description ILIKE %:searchTerm% AND s.isActive = true")
    Page<Service> searchByTitleOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);
} 