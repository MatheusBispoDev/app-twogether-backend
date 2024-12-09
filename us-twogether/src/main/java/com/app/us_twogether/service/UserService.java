package com.app.us_twogether.service;

import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.repository.NotificationUserRepository;
import com.app.us_twogether.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationUserRepository notificationUserRepository;

    public User findByUsername(String username) {
        //TODO Tratar melhor a excecao
        return userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public void saveUser(User newUser) {
        if (userRepository.existsById(newUser.getUsername())) {
            throw new DataAlreadyExistsException("Usuário '" + newUser.getUsername() + "' já está cadastrado.");
        }

        userRepository.save(newUser);
    }

    public Optional<User> updateUser(String username, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(username);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setPassword(updatedUser.getPassword());
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setType(updatedUser.getType());
            user.setNotificationUser(updatedUser.getNotificationUser());
            return Optional.of(userRepository.save(user));
        }

        return Optional.empty();
    }

}