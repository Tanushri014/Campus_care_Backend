package campus_care.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key signKey;

    private static final long EXPIRATION =
            1000 * 60 * 60 * 24;

    @PostConstruct
    public void init() {

        signKey =
                Keys.hmacShaKeyFor(
                        secret.getBytes()
                );
    }

    public String generateToken(
            String email,
            String role,
            String category
    ) {

        Map<String, Object> claims =
                new HashMap<>();

        claims.put("role", role);

        if (category != null) {
            claims.put("category", category);
        }

        return Jwts.builder()

                .setClaims(claims)

                .setSubject(email)

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION
                        )
                )

                .signWith(
                        signKey,
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    public String extractEmail(
            String token
    ) {

        return extractAllClaims(token)
                .getSubject();
    }

    public String extractRole(
            String token
    ) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    public String extractCategory(
            String token
    ) {

        return extractAllClaims(token)
                .get("category", String.class);
    }

    private Claims extractAllClaims(
            String token
    ) {

        return Jwts.parserBuilder()

                .setSigningKey(signKey)

                .build()

                .parseClaimsJws(token)

                .getBody();
    }

    public boolean validateToken(
            String token
    ) {

        try {

            extractAllClaims(token);

            return true;

        } catch (
                JwtException |
                IllegalArgumentException e
        ) {

            return false;
        }
    }
}