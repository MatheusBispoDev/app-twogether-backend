package com.app.us_twogether.domain.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT task " +
            "FROM Task task " +
                "JOIN task.category category " +
                "JOIN task.subCategory subCategory " +
            "WHERE task.space.spaceId = :spaceId " +
                "AND task.dateCompletion = :dateCompletion")
    List<Task> findBySpaceAndDate(@Param("spaceId") Long spaceId, @Param("dateCompletion") LocalDate dateCompletion);
}
