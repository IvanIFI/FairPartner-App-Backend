
package com.ferrinsa.fairpartner.user.service;

import com.ferrinsa.fairpartner.exception.user.UserEmailAlreadyExistsException;
import com.ferrinsa.fairpartner.exception.user.UserNotFoundException;
import com.ferrinsa.fairpartner.security.role.service.RoleService;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;
import com.ferrinsa.fairpartner.user.dto.RegisterUserRequestDTO;
import com.ferrinsa.fairpartner.user.dto.UserResponseDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.repository.UserRepository;
import com.ferrinsa.fairpartner.user.util.UserTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.ferrinsa.fairpartner.security.util.RolesTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.UserTestConstants.*;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock RoleService roleService;
    @InjectMocks UserService userService;

    private UserEntity expectedUser ;
    private List<UserEntity> expectListUsers;

    @BeforeEach
    void setUp() {
        expectedUser = UserTestFactory.buildTestUser();
        expectListUsers = UserTestFactory.buildListTestUsers();
    }

    @Test
    @DisplayName("Find all users with users")
    void findAllUsers_returnsUserEntityList_whenUsersExist(){
        when(userRepository.findAll()).thenReturn(expectListUsers);
        assertEquals(expectListUsers,userService.findAllUsers());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Find all users with no users")
    void findAllUsers_returnEmptyList_whenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(userService.findAllUsers().isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("User found by Id")
    void findUserById_returnUser_whenIdMatches(){
        when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser));
        assertEquals(expectedUser,userService.findUserById(ID_1));
        verify(userRepository).findById(ID_1);
    }

    @Test
    @DisplayName("User not found by Id")
    void findUserById_throwsUserNotFoundException_whenIdDoesNotMatch() {
        when(userRepository.findById(ID_1)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findUserById(ID_1));
        verify(userRepository).findById(ID_1);
    }

    @Test
    @DisplayName("User found by email")
    void findUserByEmail_returnUser_whenEmailMatches(){
        when(userRepository.findByEmail(EMAIL_1)).thenReturn(Optional.of(expectedUser));
        assertEquals(expectedUser, userService.findUserByEmail(EMAIL_1));
        verify(userRepository).findByEmail(EMAIL_1);
    }

    @Test
    @DisplayName("User not found by email")
    void findUserByEmail_throwsUserNameNotFoundException_whenEmailDoesNotMatch() {
        when(userRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.findUserByEmail(INVALID_EMAIL));
        verify(userRepository).findByEmail(INVALID_EMAIL);
    }

    @DisplayName("Sign up Successful")
    @Test
    void createNewUser_returnUserLoginResponseDTO_whenSignUpIsSuccessful() {
        when(userRepository.existsByEmail(EMAIL_1)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(roleService.findRoleByName(UserRoles.USER)).thenReturn(ROLE_USER);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(ID_1);
            savedUser.setRegistrationDate(DEFAULT_DATE);
            return savedUser;
        });

        UserResponseDTO userResponseDTO = userService.registerNewUser(new RegisterUserRequestDTO(NAME_1,EMAIL_1,PASSWORD));

        assertAll(
                () -> assertEquals(ID_1, userResponseDTO.id()),
                () -> assertEquals(NAME_1, userResponseDTO.name()),
                () -> assertEquals(EMAIL_1, userResponseDTO.email()),
                () -> assertEquals(DEFAULT_ROLE_LIST, userResponseDTO.roles())
        );
        verify(userRepository).existsByEmail(EMAIL_1);
        verify(passwordEncoder).encode(PASSWORD);
        verify(userRepository).save(any(UserEntity.class));
        verify(roleService).findRoleByName(UserRoles.USER);
    }

    @DisplayName("Sign up failed - Email already exists")
    @Test
    void createNewUser_returnUserEmailAlreadyExistsException_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(EMAIL_1)).thenReturn(true);

        RegisterUserRequestDTO registerUserDTO = new RegisterUserRequestDTO(NAME_1, EMAIL_1,PASSWORD);

        assertThrows(UserEmailAlreadyExistsException.class, () -> userService.registerNewUser(registerUserDTO));
        verify(userRepository).existsByEmail(EMAIL_1);
    }
}
