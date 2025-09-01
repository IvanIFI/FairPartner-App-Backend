package com.ferrinsa.fairpartner.user.util;

import static com.ferrinsa.fairpartner.user.util.UserTestConstants.*;

public class JsonTestConstants {
    public static final String VALID_LOGIN_ADMIN_JSON = String.format("""
        {
            "email": "%s",
            "password": "%s"
        }
        """, EMAIL_1, PASSWORD);

    public static final String VALID_LOGIN_USER_JSON = String.format("""
        {
            "email": "%s",
            "password": "%s"
        }
        """, EMAIL_2, PASSWORD);

    public static final String INVALID_LOGIN_EMAIL_USER_JSON = String.format("""
        {
            "email": "%s",
            "password": "%s"
        }
        """, INVALID_EMAIL, INVALID_PASSWORD);

    public static final String SIGN_UP_USER_JSON = String.format("""
        {
            "name": "%s",
            "email": "%s",
            "password": "%s"
        }
        """, NAME_2, EMAIL_2, PASSWORD);
}
