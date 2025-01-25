package com.app.us_twogether.domain.task;

import com.app.us_twogether.domain.category.Category;
import com.app.us_twogether.domain.category.subCategory.SubCategory;

import java.time.LocalDate;
import java.time.LocalTime;

public record TaskDTO(Long taskId, String userCreation, String userResponsible,
                      Long categoryId, String categoryTitle, String categoryColor,
                      Long subCategoryId, String subCategoryTitle, String subCategoryColor,
                      String title, String description, String observation, LocalDate dateCreation,
                      LocalTime timeCreation, LocalDate dateCompletion, LocalTime timeCompletion,
                      LocalDate dateEnd, LocalTime timeEnd, String attachment, boolean completed) { }
