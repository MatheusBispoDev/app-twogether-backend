package com.app.us_twogether.domain.task;

import java.time.LocalDate;
import java.time.LocalTime;

public record TaskRequestDTO(String userResponsible,
                             Long categoryId, Long subCategoryId,
                             String title, String description, String observation, LocalDate dateCompletion,
                             LocalTime timeCompletion, LocalDate dateEnd, LocalTime timeEnd, String attachment,
                             boolean completed) {
}
