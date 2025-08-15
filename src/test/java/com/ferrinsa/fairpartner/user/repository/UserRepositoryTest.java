package com.ferrinsa.fairpartner.user.repository;

import com.ferrinsa.fairpartner.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    private static final String NAME = "ivan";
    private static final String EMAIL = "ivan@example.com";
    private static final String BAD_EMAIL = "badEmail@example.com";

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User(NAME, EMAIL, "pswd");
        userRepository.save(user);
    }

    @Test
    @DisplayName("User found by email")
    void findByEmail_returnsUser_whenEmailMatches() {
        assertEquals(EMAIL, userRepository.findByEmail(EMAIL).orElseThrow().getEmail());
    }

    @Test
    @DisplayName("User not found by email")
    void findByEmail_returnsEmpty_whenEmailDoesNotMatch() {
        assertTrue(userRepository.findByEmail(BAD_EMAIL).isEmpty());
    }

    @Test
    @DisplayName("Email not exists")
    void existsByEmail_returnsFalse_whenEmailNotExists() {
        assertFalse(userRepository.existsByEmail(BAD_EMAIL));
    }

    @Test
    @DisplayName("Email exists")
    void existsByEmail_returnsTrue_whenEmailExists() {
        assertTrue(userRepository.existsByEmail(EMAIL));
    }
}
