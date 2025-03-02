package com.example.fakestore.repository;

import com.example.fakestore.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Optional<Category> findByName(String name);

    @Query("SELECT c FROM Category c WHERE c.categoryId = :categoryId")
    Optional<Category> findByCategoryId(Long categoryId);

    @Query("SELECT c FROM Category c")
    Page<Category> findAll(Pageable pageable);
}
