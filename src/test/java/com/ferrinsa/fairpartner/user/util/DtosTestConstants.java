package com.ferrinsa.fairpartner.user.util;

import com.ferrinsa.fairpartner.security.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.security.dto.LoginResponseDTO;
import com.ferrinsa.fairpartner.security.dto.RegisterUserRequestDTO;
import com.ferrinsa.fairpartner.user.dto.*;

import java.util.List;

import static com.ferrinsa.fairpartner.security.util.RolesTestConstants.*;
import static com.ferrinsa.fairpartner.security.util.TokenTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.UserTestConstants.*;


public class DtosTestConstants {

    public static final RegisterUserRequestDTO REGISTER_REQUEST_DTO =
            new RegisterUserRequestDTO(NAME_2, EMAIL_2, PASSWORD);

    public static final LoginRequestDTO LOGIN_REQUEST_DTO =
            new LoginRequestDTO(EMAIL_2, PASSWORD);

    public static final LoginResponseDTO LOGIN_RESPONSE_DTO =
            new LoginResponseDTO(
                    ID_2,
                    NAME_2,
                    EMAIL_2,
                    DEFAULT_ROLE_LIST,
                    FAKE_TOKEN
            );

    public static final UserResponseDTO USER_RESPONSE_DTO =
            new UserResponseDTO(ID_2, NAME_2, EMAIL_2, DEFAULT_ROLE_LIST);

    public static final UpdateUserNameRequestDTO UPDATE_USER_NAME_REQUEST_DTO =
            new UpdateUserNameRequestDTO(NAME_2);

    public static final UpdateUserNameRequestDTO INVALID_UPDATE_USER_NAME_REQUEST_DTO =
            new UpdateUserNameRequestDTO("");

    public static final UpdateUserEmailRequestDTO UPDATE_USER_EMAIL_REQUEST_DTO =
            new UpdateUserEmailRequestDTO(EMAIL_2);

    public static final UpdateUserPasswordRequestDTO UPDATE_USER_PASSWORD_REQUEST_DTO =
            new UpdateUserPasswordRequestDTO(
                    HASHED_PASSWORD_1,
                    NEW_PASSWORD,
                    NEW_PASSWORD
            );

    public static final UpdateUserPasswordRequestDTO INVALID_UPDATE_USER_PASSWORD_REQUEST_DTO =
            new UpdateUserPasswordRequestDTO(
                    HASHED_PASSWORD_1,
                    NEW_PASSWORD,
                    "failPasswordConfirm"
            );

    public static final UserUpdateAdminRequestDTO USER_UPDATE_ADMIN_REQUEST_DTO =
            new UserUpdateAdminRequestDTO(
                    NAME_2,
                    EMAIL_2,
                    ADMIN_ROLE_LIST
            );

    public static final UserUpdateAdminRequestDTO INVALID_ROLE_USER_UPDATE_ADMIN_REQUEST_DTO =
            new UserUpdateAdminRequestDTO(
                    NAME_2,
                    EMAIL_2,
                    List.of("INVALID_ROLE")
            );

    public static final UserAdminResponseDTO USER_ADMIN_RESPONSE_DTO =
            new UserAdminResponseDTO(
                    ID_1,
                    NAME_1,
                    EMAIL_1,
                    HASHED_PASSWORD_1,
                    DEFAULT_DATE,
                    DEFAULT_ROLE_LIST
            );
}

