package com.app.us_twogether.domain.category.subCategory;

import com.app.us_twogether.domain.category.Category;
import com.app.us_twogether.domain.reminder.Reminder;
import com.app.us_twogether.domain.task.Task;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tb_sub_category")
@Data
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "subCategory")
    private List<Task> tasks;

    @OneToMany(mappedBy = "subCategory")
    private List<Reminder> reminders;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String color;
}
