package com.app.us_twogether.domain.category;

import com.app.us_twogether.domain.category.subCategory.SubCategory;
import com.app.us_twogether.domain.category.subCategory.SubCategoryResponseDTO;
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

    public SubCategoryResponseDTO toSubCategoryResponseDTO(SubCategory subCategory) {
        return new SubCategoryResponseDTO(
                subCategory.getSubCategoryId(),
                subCategory.getTitle(),
                subCategory.getColor()
        );
    }
}
