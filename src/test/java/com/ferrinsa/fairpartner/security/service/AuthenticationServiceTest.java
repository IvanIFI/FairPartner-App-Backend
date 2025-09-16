package com.ferrinsa.fairpartner.security.service;

import com.ferrinsa.fairpartner.security.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.security.dto.LoginResponseDTO;
import com.ferrinsa.fairpartner.security.jwt.JwtTokenProvider;
import com.ferrinsa.fairpartner.security.util.UserAuthTestFactory;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.util.UserTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static com.ferrinsa.fairpartner.security.util.TokenTestConstants.FAKE_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock JwtTokenProvider jwtTokenProvider;
    @Mock AuthenticationManager authenticationManager;
    @InjectMocks AuthenticationService authenticationService;

    private UserEntity user;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        user = UserTestFactory.buildTestUser1();
        authentication = UserAuthTestFactory.buildUserPassAuthToken();
    }

    @Test
    @DisplayName("Successful Authentication")
    void authenticateByEmail_returnLoginResponseDTO_whenAuthenticationIsSuccessful() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(FAKE_TOKEN);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(user.getEmail(),user.getPassword());
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.of(user,FAKE_TOKEN);

        assertEquals(loginResponseDTO,authenticationService.authenticateByEmail(loginRequestDTO));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
    }

    @Test
    @DisplayName("Failed Authentication")
    void authenticateByEmail_throwBadCredentialsException_whenAuthenticationFails() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(user.getEmail(),user.getPassword());

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateByEmail(loginRequestDTO));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtTokenProvider);
    }
}