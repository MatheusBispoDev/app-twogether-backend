package com.app.us_twogether.domain.category.subCategory;

import com.app.us_twogether.domain.category.Category;
import jakarta.persistence.*;
import lombok.Data;

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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String color;
}
