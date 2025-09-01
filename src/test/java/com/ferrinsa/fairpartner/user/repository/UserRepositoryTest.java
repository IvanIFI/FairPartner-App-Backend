
package com.ferrinsa.fairpartner.user.repository;


import com.ferrinsa.fairpartner.user.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static com.ferrinsa.fairpartner.user.util.UserTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity(NAME_1, EMAIL_1, PASSWORD);
        userRepository.save(user);
    }

    @Test
    @DisplayName("User found by email")
    void findByEmail_returnsUser_whenEmailMatches() {
        assertEquals(EMAIL_1, userRepository.findByEmail(EMAIL_1).orElseThrow().getEmail());
    }

    @Test
    @DisplayName("User not found by email")
    void findByEmail_returnsEmpty_whenEmailDoesNotMatch() {
        assertTrue(userRepository.findByEmail(INVALID_EMAIL).isEmpty());
    }

    @Test
    @DisplayName("Email not exists")
    void existsByEmail_returnsFalse_whenEmailNotExists() {
        assertFalse(userRepository.existsByEmail(INVALID_EMAIL));
    }

    @Test
    @DisplayName("Email exists")
    void existsByEmail_returnsTrue_whenEmailExists() {
        assertTrue(userRepository.existsByEmail(EMAIL_1));
    }
}
