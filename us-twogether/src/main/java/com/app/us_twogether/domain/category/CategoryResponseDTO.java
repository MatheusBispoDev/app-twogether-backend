package com.app.us_twogether.domain.category;

import java.util.List;

public record CategoryResponseDTO(Long categoryId, String title, String color, List<SubCategoryDTO> subCategories) {
}
