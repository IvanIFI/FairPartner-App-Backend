package com.ferrinsa.fairpartner.security.config;

import com.ferrinsa.fairpartner.security.role.model.RoleEntity;
import com.ferrinsa.fairpartner.security.role.values.UserRoles;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

@TestConfiguration
public class TestUserDetailsServiceConfig {

    @Bean
    public UserDetailsService customUserDetailsServiceTest() {
        return username -> {
            UserEntity user = new UserEntity("Ivan", username, "$2a$10$abcDEFghiJKLmnopQRstu");
            user.setId(1L);
            user.setRoles(Set.of(new RoleEntity(UserRoles.USER)));
            return user;
        };
    }
}
