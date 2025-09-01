package com.ferrinsa.fairpartner.security;

import com.ferrinsa.fairpartner.security.role.model.RoleEntity;
import com.ferrinsa.fairpartner.security.role.repository.RoleRepository;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.repository.UserRepository;
import com.ferrinsa.fairpartner.security.util.UserTestSecurityFactory;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static com.ferrinsa.fairpartner.security.util.EntryPointTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.JsonTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.UserTestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static UserEntity admin;
    private static UserEntity user;

    @BeforeAll
    static void setUpAll(
            @Autowired RoleRepository roleRepository,
            @Autowired UserRepository userRepository,
            @Autowired PasswordEncoder passwordEncoder) {
        RoleEntity adminRole = roleRepository.findByUserRole(UserRoles.ADMIN).orElseThrow();
        RoleEntity userRole = roleRepository.findByUserRole(UserRoles.USER).orElseThrow();

        admin = UserTestSecurityFactory.buildTestAdminToSecurityTest(passwordEncoder);
        admin.setRoles(Set.of(userRole, adminRole));
        userRepository.save(admin);

        user = UserTestSecurityFactory.buildTestUserToSecurityTest(passwordEncoder);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }

    @Test
    @DisplayName("/users/me - Unauthenticated user")
    void accessProtectedEndpoint_returnUnauthorized401_whenNoAuthentication() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("/users/me - Authenticated user")
    void accessProtectedEndpoint_return200_whenAuthenticationIsSuccessful() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_LOGIN_USER_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String token = JsonPath.parse(responseBody).read("$.token");

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("/users/email - Unauthorized user")
    void onlyAccessAdminEndpoint_returnUnauthorized401_whenUnauthorized() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_LOGIN_USER_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String tokenUSER = JsonPath.parse(responseBody).read("$.token");

        mockMvc.perform(get("/users/email")
                        .param("email",EMAIL_1)
                        .header("Authorization", "Bearer " + tokenUSER))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("/users/email - Authorized user")
    void onlyAccessAdminEndpoint_returnOk200_whenUserIsAuthorized() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_LOGIN_ADMIN_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String tokenAdmin = JsonPath.parse(responseBody).read("$.token");

        mockMvc.perform(get("/users/email")
                        .param("email",EMAIL_1)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(admin.getEmail()));
    }

    @Test
    @DisplayName("EntryPoint - Unauthorized response structure")
    void entryPoint_returnCustomUnauthorizedResponse_whenNoAuthentication() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value(ENTRY_POINT_TITLE))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value(ENTRY_POINT_DETAIL))
                .andExpect(jsonPath("$.properties.path").value("/users/me"));
    }



}
