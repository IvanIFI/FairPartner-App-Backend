package com.ferrinsa.fairpartner.user.util;

import com.ferrinsa.fairpartner.security.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.security.dto.LoginResponseDTO;
import com.ferrinsa.fairpartner.user.dto.RegisterUserRequestDTO;
import com.ferrinsa.fairpartner.user.dto.UserResponseDTO;

import static com.ferrinsa.fairpartner.security.util.RolesTestConstants.*;
import static com.ferrinsa.fairpartner.security.util.TokenTestConstants.*;
import static com.ferrinsa.fairpartner.user.util.UserTestConstants.*;


public class DtosTestConstatns {
    public static final RegisterUserRequestDTO REGISTER_REQUEST_DTO = new RegisterUserRequestDTO(NAME_2,EMAIL_2,PASSWORD);
    public static final LoginRequestDTO LOGIN_REQUEST_DTO = new LoginRequestDTO(EMAIL_2,PASSWORD);
    public static final LoginResponseDTO LOGIN_RESPONSE_DTO = new LoginResponseDTO(ID_2,NAME_2,EMAIL_2,DEFAULT_ROLE_LIST,FAKE_TOKEN);
    public static final UserResponseDTO USER_RESPONSE_DTO = new UserResponseDTO(ID_2,NAME_2,EMAIL_2,DEFAULT_ROLE_LIST);
}
