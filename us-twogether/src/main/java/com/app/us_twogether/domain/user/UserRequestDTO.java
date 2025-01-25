package com.app.us_twogether.domain.user;

public record UserRequestDTO(String username,
                             String password,
                             String name,
                             String email,
                             String phoneNumber,
                             String type) { }
