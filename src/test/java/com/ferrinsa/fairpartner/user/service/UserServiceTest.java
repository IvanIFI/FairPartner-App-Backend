
package com.ferrinsa.fairpartner.user.service;

import com.ferrinsa.fairpartner.exception.role.RoleNotFoundException;
import com.ferrinsa.fairpartner.exception.user.*;
import com.ferrinsa.fairpartner.security.role.model.RoleEntity;
import com.ferrinsa.fairpartner.security.role.service.RoleService;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;
import com.ferrinsa.fairpartner.security.dto.RegisterUserRequestDTO;
import com.ferrinsa.fairpartner.user.dto.UserAdminResponseDTO;
import com.ferrinsa.fairpartner.user.dto.UserResponseDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.repository.UserRepository;
import com.ferrinsa.fairpartner.user.util.UserTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.ferrinsa.fairpartner.security.util.RolesTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.DtosTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.UserTestConstants.*;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock RoleService roleService;
    @InjectMocks UserServiceImpl userService;

    private UserEntity expectedUser1;
    private UserEntity expectedUser2;
    private List<UserEntity> expectListUsers;

    @BeforeEach
    void setUp() {
        expectedUser1 = UserTestFactory.buildTestUser1();
        expectedUser2 = UserTestFactory.buildTestUser2();
        expectListUsers = UserTestFactory.buildListTestUsers();
    }

    @Nested
    @DisplayName("Find Users")
    class FindUsersTests {

        @Test
        @DisplayName("Find all users with users")
        void findAllUsers_returnsUserEntityList_whenUsersExist() {
            when(userRepository.findAll()).thenReturn(expectListUsers);
            assertEquals(expectListUsers, userService.findAllUsers());
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
        void findUserById_returnUser_whenIdMatches() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));
            assertEquals(expectedUser1, userService.findUserById(ID_1));
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
        void findUserByEmail_returnUser_whenEmailMatches() {
            when(userRepository.findByEmail(EMAIL_1)).thenReturn(Optional.of(expectedUser1));
            assertEquals(expectedUser1, userService.findUserByEmail(EMAIL_1));
            verify(userRepository).findByEmail(EMAIL_1);
        }

        @Test
        @DisplayName("User not found by email")
        void findUserByEmail_throwsUserNameNotFoundException_whenEmailDoesNotMatch() {
            when(userRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
            assertThrows(UsernameNotFoundException.class, () -> userService.findUserByEmail(INVALID_EMAIL));
            verify(userRepository).findByEmail(INVALID_EMAIL);
        }
    }

    @Nested
    @DisplayName("Register Users")
    class RegisterUserTests {

        @Test
        @DisplayName("Sign up Successful")
        void createNewUser_returnUserLoginResponseDTO_whenSignUpIsSuccessful() {
            when(userRepository.existsByEmail(EMAIL_1)).thenReturn(false);
            when(passwordEncoder.encode(PASSWORD)).thenReturn(HASHED_PASSWORD_1);
            when(roleService.findRoleByName(UserRoles.USER)).thenReturn(ROLE_USER);
            when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
                UserEntity savedUser = invocation.getArgument(0);
                savedUser.setId(ID_1);
                savedUser.setRegistrationDate(DEFAULT_DATE);
                return savedUser;
            });

            UserResponseDTO userResponseDTO = userService.registerNewUser(new RegisterUserRequestDTO(NAME_1, EMAIL_1, PASSWORD));

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

        @Test
        @DisplayName("Sign up failed - Email already exists")
        void createNewUser_returnUserEmailAlreadyExistsException_whenEmailAlreadyExists() {
            when(userRepository.existsByEmail(EMAIL_1)).thenReturn(true);

            RegisterUserRequestDTO registerUserDTO = new RegisterUserRequestDTO(NAME_1, EMAIL_1, PASSWORD);

            assertThrows(UserEmailAlreadyExistsException.class, () -> userService.registerNewUser(registerUserDTO));
            verify(userRepository).existsByEmail(EMAIL_1);
        }
    }

    @Nested
    @DisplayName("Update Users")
    class UpdateUserTests {

        @Test
        @DisplayName("Update name successful")
        void updateNameUser_returnUserResponseDTO_whenSuccessfulUpdate() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));

            String resultUpdate = userService.updateNameUser(expectedUser1, UPDATE_USER_NAME_REQUEST_DTO).name();

            assertEquals(NAME_2, resultUpdate);
            verify(userRepository).findById(ID_1);
        }

        @Test
        @DisplayName("Update name failed")
        void updateNameUser_returnUserFailedUpdateProfileException_whenFailedUpdate() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));

            assertThrows(UserFailedUpdateProfileException.class,
                    () -> userService.updateNameUser(expectedUser1, INVALID_UPDATE_USER_NAME_REQUEST_DTO));
            verify(userRepository).findById(ID_1);
        }

        @Test
        @DisplayName("Update email successful")
        void updateEmailUser_returnUserResponseDTO_whenSuccessfulUpdate() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));
            when(userRepository.findByEmail(UPDATE_USER_EMAIL_REQUEST_DTO.email())).thenReturn(Optional.empty());

            String resultUpdate = userService.updateEmailUser(expectedUser1, UPDATE_USER_EMAIL_REQUEST_DTO).email();

            assertEquals(EMAIL_2, resultUpdate);
            verify(userRepository).findByEmail(EMAIL_2);
            verify(userRepository).findById(ID_1);
        }

        @Test
        @DisplayName("Update email failed")
        void updateEmailUser_returnUserEmailAlreadyExistsException_whenFailedUpdateWhenEmailAlreadyExists() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));
            when(userRepository.findByEmail(UPDATE_USER_EMAIL_REQUEST_DTO.email())).thenReturn(Optional.of(expectedUser2));

            assertThrows(UserEmailAlreadyExistsException.class, () -> userService.updateEmailUser(expectedUser1, UPDATE_USER_EMAIL_REQUEST_DTO));
            verify(userRepository).findByEmail(EMAIL_2);
            verify(userRepository).findById(ID_1);
        }

        @Test
        @DisplayName("Update password successful")
        void updatePasswordUser_returnVoid_whenSuccessfulUpdatePassword() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));
            when(passwordEncoder
                    .matches(eq(UPDATE_USER_PASSWORD_REQUEST_DTO.oldPassword()), anyString()))
                    .thenReturn(true);
            when(passwordEncoder.encode(UPDATE_USER_PASSWORD_REQUEST_DTO.newPassword())).thenReturn(HASHED_PASSWORD_2);

            userService.updatePasswordUser(expectedUser1, UPDATE_USER_PASSWORD_REQUEST_DTO);

            assertEquals(HASHED_PASSWORD_2, expectedUser1.getPassword());
            verify(passwordEncoder).matches(
                    eq(UPDATE_USER_PASSWORD_REQUEST_DTO.oldPassword()),
                    anyString()
            );
            verify(passwordEncoder).encode(UPDATE_USER_PASSWORD_REQUEST_DTO.newPassword());
            verify(userRepository).findById(ID_1);
        }

        @Test
        @DisplayName("Update password fails - invalid password ")
        void updatePasswordUser_throwUserPasswordCheckException_whenOldPasswordInvalid() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));
            when(passwordEncoder
                    .matches(eq(UPDATE_USER_PASSWORD_REQUEST_DTO.oldPassword()), anyString()))
                    .thenReturn(false);

            assertThrows(UserPasswordCheckException.class,
                    () -> userService.updatePasswordUser(expectedUser1, UPDATE_USER_PASSWORD_REQUEST_DTO));

            verify(passwordEncoder).matches(eq(UPDATE_USER_PASSWORD_REQUEST_DTO.oldPassword()), anyString());
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository).findById(ID_1);
        }

        @Test
        @DisplayName("Update password fails - invalid confirmation")
        void updatePasswordUser_throwUserPasswordException_whenConfirmationInvalid() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));
            when(passwordEncoder.matches(
                    eq(UPDATE_USER_PASSWORD_REQUEST_DTO.oldPassword()),
                    anyString()
            )).thenReturn(true);

            assertThrows(UserPasswordException.class,
                    () -> userService.updatePasswordUser(expectedUser1, INVALID_UPDATE_USER_PASSWORD_REQUEST_DTO));

            verify(passwordEncoder).matches(eq(UPDATE_USER_PASSWORD_REQUEST_DTO.oldPassword()), anyString());
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository).findById(ID_1);
        }
    }

    @Nested
    @DisplayName("Delete Users")
    class DeleteUserTests {

        @Test
        @DisplayName("Delete user successful")
        void deleteUser_returnVoid_whenSuccessfulDelete() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));
            doNothing().when(userRepository).delete(expectedUser1);

            userService.deleteUser(expectedUser1);

            verify(userRepository).findById(ID_1);
            verify(userRepository).delete(expectedUser1);
        }
    }

    @Nested
    @DisplayName("Admin Updates")
    class AdminUpdateTests {

        @Test
        @DisplayName("Data update from admin successful")
        void updateUserToAdmin_ReturnUserAdminResponseDTO_whenUpdateIsSuccessful() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));
            when(roleService.findRoleByName(UserRoles.ADMIN)).thenReturn(new RoleEntity(UserRoles.ADMIN));

            UserAdminResponseDTO userAdminResponseDTO = userService.updateUserToAdmin(ID_1, USER_UPDATE_ADMIN_REQUEST_DTO);

            assertAll(
                    () -> assertEquals(NAME_2, userAdminResponseDTO.name()),
                    () -> assertEquals(EMAIL_2, userAdminResponseDTO.email()),
                    () -> assertEquals(ADMIN_ROLE_LIST, userAdminResponseDTO.roles())
            );

            assertAll(
                    () -> assertEquals(NAME_2, expectedUser1.getName()),
                    () -> assertEquals(EMAIL_2, expectedUser1.getEmail()),
                    () -> assertEquals(ADMIN_ROLE_LIST, expectedUser1.getRoles()
                            .stream().map(RoleEntity::getRoleName).toList())
            );

            verify(userRepository).findById(ID_1);
            verify(roleService).findRoleByName(UserRoles.ADMIN);
        }

        @Test
        @DisplayName("Data update from admin failed - Not found ID")
        void updateUserToAdmin_ReturnUserNotFoundException_whenFindByIdNotFound() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService
                    .updateUserToAdmin(ID_1, USER_UPDATE_ADMIN_REQUEST_DTO));

            verify(userRepository).findById(ID_1);
        }

        @Test
        @DisplayName("Data update from admin failed - Invalid Role")
        void updateUserToAdmin_ReturnIllegalArgumentException_whenRoleIsInvalid() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));

            assertThrows(IllegalArgumentException.class, () -> userService
                    .updateUserToAdmin(ID_1, INVALID_ROLE_USER_UPDATE_ADMIN_REQUEST_DTO));

            verify(userRepository).findById(ID_1);
        }

        @Test
        @DisplayName("Data update from admin failed - Role not exists ")
        void updateUserToAdmin_ReturnRoleNotFoundException_whenRoleNotExistsInDataBase() {
            when(userRepository.findById(ID_1)).thenReturn(Optional.of(expectedUser1));
            when(roleService.findRoleByName(UserRoles.ADMIN)).thenThrow(new RoleNotFoundException(UserRoles.ADMIN));

            assertThrows(RoleNotFoundException.class, () -> userService
                    .updateUserToAdmin(ID_1, USER_UPDATE_ADMIN_REQUEST_DTO));

            verify(userRepository).findById(ID_1);
            verify(roleService).findRoleByName(UserRoles.ADMIN);
        }
    }
}
