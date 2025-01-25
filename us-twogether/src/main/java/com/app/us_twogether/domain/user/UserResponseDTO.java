package com.app.us_twogether.domain.user;

public record UserResponseDTO(String username,
                              String name,
                              String email,
                              String phoneNumber,
                              String type) { }
