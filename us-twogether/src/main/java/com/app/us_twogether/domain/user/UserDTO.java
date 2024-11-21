package com.app.us_twogether.domain.user;

import com.app.us_twogether.model.NotificationUser;

public record UserDTO(String username,
         NotificationUser notificationUser,
         String password,
         String name,
         String email,
         String phoneNumber,
         String type) {}
