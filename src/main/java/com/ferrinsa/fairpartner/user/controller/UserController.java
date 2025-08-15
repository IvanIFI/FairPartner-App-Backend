package com.ferrinsa.fairpartner.user.controller;

import com.ferrinsa.fairpartner.user.dto.NewUserDTO;
import com.ferrinsa.fairpartner.user.dto.UserAdminDTO;
import com.ferrinsa.fairpartner.user.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.user.dto.UserLoginResponseDTO;
import com.ferrinsa.fairpartner.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService service) {
        this.userService = service;
    }

    //ADMIN
    @GetMapping()
    public List<UserAdminDTO> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(UserAdminDTO::of)
                .toList();
    }

    //ADMIN cambiar a email or id or name??
    @GetMapping("/{email}")
    public UserAdminDTO getUserByEmail(@PathVariable @Email String email) {
        return UserAdminDTO.of(userService.findUserByEmail(email));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserLoginResponseDTO> loginUserByEmail(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        UserLoginResponseDTO userLoginResponseDTO = userService.loginValidateUser(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userLoginResponseDTO);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserLoginResponseDTO> createNewUser (@Valid @RequestBody NewUserDTO newUserDTO){
        UserLoginResponseDTO userLoginResponseDTO = userService.createNewUser(newUserDTO);
        return ResponseEntity
                .created(URI.create("/api/users/" + userLoginResponseDTO.id()))
                .body(userLoginResponseDTO);
    }


}
