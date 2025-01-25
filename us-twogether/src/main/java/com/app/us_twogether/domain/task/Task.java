package com.app.us_twogether.domain.task;

import com.app.us_twogether.domain.category.Category;
import com.app.us_twogether.domain.category.subCategory.SubCategory;
import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tb_task")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @ManyToOne
    @JoinColumn(name = "user_creation", nullable = false)
    private User userCreation;

    @ManyToOne
    @JoinColumn(name = "user_responsible", nullable = false)
    private User userResponsible;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;

    @Column(nullable = false)
    private String title;

    private String description;

    private String observation;

    @Column(nullable = false)
    private LocalDate dateCreation;

    @Column(nullable = false)
    private LocalTime timeCreation;

    private LocalDate dateCompletion;

    private LocalTime timeCompletion;

    private LocalDate dateEnd;

    private LocalTime timeEnd;

    private String attachment;

    private boolean completed = false;
}
