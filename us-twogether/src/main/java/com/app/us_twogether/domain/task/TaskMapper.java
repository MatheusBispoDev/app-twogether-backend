package com.app.us_twogether.domain.task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponseDTO toResponseDTO(Task task){
        return new TaskResponseDTO(task.getTaskId(), task.getUserCreation().getUsername(), task.getUserResponsible().getUsername(),
                task.getCategory().getCategoryId(), task.getCategory().getTitle(), task.getCategory().getColor(),
                task.getSubCategory().getSubCategoryId(), task.getSubCategory().getTitle(), task.getSubCategory().getColor(),
                task.getTitle(), task.getDescription(), task.getObservation(),
                task.getDateCreation(), task.getTimeCreation(), task.getDateCompletion(), task.getTimeCompletion(),
                task.getDateEnd(), task.getTimeEnd(), task.getAttachment(), task.isCompleted());
    }

}
