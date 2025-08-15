package com.ferrinsa.fairpartner.user.service;

import com.ferrinsa.fairpartner.exception.user.UserEmailAlreadyExistsException;
import com.ferrinsa.fairpartner.exception.user.UserLoginFailedException;
import com.ferrinsa.fairpartner.exception.user.UserNotFoundException;
import com.ferrinsa.fairpartner.user.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.user.dto.NewUserDTO;
import com.ferrinsa.fairpartner.user.dto.UserLoginResponseDTO;
import com.ferrinsa.fairpartner.user.model.User;
import com.ferrinsa.fairpartner.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    //FIXME: Crear un generador de tokens (solo para pruebas)
    private static final String TOKEN = "12345";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    // FIXME: Aqui se usa la variable TOKEN que hay que eliminar
    public UserLoginResponseDTO loginValidateUser(LoginRequestDTO loginRequestDTO) {
        User userSearchToLogin = userRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(UserLoginFailedException::new);

        if (!passwordEncoder.matches(loginRequestDTO.password(),userSearchToLogin.getPassword())) {
            throw new UserLoginFailedException();
        }

        return UserLoginResponseDTO.of(userSearchToLogin,TOKEN);
    }

    // FIXME: Aqui se usa la variable TOKEN que hay que eliminar
    public UserLoginResponseDTO createNewUser(NewUserDTO newUserDTO){
        if (userRepository.existsByEmail(newUserDTO.email())) {
            throw new UserEmailAlreadyExistsException(); // mapear a 409
        }

        User newUser = new User();
        newUser.setName(newUserDTO.name());
        newUser.setEmail(newUserDTO.email());
        newUser.setPassword(passwordEncoder.encode(newUserDTO.password()));
        userRepository.save(newUser);

        return UserLoginResponseDTO.of(newUser, TOKEN);
    }



}

