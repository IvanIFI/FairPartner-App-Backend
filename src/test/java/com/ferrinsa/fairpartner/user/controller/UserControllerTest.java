
package com.ferrinsa.fairpartner.user.controller;


import com.ferrinsa.fairpartner.security.config.TestSecurityConfig;
import com.ferrinsa.fairpartner.security.config.TestUserDetailsServiceConfig;
import com.ferrinsa.fairpartner.security.jwt.JwtTokenFilter;
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
import static com.ferrinsa.fairpartner.user.util.ExceptionsTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.UserTestConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.Mockito.verify;


@Import({TestSecurityConfig.class, TestUserDetailsServiceConfig.class})
@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtTokenFilter.class
        )
)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;

    // TODO AÑADIR UN BEFOREEACH CON EL USEREXPETED???
    // Cuanta veces se usa  el metodo de build????? dependiendo de eso o lo dejas con la llamad directa al metodo o hace el beforeeach

    @Nested
    @DisplayName("Gets")
    class GetTest {

        @Test
        @DisplayName("/users - When users exist")
        void getAllUsers_returnListUsers_whenUsersExist() throws Exception {
            when(userService.findAllUsers()).thenReturn(UserTestFactory.buildListTestUsers());

            mockMvc.perform(get("/users")
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
        @DisplayName("/users - When no users exist")
        void getAllUsers_returnEmptyList_whenNoUsersExist() throws Exception {
            when(userService.findAllUsers()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/users")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(userService).findAllUsers();
        }

        @Test
        @DisplayName("/users/{id} - Found")
        void getUserById_returnUserAdminResponseDTO_whenIdMatches() throws Exception {
            when(userService.findUserById(ID_1)).thenReturn(UserTestFactory.buildTestUser());

            mockMvc.perform(get("/users/{id}", ID_1)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(ID_1))
                    .andExpect(jsonPath("$.name").value(NAME_1))
                    .andExpect(jsonPath("$.email").value(EMAIL_1))
                    .andExpect(jsonPath("$.password").value(ENCODED_PASSWORD))
                    .andExpect(jsonPath("$.roles[0]").value(ROLE_USER.getRoleName()));

            verify(userService).findUserById(ID_1);
        }

        @Test
        @DisplayName("/users/{email} - Found")
        void getUserByEmail_returnUserAdminResponseDTO_whenEmailMatches() throws Exception {
            when(userService.findUserByEmail(EMAIL_1)).thenReturn(UserTestFactory.buildTestUser());

            mockMvc.perform(get("/users/email")
                            .param("email",EMAIL_1)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(ID_1))
                    .andExpect(jsonPath("$.name").value(NAME_1))
                    .andExpect(jsonPath("$.email").value(EMAIL_1))
                    .andExpect(jsonPath("$.password").value(ENCODED_PASSWORD))
                    .andExpect(jsonPath("$.roles[0]").value(ROLE_USER.getRoleName()));

            verify(userService).findUserByEmail(EMAIL_1);
        }

        @Test
        @DisplayName("/users/{email} - Not found")
        void getUserByEmail_returnJsonProblem_whenEmailNotMatches() throws Exception {
            when(userService.findUserByEmail(INVALID_EMAIL)).thenThrow(new UsernameNotFoundException(INVALID_EMAIL));

            mockMvc.perform(get("/users/email")
                            .param("email",INVALID_EMAIL)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value(USER_NOT_FOUND_TITLE))
                    .andExpect(jsonPath("$.detail").exists());

            verify(userService).findUserByEmail(INVALID_EMAIL);
        }

        @Test
        @DisplayName("/users/{email} - Not valid email format")
        void getUserByEmail_returnJsonProblem_whenEmailNotValid() throws Exception {
            mockMvc.perform(get("/users/email")
                            .param("email",INVALID_FORMAT_EMAIL)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value(INVALID_EMAIL_TITLE))
                    .andExpect(jsonPath("$.detail").exists());
        }

        @Test
        @WithUserDetails(value = "ivan@example.com", userDetailsServiceBeanName = "customUserDetailsServiceTest")
        @DisplayName("/users/me - when is authenticated ")
        void getMe_returnUser_whenUserIsLogin() throws Exception {
            mockMvc.perform(get("/users/me")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.email").value(EMAIL_1))
                    .andExpect(jsonPath("$.roles[0]").value(ROLE_USER.getRoleName()));
        }

    }

    @Nested
    @DisplayName("Posts")
    class PostTest {


    }
}
