package com.app.us_twogether.domain.authentication;

import com.app.us_twogether.domain.authentication.token.RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public LoginResponseDTO toLoginResponseDTO(String username, String accessToken, RefreshToken refreshToken) {
        return new LoginResponseDTO(username, accessToken,
                refreshToken.getToken(), refreshToken.getExpiryDate());
    }

}
