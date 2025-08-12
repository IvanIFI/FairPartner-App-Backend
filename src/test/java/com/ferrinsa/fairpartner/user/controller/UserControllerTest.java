package com.ferrinsa.fairpartner.user.controller;

import com.ferrinsa.fairpartner.exception.user.UserNotFoundException;
import com.ferrinsa.fairpartner.user.model.User;
import com.ferrinsa.fairpartner.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;


@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String NAME = "ivan";
    private static final String EMAIL = "ivan@example.com";
    private static final String BAD_EMAIL = "badEmail@example.com";
    private static final String INVALID_EMAIL = "invalidEmail.com";
    private static final User USER = new User(NAME, EMAIL, "pswd", LocalDate.of(2024, 1, 1));

    private static final String USER_NOT_FOUND_TITLE = "Usuario no encontrado" ;
    private static final String INVALID_EMAIL_TITLE = "Parámetros inválidos";
    private static final String APPLICATION_PROBLEM_JSON = "application/problem+json";

    @Autowired private MockMvc mockMvc;
    @MockitoBean private UserService userService;

    @Nested
    @DisplayName("Gets")
    class GetTest {

        @Test
        @DisplayName("/users/{email} Found")
        void getUserByEmail_returnUserLoginDTO_whenEmailMatches() throws Exception {
            when(userService.findUserByEmail(EMAIL)).thenReturn(USER);

            mockMvc.perform(get("/users/{email}", EMAIL)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.email").value(EMAIL))
                    .andExpect(jsonPath("$.password").value(USER.getPassword()));

            verify(userService).findUserByEmail(EMAIL);
        }

        @Test
        @DisplayName("/users/{email} Not found")
        void getUserByEmail_returnJsonProblem_whenEmailNotMatches() throws Exception {
            when(userService.findUserByEmail(BAD_EMAIL)).thenThrow(new UserNotFoundException(BAD_EMAIL));

            mockMvc.perform(get("/users/{email}", BAD_EMAIL)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value(USER_NOT_FOUND_TITLE))
                    .andExpect(jsonPath("$.detail").exists());

            verify(userService).findUserByEmail(BAD_EMAIL);
        }

        @Test
        @DisplayName("/users/{email} Not valid email format")
        void getUserByEmail_returnJsonProblem_whenEmailNotValid() throws Exception {
            mockMvc.perform(get("/users/{email}", INVALID_EMAIL)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value(INVALID_EMAIL_TITLE))
                    .andExpect(jsonPath("$.detail").exists());
        }
    }

    @Nested
    @DisplayName("Posts")
    class PostTest {

    }

}