package com.app.us_twogether.domain.category.subCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    @Query("SELECT subCategory FROM SubCategory subCategory WHERE subCategory.category.categoryId = :categoryId AND subCategory.subCategoryId = :subCategoryId ")
    Optional<SubCategory> getSubCategoryFromCategory(@Param("categoryId") Long categoryId, @Param("subCategoryId") Long subCategoryId);
    @Query("SELECT subCategory FROM SubCategory subCategory WHERE subCategory.category.categoryId = :categoryId ")
    List<SubCategory> getAllSubCategoriesFromCategory(@Param("categoryId") Long categoryId);
}
