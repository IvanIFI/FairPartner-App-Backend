package com.ferrinsa.fairpartner.user.service;

import com.ferrinsa.fairpartner.exception.user.UserEmailAlreadyExistsException;
import com.ferrinsa.fairpartner.exception.user.UserNotFoundException;
import com.ferrinsa.fairpartner.security.role.service.RoleService;
import com.ferrinsa.fairpartner.user.dto.RegisterUserRequestDTO;
import com.ferrinsa.fairpartner.user.dto.UserResponseDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;
import com.ferrinsa.fairpartner.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Long.toString(id)));
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Transactional
    public UserResponseDTO registerNewUser(RegisterUserRequestDTO registerUserRequestDTO) {
        if (userRepository.existsByEmail(registerUserRequestDTO.email())) {
            throw new UserEmailAlreadyExistsException();
        }
        UserEntity newUser = new UserEntity();
        newUser.setName(registerUserRequestDTO.name());
        newUser.setEmail(registerUserRequestDTO.email());
        newUser.setPassword(passwordEncoder.encode(registerUserRequestDTO.password()));
        newUser.setRoles(Set.of(roleService.findRoleByName(UserRoles.USER)));
        userRepository.save(newUser);

        return UserResponseDTO.of(newUser);
    }


}

