package com.ferrinsa.fairpartner.user.controller;

import com.ferrinsa.fairpartner.user.dto.UserAdminDTO;
import com.ferrinsa.fairpartner.user.dto.UserLoginDTO;
import com.ferrinsa.fairpartner.user.model.User;
import com.ferrinsa.fairpartner.user.service.UserService;
import jakarta.validation.constraints.Email;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService service) {
        this.userService = service;
    }

    @GetMapping()
    public List<UserAdminDTO> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(UserAdminDTO::of)
                .toList();
    }

    @GetMapping("/{email}")
    public UserLoginDTO getUserByEmail(@PathVariable @Email String email) {
        return UserLoginDTO.of(userService.findUserByEmail(email));
    }


}
