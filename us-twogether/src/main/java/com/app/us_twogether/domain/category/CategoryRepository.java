package com.app.us_twogether.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT category FROM Category category " +
            "LEFT JOIN FETCH category.subCategories " +
            "WHERE category.space.spaceId = :spaceId")
    List<Category> getAllCategoriesFromSpace(@Param("spaceId") Long spaceId);
}
