package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.impl.DefaultJwtParserBuilder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    @Value("${security.jwt.signing.key}")
    private String jwtSigningKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationTime;

    private JwtParser jwtParser;
    private final JwtBuilder jwtBuilder;

    public JwtService() {
        jwtBuilder = new DefaultJwtBuilder();
    }

    @PostConstruct
    public void init() {
        jwtParser = new DefaultJwtParserBuilder()
                .verifyWith(getSigningKey()) // Ensuring jwtSigningKey is initialized before use
                .build();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("role", customUserDetails.getRole());
        }
        return generateToken(claims, userDetails);
    }

    public void validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        if (!username.equals(userDetails.getUsername()) || isTokenExpired(token)) {
            throw new AuthenticationException("Not correct JWT token.");
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        // not sure if Date is appropriate enough
        return jwtBuilder
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                // JAVA 8 time
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return jwtParser
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
