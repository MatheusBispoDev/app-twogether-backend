package com.app.us_twogether.domain.notificationUser;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="tb_notification_users")
@Data
public class NotificationUser {
    @Id
    private int notificationUserId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;

    public NotificationUser() {}

    public NotificationUser(int notificationUserId, String title, String description) {
        this.notificationUserId = notificationUserId;
        this.title = title;
        this.description = description;
    }
}
