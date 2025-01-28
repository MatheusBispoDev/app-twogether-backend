package com.app.us_twogether.domain.authentication;

import com.app.us_twogether.domain.authentication.token.RefreshToken;
import com.app.us_twogether.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public LoginResponseDTO toLoginResponseDTO(User user, String accessToken, RefreshToken refreshToken) {
        return new LoginResponseDTO(user.getUsername(), accessToken,
                refreshToken.getToken(), refreshToken.getExpiryDate());
    }

}
