package com.ferrinsa.fairpartner.security.controller;

import com.ferrinsa.fairpartner.exception.user.UserEmailAlreadyExistsException;
import com.ferrinsa.fairpartner.exception.user.UserLoginFailedException;
import com.ferrinsa.fairpartner.security.config.TestSecurityConfig;
import com.ferrinsa.fairpartner.security.config.TestUserDetailsServiceConfig;
import com.ferrinsa.fairpartner.security.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.security.jwt.JwtTokenFilter;
import com.ferrinsa.fairpartner.security.jwt.JwtTokenProvider;
import com.ferrinsa.fairpartner.security.service.AuthenticationService;
import com.ferrinsa.fairpartner.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.ferrinsa.fairpartner.security.util.RolesTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.DtosTestConstatns.*;
import static com.ferrinsa.fairpartner.user.util.ExceptionsTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.JsonTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.UserTestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Import({TestSecurityConfig.class, TestUserDetailsServiceConfig.class})
@WebMvcTest(
        controllers = AuthenticationController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtTokenFilter.class
        )
)
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthenticationService authenticationService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private AuthenticationManager authenticationManager;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("/auth/login - Login successful")
    void loginUserByEmail_returnOk200_whenLoginIsSuccessful() throws Exception {
        when(authenticationService.authenticateByEmail(LOGIN_REQUEST_DTO)).thenReturn(LOGIN_RESPONSE_DTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(VALID_LOGIN_USER_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(NAME_2))
                .andExpect(jsonPath("$.email").value(EMAIL_2))
                .andExpect(jsonPath("$.roles[0]").value(ROLE_USER.getRoleName()))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("/auth/login - Fail login bad credentials")
    void loginUserByEmail_returnUnauthorized401_whenLoginFail() throws Exception {
        when(authenticationService.authenticateByEmail(any(LoginRequestDTO.class)))
                .thenThrow(new UserLoginFailedException());

        mockMvc.perform(post("/auth/login")
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
        when(userService.registerNewUser(REGISTER_REQUEST_DTO)).thenReturn(USER_RESPONSE_DTO);
        when(authenticationService.authenticateByEmail(LOGIN_REQUEST_DTO)).thenReturn(LOGIN_RESPONSE_DTO);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(SIGN_UP_USER_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID_2))
                .andExpect(jsonPath("$.name").value(NAME_2))
                .andExpect(jsonPath("$.email").value(EMAIL_2))
                .andExpect(jsonPath("$.roles[0]").value(ROLE_USER.getRoleName()))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("/auth/register - Fail SignUp Email already exists")
    void registerUser_returnConflict409_whenEmailAlreadyExists() throws Exception {
        when(userService.registerNewUser(REGISTER_REQUEST_DTO))
                .thenThrow(new UserEmailAlreadyExistsException());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(SIGN_UP_USER_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value(EMAIL_ALREADY_EXISTS_TITLE))
                .andExpect(jsonPath("$.detail").exists());

    }
}
