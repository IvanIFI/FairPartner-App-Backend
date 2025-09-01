package com.ferrinsa.fairpartner.security.util;

import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.util.UserTestConstants;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserTestSecurityFactory {

    public static UserEntity buildTestAdminToSecurityTest(PasswordEncoder encoder) {
        return new UserEntity(UserTestConstants.NAME_1, UserTestConstants.EMAIL_1, encoder.encode(UserTestConstants.PASSWORD));
    }

    public static UserEntity buildTestUserToSecurityTest(PasswordEncoder encoder) {
        return new UserEntity(UserTestConstants.NAME_2, UserTestConstants.EMAIL_2, encoder.encode(UserTestConstants.PASSWORD));
    }
}
