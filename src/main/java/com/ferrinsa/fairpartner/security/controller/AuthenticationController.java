package com.ferrinsa.fairpartner.security.controller;

import com.ferrinsa.fairpartner.security.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.security.dto.LoginResponseDTO;
import com.ferrinsa.fairpartner.security.jwt.JwtTokenProvider;
import com.ferrinsa.fairpartner.security.service.AuthenticationService;
import com.ferrinsa.fairpartner.user.dto.RegisterUserRequestDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationManager authManager, JwtTokenProvider jwtTokenProvider, UserService userService, AuthenticationService authenticationService) {
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> registerAndLoginNewUser(@Valid @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        userService.registerNewUser(registerUserRequestDTO);
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(registerUserRequestDTO.email(), registerUserRequestDTO.password());

        return loginByEmail(loginRequestDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginByEmail(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authenticationService.authenticateByEmail(loginRequestDTO));
    }
}
