package com.app.us_twogether.domain.space;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_space")
@Data
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spaceId;

    private String name;
    @Column(unique = true)

    private String sharedToken;

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL)
    private List<UserSpaceRole> userSpaceRoles = new ArrayList<>();

    public Space() {}
}
