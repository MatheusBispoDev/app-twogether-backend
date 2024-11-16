package com.app.us_twogether.repository;

import com.app.us_twogether.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findBycpf(String cpf);
}
