package com.ferrinsa.fairpartner.user.service;

import com.ferrinsa.fairpartner.exception.user.*;
import com.ferrinsa.fairpartner.security.dto.RegisterUserRequestDTO;
import com.ferrinsa.fairpartner.security.role.service.RoleService;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;
import com.ferrinsa.fairpartner.user.dto.*;
import com.ferrinsa.fairpartner.user.model.UserEntity;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Long.toString(id)));
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    @Transactional
    public UserResponseDTO registerNewUser(RegisterUserRequestDTO registerUserRequestDTO) {
        if (userRepository.existsByEmail(registerUserRequestDTO.email())) {
            throw new UserEmailAlreadyExistsException();
        }

        UserEntity newUser = new UserEntity(
                registerUserRequestDTO.name(),
                registerUserRequestDTO.email(),
                passwordEncoder.encode(registerUserRequestDTO.password())
        );

        newUser.setRoles(Set.of(roleService.findRoleByName(UserRoles.USER)));
        userRepository.save(newUser);

        return UserResponseDTO.of(newUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateNameUser(UserEntity authUser, UpdateUserNameRequestDTO updateUserNameRequestDTO) {
        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new UserNotFoundException(Long.toString(authUser.getId())));

        user.setName(updateUserNameRequestDTO.name());
        userRepository.save(user);

        return UserResponseDTO.of(user);
    }

    @Override
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

    @Override
    @Transactional
    public void deleteUser(UserEntity authUser) {
        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new UserNotFoundException(Long.toString(authUser.getId())));

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void updatePasswordUser(UserEntity authUser, UpdateUserPasswordRequestDTO updateUserPasswordRequestDTO) {
        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new UserNotFoundException(Long.toString(authUser.getId())));

        validatePasswordChange(user, updateUserPasswordRequestDTO);

        user.setPasswordHash(passwordEncoder.encode(updateUserPasswordRequestDTO.newPassword()));
        userRepository.save(user);
    }

    @Override
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


    // TODO: Si se utiliza mas veces lo mejor sería crear una clase para reutilizarlo.
    private void validatePasswordChange(UserEntity authUser, UpdateUserPasswordRequestDTO updateUserPassRequestDto) {
        if (!passwordEncoder.matches(updateUserPassRequestDto.oldPassword(), authUser.getPassword())) {
            throw new UserPasswordCheckException();
        }

        if (!updateUserPassRequestDto.newPassword().equals(updateUserPassRequestDto.confirmNewPassword())) {
            throw new UserPasswordException();
        }
    }
}
