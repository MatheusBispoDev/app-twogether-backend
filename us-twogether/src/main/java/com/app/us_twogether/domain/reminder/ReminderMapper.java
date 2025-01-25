package com.app.us_twogether.domain.reminder;

import org.springframework.stereotype.Component;

@Component
public class ReminderMapper {

    public ReminderResponseDTO toResponseDTO(Reminder reminder) {
        return new ReminderResponseDTO(reminder.getRemindersId(), reminder.getUserCreation().getUsername(),
                reminder.getCategory().getCategoryId(), reminder.getCategory().getTitle(), reminder.getCategory().getColor(),
                reminder.getSubCategory().getSubCategoryId(), reminder.getSubCategory().getTitle(), reminder.getSubCategory().getColor(),
                reminder.getTitle(), reminder.getDescription(),
                reminder.getDateCreation(), reminder.getTimeCreation(), reminder.getDateCompletion(),
                reminder.getTimeCompletion(), reminder.isCompleted());
    }

}
