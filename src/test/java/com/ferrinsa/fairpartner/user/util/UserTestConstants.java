package com.ferrinsa.fairpartner.user.util;

import java.time.LocalDate;

public class UserTestConstants {

    public static final Long ID_1 = 1L;
    public static final Long ID_2 = 2L;
    public static final Long ID_NOT_EXIST = 223344L;

    public static final String NAME_1 = "Ivan";
    public static final String NAME_2= "Eva";

    public static final String EMAIL_1 = "ivan@example.com";
    public static final String EMAIL_2 = "eva@example.com";
    public static final String INVALID_EMAIL = "badEmail@example.com";
    public static final String INVALID_FORMAT_EMAIL = "invalidEmail.com";

    public static final String PASSWORD = "Abcd1234!";
    public static final String NEW_PASSWORD = "Xyz7890@";
    public static final String INVALID_PASSWORD = "87654321";
    public static final String HASHED_PASSWORD_1 = "$2a$10$abcDEFghiJKLmnopQRstu";
    public static final String HASHED_PASSWORD_2 = "$2a$10$VWxyzABCdefGHIjklMNopq";

    public static final LocalDate DEFAULT_DATE = LocalDate.of(2025, 1, 1);

}
