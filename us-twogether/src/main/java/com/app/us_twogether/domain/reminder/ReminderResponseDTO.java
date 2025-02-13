package com.app.us_twogether.domain.reminder;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReminderResponseDTO(Long reminderId, String userCreation,
                                  Long categoryId, String categoryTitle, String categoryColor,
                                  Long subCategoryId, String subCategoryTitle, String subCategoryColor,
                                  String title, String description, LocalDate dateCreation, LocalTime timeCreation,
                                  LocalDate dateCompletion, LocalTime timeCompletion, boolean completed) { }
