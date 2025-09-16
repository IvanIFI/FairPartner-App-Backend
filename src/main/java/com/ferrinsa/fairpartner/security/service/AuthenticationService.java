package com.ferrinsa.fairpartner.security.service;


import com.ferrinsa.fairpartner.security.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.security.dto.LoginResponseDTO;
import com.ferrinsa.fairpartner.security.jwt.JwtTokenProvider;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthenticationService(AuthenticationManager authManager, JwtTokenProvider jwtTokenProvider) {
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseDTO authenticateByEmail(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity loginUser = (UserEntity) authentication.getPrincipal();
        return LoginResponseDTO.of(loginUser, jwtTokenProvider.generateToken(authentication));
    }

}
