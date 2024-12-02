package com.app.us_twogether.domain.space;

import com.app.us_twogether.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;

@Entity
@Table(name = "tb_user_space_role")
public class UserSpaceRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSpaceRoleId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "space_id")
    private Space space;

    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;
}
