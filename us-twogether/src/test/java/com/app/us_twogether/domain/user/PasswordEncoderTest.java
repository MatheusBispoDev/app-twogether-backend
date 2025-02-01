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

        assertNotNull(encodedPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));

        String anotherEncodedPassword = passwordEncoder.encode(rawPassword);
        assertNotEquals(encodedPassword, anotherEncodedPassword);
    }

    @Test
    public void testPasswordMatches() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "mySecretPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));

        String wrongPassword = "wrongPassword";
        assertFalse(passwordEncoder.matches(wrongPassword, encodedPassword));
    }
}
