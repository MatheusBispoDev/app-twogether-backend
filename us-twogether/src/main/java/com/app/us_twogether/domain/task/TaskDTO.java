package com.app.us_twogether.domain.task;

import com.app.us_twogether.domain.user.User;

import java.time.LocalDate;
import java.time.LocalTime;

public record TaskDTO(Long taskId, String userCreation, String userResponsible, String title,
                      String description, String observation, LocalDate dateCreation, LocalTime timeCreation,
                      LocalDate dateCompletion, LocalTime timeCompletion, LocalDate dateEnd, LocalTime timeEnd,
                      String attachment, boolean completed) {}
