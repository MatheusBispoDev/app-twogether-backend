package com.app.us_twogether.domain.user;

import com.app.us_twogether.domain.space.UserSpaceRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_users")
@Data
@EqualsAndHashCode(of = "username")
public class User implements UserDetails {
    //TODO: Alterar a chave primaria para Long e alterar o relacionamento do notificationUser para o UserSpaceRole
    @Id
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String type;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserSpaceRole> userSpaceRoles = new ArrayList<>();

    public User() {}

    public User(String username, String password, String name, String email, String phoneNumber, String type){
        this.username = username;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.setPassword(password);
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.name = "";
        this.email = "";
        this.phoneNumber = "";
        this.type = "";
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "read");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
