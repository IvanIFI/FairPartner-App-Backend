package com.ferrinsa.fairpartner.security.jwt;

import com.ferrinsa.fairpartner.user.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static com.ferrinsa.fairpartner.security.util.TokenTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.UserTestFactory.buildTestUser;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private UsernamePasswordAuthenticationToken authUser;

    @BeforeEach
    void setUp() {
        UserEntity user = buildTestUser();
        jwtTokenProvider = new JwtTokenProvider(TEST_GENERATE_TOKEN_KEY,  TEST_GENERATE_TOKEN_TIME_EXPIRED);
        authUser = new UsernamePasswordAuthenticationToken(user, null, List.of());
    }

    @Test
    @DisplayName("Successful token generate")
    void generateToken_ReturnValidToken() {
        String token = jwtTokenProvider.generateToken(authUser);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.isValidToken(token));
        assertEquals(1L, jwtTokenProvider.getIdUserFromToken(token));
    }

    @Test
    @DisplayName("Validate failed for invalid token")
    void isValidToken_ReturnFalse_WhenTokenIsInvalid() {
        String token = jwtTokenProvider.generateToken(authUser);
        String invalidToken = token.substring(0, token.length() - 1);

        assertFalse(jwtTokenProvider.isValidToken(invalidToken));
    }

    @Test
    @DisplayName("Validate failed for token time expired")
    void isValidToken_ReturnFalse_WhenTokenIsExpired() {
        String token = jwtTokenProvider.generateToken(authUser);
        await().atMost(3, SECONDS).until(() -> !jwtTokenProvider.isValidToken(token));

        assertFalse(jwtTokenProvider.isValidToken(token));
    }

    @Test
    @DisplayName("Successful get Id from Token")
    void getIdUserFromToken_returnId_whenParserIsSuccessful (){
        UserEntity userPrincipal = (UserEntity) authUser.getPrincipal();
        Long userId = userPrincipal.getId();
        String token = jwtTokenProvider.generateToken(authUser);

        assertEquals(userId,jwtTokenProvider.getIdUserFromToken(token));
    }
}
