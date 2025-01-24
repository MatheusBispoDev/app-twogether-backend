package com.app.us_twogether.domain.category;

import jakarta.validation.constraints.NotNull;

public record CategoryRequestDTO(String title,
                                 String color,
                                 @NotNull(message = "CategoryType cannot be null")
                                 CategoryType categoryType) {
}
