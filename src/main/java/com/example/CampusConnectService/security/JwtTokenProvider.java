package com.example.CampusConnectService.security;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.stream.Collectors;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class JwtTokenProvider {

    // Configure via application.properties, but keep here as example
    // Use raw secret bytes to be compatible with the jjwt 0.9.1 dependency in the project
    private final byte[] secretBytes;
    private final long validityInMilliseconds;

    public JwtTokenProvider(org.springframework.core.env.Environment env) {
        String secret = env.getProperty("app.jwt.secret", "replace-me-with-very-secure-secret");
        // Note: in prod use at least 256-bit key and secure storage
        this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.validityInMilliseconds = Long.parseLong(env.getProperty("app.jwt.expiration-ms", "3600000")); // 1h
    }

    public String createToken(Authentication auth) {
        // principal not required here; keep code focused on username and roles
        String username = auth.getName();
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", authorities)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretBytes)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretBytes)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretBytes).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("Invalid JWT: {}", ex.getMessage());
            return false;
        }
    }
}
