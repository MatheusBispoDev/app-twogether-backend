package com.app.us_twogether.domain.category;

import com.app.us_twogether.domain.category.subCategory.SubCategory;
import com.app.us_twogether.domain.space.Space;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategory> subCategories = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String color;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;
}
