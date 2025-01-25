package com.app.us_twogether.domain.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(user.getUsername(), user.getName(),
                user.getEmail(), user.getPhoneNumber(), user.getType());
    }

}
