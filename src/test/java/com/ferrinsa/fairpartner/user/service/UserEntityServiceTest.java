/*
package com.ferrinsa.fairpartner.user.service;

import com.ferrinsa.fairpartner.exception.user.UserEmailAlreadyExistsException;
import com.ferrinsa.fairpartner.exception.user.UserLoginFailedException;
import com.ferrinsa.fairpartner.security.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.user.dto.RegisterUserDTO;
import com.ferrinsa.fairpartner.security.dto.LoginResponseDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

    private static final Long ID = 1L;
    private static final String NAME = "Ivan";
    private static final String EMAIL = "ivan@example.com";
    private static final String BAD_EMAIL = "badEmail@example.com";
    private static final String PASSWORD = "12345678";
    private static final String WRONG_PASSWORD = "87654321";
    private static final String ENCODED_PASSWORD = "$2a$10$abcDEFghiJKLmnopQRstu";

    private static final UserEntity USER = new UserEntity(NAME, EMAIL, PASSWORD);


    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UserService userService;

    @Test
    @DisplayName("User found by email")
    void findUserByEmail_returnUser_whenEmailMatches(){
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(USER));
        assertEquals(USER, userService.findUserByEmail(EMAIL));
        verify(userRepository).findByEmail(EMAIL);
    }

    */
/* // FIXME: UserNameNotFoundExcepction Change
    @DisplayName("User not found by email")
    @Test
    void findUserByEmail_throwsUserNotFoundException_whenEmailDoesNotMatch() {
        when(userRepository.findByEmail(BAD_EMAIL)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findUserByEmail(BAD_EMAIL));
        verify(userRepository).findByEmail(BAD_EMAIL);
    }*//*


    @DisplayName("Login successful")
    @Test
    void loginValidateUser_returnUserLoginResponseDTO_whenLoginIsSuccessful() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(USER));
        USER.setId(ID);
        when(passwordEncoder.matches(PASSWORD, USER.getPassword())).thenReturn(true);

        LoginResponseDTO loginResponseDTO = userService.loginValidateUser(new LoginRequestDTO(EMAIL,PASSWORD));

        assertNotNull(loginResponseDTO.id());
        verify(userRepository).findByEmail(EMAIL);
        verify(passwordEncoder).matches(PASSWORD, USER.getPassword());
    }

    @DisplayName("Login fails - email not found")
    @Test
    void loginValidateUser_throwsUserLoginFailedException_whenEmailIsWrong() {
        when(userRepository.findByEmail(BAD_EMAIL)).thenReturn(Optional.empty());
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(BAD_EMAIL,PASSWORD);

        assertThrows(UserLoginFailedException.class, () -> userService.loginValidateUser(loginRequestDTO));
        verify(userRepository).findByEmail(BAD_EMAIL);
    }

    @DisplayName("Login fails - incorrect password")
    @Test
    void loginValidateUser_throwsUserLoginFailedException_whenPasswordIsWrong() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(USER));
        USER.setId(ID);
        when(passwordEncoder.matches(WRONG_PASSWORD,USER.getPassword())).thenReturn(false);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(EMAIL,WRONG_PASSWORD);

        assertThrows(UserLoginFailedException.class, () -> userService.loginValidateUser(loginRequestDTO));
        verify(userRepository).findByEmail(EMAIL);
        verify(passwordEncoder).matches(WRONG_PASSWORD,USER.getPassword());
    }

    @DisplayName("Sign up Successful")
    @Test
    void createNewUser_returnUserLoginResponseDTO_whenSignUpIsSuccesful() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity newUser = invocation.getArgument(0);
            newUser.setId(ID);
            return newUser;
        });

        LoginResponseDTO loginResponseDTO = userService.registerNewUser(new RegisterUserDTO(NAME,EMAIL,PASSWORD));

        assertNotNull(loginResponseDTO.id());
        verify(userRepository).existsByEmail(EMAIL);
        verify(passwordEncoder).encode(PASSWORD);
        verify(userRepository).save(any(UserEntity.class));
    }

    @DisplayName("Sign up failed - Email already exists")
    @Test
    void createNewUser_returnUserEmailAlreadyExistsException_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        RegisterUserDTO registerUserDTO = new RegisterUserDTO(NAME,EMAIL,PASSWORD);

        assertThrows(UserEmailAlreadyExistsException.class, () -> userService.registerNewUser(registerUserDTO));
        verify(userRepository).existsByEmail(EMAIL);
    }

}*/
