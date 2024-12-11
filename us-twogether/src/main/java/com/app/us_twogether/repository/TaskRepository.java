package com.app.us_twogether.repository;

import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.task.Task;
import com.app.us_twogether.domain.task.TaskDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT new com.app.us_twogether.domain.task.TaskDTO(task.taskId, task.userCreation.username, task.userResponsible.username, task.title, " +
            "task.description, task.observation, task.dateCreation, task.timeCreation, task.dateCompletion, task.timeCompletion, " +
            "task.dateEnd, task.timeEnd, task.attachment, task.completed) " +
            "FROM Task task " +
            "WHERE task.spaceId = :space AND task.dateCompletion = :dateCompletion")
    Optional<List<TaskDTO>> findBySpaceAndDate(@Param("space") Space space, @Param("dateCompletion") LocalDate dateCompletion);
}
