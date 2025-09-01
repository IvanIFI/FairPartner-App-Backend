package com.ferrinsa.fairpartner.security.util;

import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.util.UserTestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserAuthTestFactory {
    public static UsernamePasswordAuthenticationToken buildUserPassAuthToken (){
        UserEntity user = UserTestFactory.buildTestUser();
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
