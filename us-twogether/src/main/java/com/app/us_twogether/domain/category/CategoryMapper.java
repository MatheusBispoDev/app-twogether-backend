package com.app.us_twogether.domain.category;

import com.app.us_twogether.domain.category.subCategory.SubCategory;
import com.app.us_twogether.domain.category.subCategory.SubCategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getCategoryId(),
                category.getTitle(),
                category.getColor(),
                category.getSubCategories().stream()
                        .map(this::toSubCategoryResponseDTO)
                        .toList()
        );
    }

    public SubCategoryResponse toSubCategoryResponseDTO(SubCategory subCategory) {
        return new SubCategoryResponse(
                subCategory.getSubCategoryId(),
                subCategory.getTitle(),
                subCategory.getColor()
        );
    }
}
