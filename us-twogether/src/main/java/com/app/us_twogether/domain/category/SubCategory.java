package com.app.us_twogether.domain.category;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_sub_category")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryId;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String color;
}
