package com.app.us_twogether.model;

import com.app.us_twogether.config.DefaultNotificationUserLoader;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_users")
@Data
public class User {
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
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false)
    private String type;

    @PrePersist
    public void setDefaultNotificationUser() {
        if (this.notificationUser == null) {
            this.notificationUser = DefaultNotificationUserLoader.getDefaultNotificationUser();
        }
    }

}
