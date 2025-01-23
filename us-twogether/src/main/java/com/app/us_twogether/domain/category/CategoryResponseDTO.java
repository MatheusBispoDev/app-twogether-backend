package com.app.us_twogether.domain.category;

import com.app.us_twogether.domain.category.subCategory.SubCategoryResponseDTO;

import java.util.List;

public record CategoryResponseDTO(Long categoryId, String title, String color, List<SubCategoryResponseDTO> subCategories) {
}
