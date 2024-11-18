package com.app.us_twogether.service;

import com.app.us_twogether.model.User;
import com.app.us_twogether.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new User(userEntity.getUsername(), userEntity.getPassword());
    }
}
