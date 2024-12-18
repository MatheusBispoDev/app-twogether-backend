package com.app.us_twogether.controller;

import com.app.us_twogether.domain.user.AuthenticationDTO;
import com.app.us_twogether.domain.user.LoginResponseDTO;
import com.app.us_twogether.domain.user.UserDTO;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.security.TokenService;
import com.app.us_twogether.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserDTO userDTO){
        User newUser = new User(userDTO.username(), userDTO.password(), userDTO.name(), userDTO.email(), userDTO.phoneNumber(), userDTO.type());

        userService.saveUser(newUser);

        return ResponseEntity.ok().build();
    }
}
