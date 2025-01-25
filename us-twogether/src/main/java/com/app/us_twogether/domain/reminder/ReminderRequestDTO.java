package com.app.us_twogether.domain.reminder;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReminderRequestDTO(Long categoryId, Long subCategoryId,
                                 String title, String description,
                                 LocalDate dateCompletion, LocalTime timeCompletion, boolean completed) { }
