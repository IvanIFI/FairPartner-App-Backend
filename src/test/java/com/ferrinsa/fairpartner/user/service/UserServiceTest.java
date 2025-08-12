package com.ferrinsa.fairpartner.user.service;

import com.ferrinsa.fairpartner.exception.user.UserNotFoundException;
import com.ferrinsa.fairpartner.user.model.User;
import com.ferrinsa.fairpartner.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String NAME = "ivan";
    private static final String EMAIL = "ivan@example.com";
    private static final String BAD_EMAIL = "badEmail@example.com";
    private static final User USER = new User(NAME, EMAIL, "pswd", LocalDate.of(2024, 1, 1));

    @Mock UserRepository userRepository;
    @InjectMocks UserService userService;

    @Test
    @DisplayName("User found by email")
    void findUserByEmail_returnUser_whenEmailMatches(){
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(USER));
        assertEquals(USER, userService.findUserByEmail(EMAIL));
        verify(userRepository).findByEmail(EMAIL);
    }

    @DisplayName("User not found by email")
    @Test
    void findUserByEmail_throwsUserNotFoundException_whenEmailDoesNotMatch() {
        when(userRepository.findByEmail(BAD_EMAIL)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findUserByEmail(BAD_EMAIL));
        verify(userRepository).findByEmail(BAD_EMAIL);

    }
}