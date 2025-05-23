package com.example.fakestore.repository;

import com.example.fakestore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.productId = :productId")
    Optional<Product> findByProductId(Long productId);

    @Query("SELECT p FROM Product p")
    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE (:partialName IS NULL OR LOCATE(:partialName, p.title) <> 0)" +
            "AND (:categoryName IS NULL OR p.category.name = :categoryName)")
    Page<Product> findProducts(Pageable pageable, String partialName, String categoryName);
}
