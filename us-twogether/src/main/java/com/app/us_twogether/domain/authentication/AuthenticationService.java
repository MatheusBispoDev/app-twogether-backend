package com.app.us_twogether.domain.authentication;

import com.app.us_twogether.domain.authentication.token.RefreshToken;
import com.app.us_twogether.domain.authentication.token.TokenService;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.user.UserRequestDTO;
import com.app.us_twogether.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationMapper authenticationMapper;

    public String register(UserRequestDTO userDTO) {
        User user = new User(userDTO.username(), userDTO.password(), userDTO.name(), userDTO.email(), userDTO.phoneNumber(), userDTO.type());

        return userService.createUser(user).username();
    }

    public LoginResponseDTO login(User user){
        String accessToken = tokenService.generateAccessToken(user.getUsername());
        RefreshToken refreshToken = tokenService.createRefreshToken(user);

        return authenticationMapper.toLoginResponseDTO(user.getUsername(), accessToken, refreshToken);
    }

    public void logout(String accessToken, String refreshToken) {
        tokenService.deleteToken(accessToken, refreshToken);
    }
}
