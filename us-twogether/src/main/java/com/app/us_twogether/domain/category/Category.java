package com.app.us_twogether.domain.category;

import com.app.us_twogether.domain.space.Space;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "space_id")
    private Space spaceId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String typeCategory;

    @Column(nullable = false)
    private String color;
}
