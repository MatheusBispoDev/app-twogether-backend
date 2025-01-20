package com.app.us_twogether.domain.reminder;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReminderDTO(Long remindersId, Long spaceId, String userCreation, String title, String description,
                          LocalDate dateCreation, LocalTime timeCreation, LocalDate dateCompletion,
                          LocalTime timeCompletion, boolean completed) {
}
