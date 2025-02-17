package com.app.us_twogether.domain.authentication;

import com.app.us_twogether.domain.authentication.token.RefreshToken;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.user.UserRequestDTO;
import com.app.us_twogether.domain.user.UserResponseDTO;
import com.app.us_twogether.domain.user.UserService;
import com.app.us_twogether.domain.authentication.token.TokenService;
import com.app.us_twogether.exception.TokenInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.base-url}/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationRequestDTO authenticationDTO) {
        var username = new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password());

        Authentication auth = this.authenticationManager.authenticate(username);

        User user = (User) auth.getPrincipal();

        LoginResponseDTO loginResponseDTO = tokenService.login(user);

        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO userDTO) {
        User newUser = new User(userDTO.username(), userDTO.password(), userDTO.name(), userDTO.email(), userDTO.phoneNumber(), userDTO.type());

        UserResponseDTO user = userService.createUser(newUser);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("refreshToken");

        RefreshToken refreshToken = tokenService.findByToken(token);

        if (tokenService.isTokenExpired(refreshToken)) {
            throw new TokenInvalidException("Refresh Token inválido ou expirado");
        }

        LoginResponseDTO loginResponseDTO = tokenService.login(refreshToken.getUser());

        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String refreshToken = request.getHeader("refreshToken");

        tokenService.logout(token, refreshToken);

        return ResponseEntity.ok("Logout realizado com sucesso");
    }
}
