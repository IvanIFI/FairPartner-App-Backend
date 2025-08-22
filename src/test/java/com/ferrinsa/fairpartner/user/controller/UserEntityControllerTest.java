/*
package com.ferrinsa.fairpartner.user.controller;

import com.ferrinsa.fairpartner.exception.user.UserEmailAlreadyExistsException;
import com.ferrinsa.fairpartner.exception.user.UserLoginFailedException;
import com.ferrinsa.fairpartner.security.TestSecurityConfig;
import com.ferrinsa.fairpartner.security.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.user.dto.RegisterUserDTO;
import com.ferrinsa.fairpartner.security.dto.LoginResponseDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.Mockito.verify;


@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserEntityControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "Ivan";
    private static final String EMAIL = "ivan@example.com";
    private static final String BAD_EMAIL = "badEmail@example.com";
    private static final String INVALID_EMAIL = "invalidEmail.com";
    private static final String PASSWORD = "12345678";
    private static final String WRONG_PASSWORD = "87654321";
    private static final String TOKEN = "TOKENTEST";

    private static final UserEntity USER = new UserEntity(NAME, EMAIL, PASSWORD);
    //private static final NewUserDTO NEW_USER_DTO = new NewUserDTO(NAME,EMAIL,PASSWORD);
    //private static final LoginRequestDTO LOGIN_REQUEST_DTO = new LoginRequestDTO(EMAIL,PASSWORD);

    private static final String USER_NOT_FOUND_TITLE = "Usuario no encontrado";
    private static final String INVALID_EMAIL_TITLE = "Parámetros inválidos";
    private static final String LOGIN_FAILED_TITLE = "Autenticación fallida";
    private static final String EMAIL_ALREADY_EXISTS_TITLE = "El email ya existe";

    //private static final String APPLICATION_PROBLEM_JSON = "application/problem+json";

    private static final String VALID_LOGIN_USER_JSON = """
                {
                    "email": "ivan@example.com",
                    "password": "12345678"
                }
            """;

    private static final String INVALID_LOGIN_EMAIL_USER_JSON = """
                {
                    "email": "badEmail@example.com",
                    "password": "87654321"
                }
            """;

    private static final String SIGN_UP_USER_JSON = """
                {
                    "name": "Ivan",
                    "email": "ivan@example.com",
                    "password": "12345678"
                }
            """;


    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;

    @Nested
    @DisplayName("Gets")
    class GetTest {

        @Test
        @DisplayName("/users/{email} - Found")
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
*/
/* // FIXME: UserNameNotFoundExcepction Change
        @Test
        @DisplayName("/users/{email} - Not found")
        void getUserByEmail_returnJsonProblem_whenEmailNotMatches() throws Exception {
            when(userService.findUserByEmail(BAD_EMAIL)).thenThrow(new UserNotFoundException(BAD_EMAIL));

            mockMvc.perform(get("/users/{email}", BAD_EMAIL)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value(USER_NOT_FOUND_TITLE))
                    .andExpect(jsonPath("$.detail").exists());

            verify(userService).findUserByEmail(BAD_EMAIL);
        }*//*


        @Test
        @DisplayName("/users/{email} - Not valid email format")
        void getUserByEmail_returnJsonProblem_whenEmailNotValid() throws Exception {
            mockMvc.perform(get("/users/{email}", INVALID_EMAIL)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value(INVALID_EMAIL_TITLE))
                    .andExpect(jsonPath("$.detail").exists());
        }
    }

    @Nested
    @DisplayName("Posts")
    class PostTest {

        @Test
        @DisplayName("/auth/login - Login successful")
        void loginUserByEmail_returnOk200_whenLoginIsSuccessful() throws Exception {
            when(userService.loginValidateUser((new LoginRequestDTO(EMAIL, PASSWORD)))).
                    thenReturn(new LoginResponseDTO(ID, NAME, EMAIL, TOKEN));

            mockMvc.perform(post("/users/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(VALID_LOGIN_USER_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Ivan"))
                    .andExpect(jsonPath("$.email").value("ivan@example.com"))
                    .andExpect(jsonPath("$.token").exists());
        }

        @Test
        @DisplayName("/auth/login - Fail login bad credentials")
        void loginUserByEmail_returnUnauthorized401_whenLoginFail() throws Exception {
            when(userService.loginValidateUser((new LoginRequestDTO(BAD_EMAIL, WRONG_PASSWORD)))).
                    thenThrow(new UserLoginFailedException());

            mockMvc.perform(post("/users/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(INVALID_LOGIN_EMAIL_USER_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value(LOGIN_FAILED_TITLE))
                    .andExpect(jsonPath("$.detail").exists());
        }

        @Test
        @DisplayName("/auth/register - Sign up successful")
        void registerUser_returnCreated201_whenSignUpIsSuccessful() throws Exception {
            when(userService.registerNewUser(new RegisterUserDTO(NAME, EMAIL, PASSWORD)))
                    .thenReturn(new LoginResponseDTO(ID, NAME, EMAIL, TOKEN));

            mockMvc.perform(post("/users/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(SIGN_UP_USER_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Ivan"))
                    .andExpect(jsonPath("$.email").value("ivan@example.com"))
                    .andExpect(jsonPath("$.token").exists());

        }

        @Test
        @DisplayName("/auth/register - Fail SignUp Email already exists")
        void registerUser_returnConflict409_whenEmailAlreadyExists() throws Exception {
            when(userService.registerNewUser(new RegisterUserDTO(NAME, EMAIL, PASSWORD)))
                    .thenThrow(new UserEmailAlreadyExistsException());

            mockMvc.perform(post("/users/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(SIGN_UP_USER_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value(EMAIL_ALREADY_EXISTS_TITLE))
                    .andExpect(jsonPath("$.detail").exists());

        }

    }
}*/
