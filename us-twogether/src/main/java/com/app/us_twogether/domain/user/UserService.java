package com.app.us_twogether.domain.user;

import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.app.us_twogether.exception.DataNotFoundException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserResponseDTO findByUsername(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new DataNotFoundException("Usuário não encontrado"));

        return userMapper.toResponseDTO(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findById(username).orElseThrow(() -> new DataNotFoundException("Usuário não encontrado"));
    }

    public UserResponseDTO createUser(User newUser) {
        if (userRepository.existsById(newUser.getUsername())) {
            throw new DataAlreadyExistsException("Usuário '" + newUser.getUsername() + "' já está cadastrado.");
        }

        newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));

        User user = userRepository.save(newUser);

        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO updateUser(String username, UserRequestDTO user) {
        User updatedUser = userRepository.findById(username).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        updatedUser.setPassword(user.password());
        updatedUser.setName(user.name());
        updatedUser.setEmail(user.email());
        updatedUser.setPhoneNumber(user.phoneNumber());
        updatedUser.setType(user.type());

        return userMapper.toResponseDTO(userRepository.save(updatedUser));
    }

    public void logout(){

    }

}