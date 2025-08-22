package com.ferrinsa.fairpartner.user.controller;

import com.ferrinsa.fairpartner.user.dto.UserAdminResponseDTO;
import com.ferrinsa.fairpartner.user.dto.UserResponseDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.service.UserService;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List<UserAdminResponseDTO> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(UserAdminResponseDTO::of)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{email}")
    public UserAdminResponseDTO getUserByEmail(@PathVariable @Email String email) {
        return UserAdminResponseDTO.of(userService.findUserByEmail(email));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal UserEntity user){
        return ResponseEntity.ok(UserResponseDTO.of(user));
    }

}
