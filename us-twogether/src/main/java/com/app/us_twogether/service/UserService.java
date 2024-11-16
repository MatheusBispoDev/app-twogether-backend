package com.app.us_twogether.service;

import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.app.us_twogether.model.User;
import com.app.us_twogether.repository.UserRepository;
import com.app.us_twogether.util.ValidationCPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findById(username);
    }

    public User saveUser(User newUser) {
        if (userRepository.existsById(newUser.getUsername())) {
            throw new DataAlreadyExistsException("Usuário '" + newUser.getUsername() + "' já está cadastrado.");
        }
        return userRepository.save(newUser);
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

            if (ValidationsCPF(updatedUser.getCpf())) {
                user.setCpf(updatedUser.getCpf());
            }

            return Optional.of(userRepository.save(user));
        }

        return Optional.empty();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Boolean ValidationsCPF(String cpf) {
        if (userRepository.findBycpf(cpf).isPresent()) {
            throw new DataAlreadyExistsException("O CPF '" + cpf + "' já foi cadastrado.");
        }
        if (cpf == null) {
            return false;
        }

        ValidationCPF.Valid(cpf); //valida se o CPF é válido

        return true;
    }
}
