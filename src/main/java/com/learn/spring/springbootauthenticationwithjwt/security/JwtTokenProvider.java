package com.learn.spring.springbootauthenticationwithjwt.security;

import com.learn.spring.springbootauthenticationwithjwt.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class JwtTokenProvider {

    @Value("S3cret")
    private String secret;

    @Value("3600000")
    private Long expiration;

    public String generateToken(Authentication authentication) {
        final User users = (User) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + expiration);
        String userId = Long.toString(users.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", users.getUsername());
        claims.put("fullName", users.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid Jwt Signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid Jwt Token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired Jwt Token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported Jwt token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Jwt claim string is empty: {}", ex.getMessage());
        }
        return false;
    }

    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        Object object;
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }
}
