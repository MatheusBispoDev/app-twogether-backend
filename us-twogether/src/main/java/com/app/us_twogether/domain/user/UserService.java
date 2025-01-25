package com.app.us_twogether.domain.user;

import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.app.us_twogether.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findById(username).orElseThrow(() -> new DataNotFoundException("Usuário não encontrado"));
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
            return Optional.of(userRepository.save(user));
        }

        return Optional.empty();
    }

}