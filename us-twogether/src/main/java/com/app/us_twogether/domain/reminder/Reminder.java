package com.app.us_twogether.domain.reminder;

import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tb_reminders")
@Data
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long remindersId;
    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;
    @ManyToOne
    @JoinColumn(name = "user_creation", nullable = false)
    private User userCreation;
    @JoinColumn(nullable = false)
    private String title;
    private String description;
    private LocalDate dateCreation;
    private LocalTime timeCreation;
    private LocalDate dateCompletion;
    private LocalTime timeCompletion;
    private boolean completed;
}
