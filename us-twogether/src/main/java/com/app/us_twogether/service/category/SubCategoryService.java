package com.app.us_twogether.service.category;

import com.app.us_twogether.domain.category.Category;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_sub_category")
@Data
public class SubCategoryService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String title;

    private String color;
}
