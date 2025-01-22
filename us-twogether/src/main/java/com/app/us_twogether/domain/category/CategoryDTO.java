package com.app.us_twogether.domain.category;

import java.util.List;

public record CategoryDTO(Long categoryId, List<SubCategoryDTO> subCategories, String title, String color) {
}
