
package com.ferrinsa.fairpartner.user.controller;


import com.ferrinsa.fairpartner.exception.user.UserNotFoundException;
import com.ferrinsa.fairpartner.security.config.TestSecurityConfig;
import com.ferrinsa.fairpartner.security.config.TestUserDetailsServiceConfig;
import com.ferrinsa.fairpartner.security.jwt.JwtTokenFilter;
import com.ferrinsa.fairpartner.user.dto.UpdateUserEmailRequestDTO;
import com.ferrinsa.fairpartner.user.dto.UpdateUserNameRequestDTO;
import com.ferrinsa.fairpartner.user.dto.UserUpdateAdminRequestDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.service.UserService;
import com.ferrinsa.fairpartner.user.util.UserTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.ferrinsa.fairpartner.security.util.RolesTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.DtosTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.ExceptionsTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.JsonTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.UserTestConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.Mockito.verify;


@Import({TestSecurityConfig.class, TestUserDetailsServiceConfig.class})
@WebMvcTest(
        controllers = {UserController.class, AdminUserController.class},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtTokenFilter.class
        )
)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private UserService userService;

    @Nested
    @DisplayName("Gets")
    class GetTest {

        @Nested
        @DisplayName("Admin")
        class GetAdmin {

            @Test
            @DisplayName("/admin/users - When users exist")
            void getAllUsers_returnOk200WithList_whenUsersExist() throws Exception {
                when(userService.findAllUsers()).thenReturn(UserTestFactory.buildListTestUsers());

                mockMvc.perform(get("/admin/users")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[0].id").value(ID_1))
                        .andExpect(jsonPath("$[0].email").value(EMAIL_1))
                        .andExpect(jsonPath("$[1].id").value(ID_2))
                        .andExpect(jsonPath("$[1].email").value(EMAIL_2));

                verify(userService).findAllUsers();
            }

            @Test
            @DisplayName("/admin/users - When no users exist")
            void getAllUsers_returnOk200WithEmptyList_whenNoUsersExist() throws Exception {
                when(userService.findAllUsers()).thenReturn(Collections.emptyList());

                mockMvc.perform(get("/admin/users")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(0)));

                verify(userService).findAllUsers();
            }

            @Test
            @DisplayName("/admin/users/{id} - Found")
            void getUserById_returnOk200WithUser_whenIdMatches() throws Exception {
                when(userService.findUserById(ID_1)).thenReturn(UserTestFactory.buildTestUser1());

                mockMvc.perform(get("/admin/users/{id}", ID_1)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(ID_1))
                        .andExpect(jsonPath("$.name").value(NAME_1))
                        .andExpect(jsonPath("$.email").value(EMAIL_1))
                        .andExpect(jsonPath("$.password").value(HASHED_PASSWORD_1))
                        .andExpect(jsonPath("$.roles[0]").value(ROLE_USER.getRoleName()));

                verify(userService).findUserById(ID_1);
            }

            @Test
            @DisplayName("/admin/users/email - Found")
            void getUserByEmail_returnOk200WithUser_whenEmailMatches() throws Exception {
                when(userService.findUserByEmail(EMAIL_1)).thenReturn(UserTestFactory.buildTestUser1());

                mockMvc.perform(get("/admin/users/email")
                                .param("email", EMAIL_1)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(ID_1))
                        .andExpect(jsonPath("$.name").value(NAME_1))
                        .andExpect(jsonPath("$.email").value(EMAIL_1))
                        .andExpect(jsonPath("$.password").value(HASHED_PASSWORD_1))
                        .andExpect(jsonPath("$.roles[0]").value(ROLE_USER.getRoleName()));

                verify(userService).findUserByEmail(EMAIL_1);
            }

            @Test
            @DisplayName("/admin/users/email - Not found")
            void getUserByEmail_returnNotFound404WithJsonProblem_whenEmailNotMatches() throws Exception {
                when(userService.findUserByEmail(INVALID_EMAIL)).thenThrow(new UsernameNotFoundException(INVALID_EMAIL));

                mockMvc.perform(get("/admin/users/email")
                                .param("email", INVALID_EMAIL)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                        .andExpect(jsonPath("$.title").value(USER_NOT_FOUND_TITLE))
                        .andExpect(jsonPath("$.detail").exists());

                verify(userService).findUserByEmail(INVALID_EMAIL);
            }

            @Test
            @DisplayName("/admin/users/email - Not valid email format")
            void getUserByEmail_returnBadRequest400WithJsonProblem_whenEmailNotValid() throws Exception {
                mockMvc.perform(get("/admin/users/email")
                                .param("email", INVALID_FORMAT_EMAIL)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                        .andExpect(jsonPath("$.title").value(INVALID_EMAIL_TITLE))
                        .andExpect(jsonPath("$.detail").exists());
            }

        }

        @Nested
        @DisplayName("User")
        class GetUser {

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/users/me - when is authenticated ")
            void getMe_returnOk200WithUser_whenUserIsLogin() throws Exception {
                mockMvc.perform(get("/users/me")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.email").value(EMAIL_1))
                        .andExpect(jsonPath("$.roles[0]").value(ROLE_USER.getRoleName()));
            }

        }

    }

    @Nested
    @DisplayName("Patches")
    class PatchTest {

        @Nested
        @DisplayName("Admin")
        class PatchAdmin {

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/admin/users/{id}} - when update admin is successful")
            void updateUserToAdmin_returnOk200_whenUpdateByAdminIsSuccessful() throws Exception {
                when(userService.updateUserToAdmin(any(Long.class), any(UserUpdateAdminRequestDTO.class)))
                        .thenReturn(USER_ADMIN_RESPONSE_DTO);

                mockMvc.perform(patch("/admin/users/{id}", ID_1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(UPDATE_USER_BY_ADMIN_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(USER_ADMIN_RESPONSE_DTO.id()))
                        .andExpect(jsonPath("$.name").value(USER_ADMIN_RESPONSE_DTO.name()))
                        .andExpect(jsonPath("$.email").value(USER_ADMIN_RESPONSE_DTO.email()))
                        .andExpect(jsonPath("$.password").value(USER_ADMIN_RESPONSE_DTO.password()))
                        .andExpect(jsonPath("$.roles[0]").exists());

                verify(userService).updateUserToAdmin(eq(ID_1), any(UserUpdateAdminRequestDTO.class));
            }

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/admin/users/{id}} - when user found not exists")
            void updateUserToAdmin_returnNotFound404_whenUpdateByAdminIsFailedToUserNotFound() throws Exception {
                when(userService.updateUserToAdmin(eq(ID_NOT_EXIST), any(UserUpdateAdminRequestDTO.class)))
                        .thenThrow(new UserNotFoundException(Long.toString(ID_NOT_EXIST)));

                mockMvc.perform(patch("/admin/users/{id}", ID_NOT_EXIST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(UPDATE_USER_BY_ADMIN_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.title").value(TITLE_USER_NOT_FOUND))
                        .andExpect(jsonPath("$.detail").exists());

                verify(userService).updateUserToAdmin(eq(ID_NOT_EXIST), any(UserUpdateAdminRequestDTO.class));
            }

        }

        @Nested
        @DisplayName("User")
        class PatchUser {

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/users/me/name - when update is successful")
            void updateName_returnOk200_whenIsSuccessful() throws Exception {
                when(userService.updateNameUser(any(UserEntity.class), any(UpdateUserNameRequestDTO.class)))
                        .thenReturn(USER_RESPONSE_DTO);

                mockMvc.perform(patch("/users/me/name")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(UPDATE_NAME_USER_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(USER_RESPONSE_DTO.id()))
                        .andExpect(jsonPath("$.name").value(USER_RESPONSE_DTO.name()))
                        .andExpect(jsonPath("$.email").value(USER_RESPONSE_DTO.email()))
                        .andExpect(jsonPath("$.roles").exists());
            }

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/users/me/email - when update is successful")
            void updateEmail_returnOk200_whenIsSuccessful() throws Exception {
                when(userService.updateEmailUser(any(UserEntity.class), any(UpdateUserEmailRequestDTO.class)))
                        .thenReturn(USER_RESPONSE_DTO);

                mockMvc.perform(patch("/users/me/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(UPDATE_EMAIL_USER_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(USER_RESPONSE_DTO.id()))
                        .andExpect(jsonPath("$.name").value(USER_RESPONSE_DTO.name()))
                        .andExpect(jsonPath("$.email").value(USER_RESPONSE_DTO.email()))
                        .andExpect(jsonPath("$.roles").exists());
            }

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/users/me/email - when email is not valid")
            void updateEmail_returnBadRequest400_whenEmailIsNotValid() throws Exception {
                mockMvc.perform(patch("/users/me/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(UPDATE_INVALID_EMAIL_USER_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/users/me/password - when update is successful")
            void updatePassword_returnNotContent204_whenIsSuccessful() throws Exception {
                mockMvc.perform(patch("/users/me/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(UPDATE_PASSWORD_USER_JSON))
                        .andExpect(status().isNoContent());
            }

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/users/me/password - when password is invalid")
            void updatePassword_returnBadRequest400_whenPasswordIsInvalid() throws Exception {
                mockMvc.perform(patch("/users/me/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(UPDATE_INVALID_PASSWORD_USER_JSON))
                        .andExpect(status().isBadRequest());
            }

        }

    }

    @Nested
    @DisplayName("Deletes")
    class DeleteTest {

        @Nested
        @DisplayName("Admin")
        class DeleteAdmin {

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/admin/users/{id} - when is successful")
            void deleteUserById_returnNotContent204_whenIsSuccessful() throws Exception {
                when(userService.findUserById(ID_1)).thenReturn(UserTestFactory.buildTestUser1());

                mockMvc.perform(delete("/admin/users/{id}", ID_1))
                        .andExpect(status().isNoContent());
            }

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/admin/users/{id} - when user not exists")
            void deleteUserById_returnNotFound404_whenUserFoundNotExists() throws Exception {
                when(userService.findUserById(ID_NOT_EXIST))
                        .thenThrow(new UserNotFoundException(Long.toString(ID_NOT_EXIST)));

                mockMvc.perform(delete("/admin/users/{id}", ID_NOT_EXIST))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.title").value(TITLE_USER_NOT_FOUND))
                        .andExpect(jsonPath("$.detail").exists());
            }

        }

        @Nested
        @DisplayName("User")
        class DeleteUser {

            @Test
            @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
            @DisplayName("/users/me - when is successful")
            void deleteUser_returnNotContent204_whenIsSuccessful() throws Exception {
                mockMvc.perform(delete("/users/me"))
                        .andExpect(status().isNoContent());
            }

        }

    }
}
