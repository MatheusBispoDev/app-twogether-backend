package com.app.us_twogether.domain.reminder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    String Query = "SELECT new com.app.us_twogether.domain.reminder.ReminderResponseDTO(" +
            "reminder.reminderId, reminder.userCreation.username, " +
            "category.categoryId, category.title, category.color, " +
            "subCategory.subCategoryId, subCategory.title, subCategory.color, " +
            "reminder.title, reminder.description, reminder.dateCreation, reminder.timeCreation, " +
            "reminder.dateCompletion, reminder.timeCompletion, reminder.completed) " +
            "FROM Reminder reminder " +
            "WHERE reminder.space.spaceId = :spaceId ";


    @Query(Query)
    List<ReminderResponseDTO> findBySpace(@Param("spaceId") Long spaceId);

    @Query(Query + "AND reminder.dateCompletion = :dateCompletion")
    List<ReminderResponseDTO> findBySpaceAndDate(@Param("spaceId") Long spaceId, @Param("dateCompletion") LocalDate dateCompletion);
}
