package com.app.us_twogether.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PasswordEncoderTest {

    @Test
    public void testPasswordEncoding() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "mySecretPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Verifica se o hash não é nulo
        assertNotNull(encodedPassword);

        // Verifica se a senha bruta corresponde ao hash gerado
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));

        // Opcional: garante que encodes diferentes são únicos
        String anotherEncodedPassword = passwordEncoder.encode(rawPassword);
        assertNotEquals(encodedPassword, anotherEncodedPassword);
    }

    @Test
    public void testPasswordMatches() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "mySecretPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Verifica se a senha corresponde ao hash gerado
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));

        // Testa com uma senha errada
        String wrongPassword = "wrongPassword";
        assertFalse(passwordEncoder.matches(wrongPassword, encodedPassword));
    }
}
