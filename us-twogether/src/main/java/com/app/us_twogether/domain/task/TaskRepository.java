package com.app.us_twogether.domain.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    final String cQuery = "SELECT new com.app.us_twogether.domain.task.TaskResponseDTO( " +
            "task.taskId, task.userCreation.username, task.userResponsible.username, " +
            "category.categoryId, category.title, category.color, " +
            "subCategory.subCategoryId, subCategory.title, subCategory.color, " +
            "task.title, task.description, task.observation, task.dateCreation, " +
            "task.timeCreation, task.dateCompletion, task.timeCompletion, task.dateEnd, " +
            "task.timeEnd, task.attachment, task.completed) " +
            "FROM Task task " +
            "JOIN task.category category " +
            "JOIN task.subCategory subCategory " +
            "WHERE task.space.spaceId = :spaceId ";

    @Query(cQuery)
    List<TaskResponseDTO> findBySpace(@Param("spaceId") Long spaceId);

    @Query(cQuery + "AND task.dateCompletion = :dateCompletion")
    List<TaskResponseDTO> findBySpaceAndDate(@Param("spaceId") Long spaceId, @Param("dateCompletion") LocalDate dateCompletion);
}
