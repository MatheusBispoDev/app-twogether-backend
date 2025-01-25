package com.app.us_twogether.domain.task;

import java.time.LocalDate;
import java.time.LocalTime;

public record TaskResponseDTO(Long taskId, String userCreation, String userResponsible,
                              Long categoryId, String categoryTitle, String categoryColor,
                              Long subCategoryId, String subCategoryTitle, String subCategoryColor,
                              String title, String description, String observation, LocalDate dateCreation,
                              LocalTime timeCreation, LocalDate dateCompletion, LocalTime timeCompletion,
                              LocalDate dateEnd, LocalTime timeEnd, String attachment, boolean completed) { }
