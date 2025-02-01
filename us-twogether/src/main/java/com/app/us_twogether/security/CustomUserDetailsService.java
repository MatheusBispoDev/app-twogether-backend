package com.app.us_twogether.security;

import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.user.UserRepository;
import com.app.us_twogether.exception.DataNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails userEntity = userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("Usuário não encontrado: " + username));

        return new User(userEntity.getUsername(), userEntity.getPassword());
    }
}
