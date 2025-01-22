package com.app.us_twogether.repository.category;

import com.app.us_twogether.domain.category.Category;
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
    List<Category> findByAllCategorySpace(@Param("spaceId") Long spaceId);
}
