package com.app.us_twogether.domain.category;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getCategoryId(),
                category.getTitle(),
                category.getColor(),
                category.getSubCategories().stream()
                        .map(this::toSubCategoryDTO)
                        .toList()
        );
    }

    public SubCategoryDTO toSubCategoryDTO(SubCategory subCategory) {
        return new SubCategoryDTO(
                subCategory.getSubCategoryId(),
                subCategory.getTitle(),
                subCategory.getColor()
        );
    }
}
