package com.ferrinsa.fairpartner.user.util;

import com.ferrinsa.fairpartner.security.dto.LoginRequestDTO;
import com.ferrinsa.fairpartner.security.dto.LoginResponseDTO;
import com.ferrinsa.fairpartner.security.role.model.RoleEntity;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;
import com.ferrinsa.fairpartner.user.dto.RegisterUserRequestDTO;
import com.ferrinsa.fairpartner.user.dto.UserResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class UserTestConstants {

    public static final Long ID_1 = 1L;
    public static final Long ID_2 = 2L;

    public static final String NAME_1 = "Ivan";
    public static final String NAME_2= "Eva";

    public static final String EMAIL_1 = "ivan@example.com";
    public static final String EMAIL_2 = "eva@example.com";
    public static final String INVALID_EMAIL = "badEmail@example.com";
    public static final String INVALID_FORMAT_EMAIL = "invalidEmail.com";

    public static final String PASSWORD = "12345678";
    public static final String INVALID_PASSWORD = "87654321";
    public static final String ENCODED_PASSWORD = "$2a$10$abcDEFghiJKLmnopQRstu";

    public static final LocalDate DEFAULT_DATE = LocalDate.of(2025, 1, 1);

}
