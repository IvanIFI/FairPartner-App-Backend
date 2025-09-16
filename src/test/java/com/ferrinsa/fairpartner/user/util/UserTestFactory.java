package com.ferrinsa.fairpartner.user.util;

import com.ferrinsa.fairpartner.security.util.RolesTestConstants;
import com.ferrinsa.fairpartner.user.model.UserEntity;

import java.util.List;

public class UserTestFactory {

    public static UserEntity buildTestUser1() {
        UserEntity userTest = new UserEntity(UserTestConstants.NAME_1, UserTestConstants.EMAIL_1, UserTestConstants.HASHED_PASSWORD_1);
        userTest.setId(UserTestConstants.ID_1);
        userTest.setRegistrationDate(UserTestConstants.DEFAULT_DATE);
        userTest.setRoles(RolesTestConstants.DEFAULT_ROLES_SET);
        return userTest;
    }

    public static UserEntity buildTestUser2() {
        UserEntity userTest = new UserEntity(UserTestConstants.NAME_2, UserTestConstants.EMAIL_2, UserTestConstants.HASHED_PASSWORD_1);
        userTest.setId(UserTestConstants.ID_2);
        userTest.setRegistrationDate(UserTestConstants.DEFAULT_DATE);
        userTest.setRoles(RolesTestConstants.DEFAULT_ROLES_SET);
        return userTest;
    }

    public static List<UserEntity> buildListTestUsers() {
        UserEntity userTest1 = new UserEntity(UserTestConstants.NAME_1, UserTestConstants.EMAIL_1, UserTestConstants.HASHED_PASSWORD_1);
        userTest1.setId(UserTestConstants.ID_1);
        userTest1.setRegistrationDate(UserTestConstants.DEFAULT_DATE);
        userTest1.setRoles(RolesTestConstants.DEFAULT_ROLES_SET);

        UserEntity userTest2 = new UserEntity(UserTestConstants.NAME_2, UserTestConstants.EMAIL_2, UserTestConstants.HASHED_PASSWORD_1);
        userTest2.setId(UserTestConstants.ID_2);
        userTest2.setRegistrationDate(UserTestConstants.DEFAULT_DATE);
        userTest2.setRoles(RolesTestConstants.DEFAULT_ROLES_SET);

        return List.of(userTest1, userTest2);
    }
}
