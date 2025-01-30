package com.app.us_twogether.domain.authentication;

import java.time.Instant;

public record LoginResponseDTO(String username, String accessToken, String refreshToken, Instant expiryDate) { }
