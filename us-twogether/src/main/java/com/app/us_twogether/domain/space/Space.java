package com.app.us_twogether.domain.space;

import com.app.us_twogether.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private List<User> users = new ArrayList<>();

    public Space() {}

    public Space(String name, String token, List<User> users){
        this.name = name;
        this.sharedToken = token;
        this.users = users;
    }
}
