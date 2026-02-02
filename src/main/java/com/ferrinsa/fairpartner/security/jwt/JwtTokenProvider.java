package com.ferrinsa.fairpartner.security.jwt;

import com.ferrinsa.fairpartner.security.role.model.RoleEntity;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final String jwtSecret;
    private final Long jwtDurationSeconds;

    public JwtTokenProvider(
            @Value("${app.security.jwt.secret}") String jwtSecret,
            @Value("${app.security.jwt.expiration}") Long jwtDurationSeconds) {
        this.jwtSecret = jwtSecret;
        this.jwtDurationSeconds = jwtDurationSeconds;
    }

    public String generateToken(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .subject(Long.toString(user.getId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (jwtDurationSeconds * 1000)))
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles().stream().map(RoleEntity::getRoleName).toList())
                .signWith(key)
                .compact();
    }

    public Long getIdUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.parseLong(claims.getSubject());
    }

    public boolean isValidToken(String token) {
        if (!StringUtils.hasLength(token)) {
            return false;
        }

        try {
            JwtParser tokenValidator = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build();

            tokenValidator.parseSignedClaims(token);
            return true;

        } catch (SignatureException ex) {
            log.warn("Error en la firma del token JWT: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.warn("Token malformado: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.info("El token ha expirado: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn("Token JWT no soportado: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims vacío");
        } catch (JwtException ex) {
            log.warn("Error genérico de JWT: {}", ex.getMessage());
        }

        return false;
    }


}

