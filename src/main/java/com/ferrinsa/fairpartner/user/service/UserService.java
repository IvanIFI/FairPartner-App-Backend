package com.ferrinsa.fairpartner.user.service;

import com.ferrinsa.fairpartner.exception.user.*;
import com.ferrinsa.fairpartner.security.role.service.RoleService;
import com.ferrinsa.fairpartner.user.dto.*;
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
import java.util.stream.Collectors;

@Service
public class UserService {

    public static final String EMPTY_NAME = "El nombre no puede estar vacío";

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

    public UserEntity findUserById(Long id) {
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
        newUser.setPasswordHash(passwordEncoder.encode(registerUserRequestDTO.password()));
        newUser.setRoles(Set.of(roleService.findRoleByName(UserRoles.USER)));
        userRepository.save(newUser);

        return UserResponseDTO.of(newUser);
    }

    @Transactional
    public UserResponseDTO updateNameUser(UserEntity authUser, UpdateUserNameRequestDTO updateUserNameRequestDTO) {
        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new UserNotFoundException(Long.toString(authUser.getId())));

        boolean hasName = updateUserNameRequestDTO.name() != null && !updateUserNameRequestDTO.name().isBlank();

        if (!hasName) {
            throw new UserFailedUpdateProfileException(EMPTY_NAME);
        }

        user.setName(updateUserNameRequestDTO.name());
        userRepository.save(user);

        return UserResponseDTO.of(user);
    }

    @Transactional
    public UserResponseDTO updateEmailUser(UserEntity authUser, UpdateUserEmailRequestDTO updateUserEmailRequestDTO) {
        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new UserNotFoundException(Long.toString(authUser.getId())));

        boolean emailAlreadyExists = userRepository.findByEmail(updateUserEmailRequestDTO.email()).isPresent();

        if (emailAlreadyExists) {
            throw new UserEmailAlreadyExistsException();
        }

        user.setEmail(updateUserEmailRequestDTO.email());
        userRepository.save(user);

        return UserResponseDTO.of(user);
    }

    @Transactional
    public void deleteUser(UserEntity authUser) {
        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new UserNotFoundException(Long.toString(authUser.getId())));

        userRepository.delete(user);
    }

    @Transactional
    public void updatePasswordUser(UserEntity authUser, UpdateUserPasswordRequestDTO updateUserPasswordRequestDTO) {
        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new UserNotFoundException(Long.toString(authUser.getId())));

        validatePasswordChange(user, updateUserPasswordRequestDTO);

        user.setPasswordHash(passwordEncoder.encode(updateUserPasswordRequestDTO.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserAdminResponseDTO updateUserToAdmin(Long id, UserUpdateAdminRequestDTO userUpdateAdminRequestDTO) {
        UserEntity userToUpdate = findUserById(id);

        userToUpdate.setName(userUpdateAdminRequestDTO.name());
        userToUpdate.setEmail(userUpdateAdminRequestDTO.email());
        userToUpdate.setRoles(userUpdateAdminRequestDTO.roles().stream()
                .map(roleName -> roleService.findRoleByName(UserRoles.valueOf(roleName)))
                .collect(Collectors.toSet()));

        return UserAdminResponseDTO.of(userToUpdate);
    }

    private void validatePasswordChange(UserEntity authUser, UpdateUserPasswordRequestDTO updateUserPassRequestDto) {
        if (!passwordEncoder.matches(updateUserPassRequestDto.oldPassword(), authUser.getPassword())) {
            throw new UserPasswordCheckException();
        }

        if (!updateUserPassRequestDto.newPassword().equals(updateUserPassRequestDto.confirmNewPassword())) {
            throw new UserPasswordException();
        }
    }
}

