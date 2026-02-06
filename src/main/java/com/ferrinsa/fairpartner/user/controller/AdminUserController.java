package com.ferrinsa.fairpartner.user.controller;

import com.ferrinsa.fairpartner.user.dto.UserAdminResponseDTO;
import com.ferrinsa.fairpartner.user.dto.UserUpdateAdminRequestDTO;
import com.ferrinsa.fairpartner.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
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

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(userService.findUserById(id));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserAdminResponseDTO> updateUserToAdmin(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateAdminRequestDTO userUpdateAdminRequestDTO) {
        return ResponseEntity.ok(userService.updateUserToAdmin(id, userUpdateAdminRequestDTO));
    }
}
