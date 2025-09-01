package com.ferrinsa.fairpartner.user.controller;

import com.ferrinsa.fairpartner.user.dto.UpdateUserEmailRequestDTO;
import com.ferrinsa.fairpartner.user.dto.UpdateUserNameRequestDTO;
import com.ferrinsa.fairpartner.user.dto.UserAdminResponseDTO;
import com.ferrinsa.fairpartner.user.dto.UserResponseDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.service.UserService;
import jakarta.validation.Valid;
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
    @GetMapping("/{id}")
    public UserAdminResponseDTO getUserById(@PathVariable Long id) {
        return UserAdminResponseDTO.of(userService.findUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email")
    public UserAdminResponseDTO getUserByEmail(@RequestParam @Email String email) {
        return UserAdminResponseDTO.of(userService.findUserByEmail(email));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(UserResponseDTO.of(user));
    }

    // TODO: Falta testear metodos a partir de aquí

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/name")
    public ResponseEntity<UserResponseDTO> updateName(@AuthenticationPrincipal UserEntity authUser,
                                                      @Valid @RequestBody UpdateUserNameRequestDTO updateUserNameRequestDTO) {
        return ResponseEntity.ok(userService.updateNameUser(authUser,updateUserNameRequestDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/email")
    public ResponseEntity<UserResponseDTO> updateEmail(@AuthenticationPrincipal UserEntity authUser,
                                                      @Valid @RequestBody UpdateUserEmailRequestDTO updateUserRequestDTO) {
        return ResponseEntity.ok(userService.updateEmailUser(authUser,updateUserRequestDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserEntity authUser){
        return ResponseEntity.noContent().build();
    }



}
