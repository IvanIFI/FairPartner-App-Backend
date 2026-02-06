package com.ferrinsa.fairpartner.user.controller;

import com.ferrinsa.fairpartner.user.dto.*;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal UserEntity authUser) {
        return ResponseEntity.ok(UserResponseDTO.of(authUser));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/name")
    public ResponseEntity<UserResponseDTO> updateName(@AuthenticationPrincipal UserEntity authUser,
                                                      @Valid @RequestBody UpdateUserNameRequestDTO updateNameRequestDTO) {
        return ResponseEntity.ok(userService.updateNameUser(authUser, updateNameRequestDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/email")
    public ResponseEntity<UserResponseDTO> updateEmail(@AuthenticationPrincipal UserEntity authUser,
                                                       @Valid @RequestBody UpdateUserEmailRequestDTO updateEmailRequestDTO) {
        return ResponseEntity.ok(userService.updateEmailUser(authUser, updateEmailRequestDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserEntity authUser,
                                               @Valid @RequestBody UpdateUserPasswordRequestDTO updatePassRequestDTO) {
        userService.updatePasswordUser(authUser, updatePassRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserEntity authUser) {
        userService.deleteUser(authUser);
        return ResponseEntity.noContent().build();
    }

}
