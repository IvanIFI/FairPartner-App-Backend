package com.ferrinsa.fairpartner.user.service;


import com.ferrinsa.fairpartner.security.dto.RegisterUserRequestDTO;
import com.ferrinsa.fairpartner.user.dto.*;
import com.ferrinsa.fairpartner.user.model.UserEntity;

import java.util.List;

public interface UserService {

    List<UserEntity> findAllUsers();

    UserEntity findUserById(Long id);

    UserEntity findUserByEmail(String email);

    UserResponseDTO registerNewUser(RegisterUserRequestDTO registerUserRequestDTO);

    UserResponseDTO updateNameUser(UserEntity authUser, UpdateUserNameRequestDTO updateUserNameRequestDTO);

    UserResponseDTO updateEmailUser(UserEntity authUser, UpdateUserEmailRequestDTO updateUserEmailRequestDTO);

    void deleteUser(UserEntity authUser);

    void updatePasswordUser(UserEntity authUser, UpdateUserPasswordRequestDTO updateUserPasswordRequestDTO);

    UserAdminResponseDTO updateUserToAdmin(Long id, UserUpdateAdminRequestDTO userUpdateAdminRequestDTO);
}

