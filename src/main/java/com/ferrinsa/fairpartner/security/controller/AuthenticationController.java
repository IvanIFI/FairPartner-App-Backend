package com.ferrinsa.fairpartner.security.controller;

import com.ferrinsa.fairpartner.security.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.security.dto.LoginResponseDTO;
import com.ferrinsa.fairpartner.security.jwt.JwtTokenProvider;
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

    @Autowired
    public AuthenticationController(AuthenticationManager authManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> registerAndLoginNewUser(@Valid @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        userService.registerNewUser(registerUserRequestDTO);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(registerUserRequestDTO.email(), registerUserRequestDTO.password());

        return loginByEmail(loginRequestDTO);
    }


    //FIXME: no deberia aplicar esta logica al servicio?
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginByEmail(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authenticacion = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(authenticacion);

        UserEntity user = (UserEntity) authenticacion.getPrincipal();
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.of(user, jwtTokenProvider.generateToken(authenticacion));

        return ResponseEntity.ok(loginResponseDTO);
    }
}
