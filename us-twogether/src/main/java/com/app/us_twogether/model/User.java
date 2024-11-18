package com.app.us_twogether.model;

import com.app.us_twogether.config.DefaultNotificationUserLoader;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_users")
@Data
@EqualsAndHashCode(of = "username")
public class User implements UserDetails {
    @Id
    @Column(nullable = false, unique = true)
    private String username;
    @ManyToOne
    @JoinColumn(name = "notification_user_id")
    private NotificationUser notificationUser;
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

    public User() {}

    public User(String username, String password, String name, String email, String phoneNumber, String type){
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    @PrePersist
    public void setDefaultNotificationUser() {
        if (this.notificationUser == null) {
            this.notificationUser = DefaultNotificationUserLoader.getDefaultNotificationUser();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        //Todo: Fazer validação
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        //Todo: Fazer validação
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //Todo: Fazer validação
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        //Todo: Fazer validação
        return UserDetails.super.isEnabled();
    }
}
