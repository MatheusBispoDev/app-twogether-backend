package com.app.us_twogether.domain.passwordEncoder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PasswordEncoderTest {

    @Test
    public void shouldEncodePassword_whenKeysAreMatches() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "mySecretPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));

        String anotherEncodedPassword = passwordEncoder.encode(rawPassword);
        assertNotEquals(encodedPassword, anotherEncodedPassword);
    }

    @Test
    public void shouldEncodePassword_whenKeysDoNotMatches() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "mySecretPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        String wrongPassword = "wrongPassword";
        assertFalse(passwordEncoder.matches(wrongPassword, encodedPassword));
    }
}
