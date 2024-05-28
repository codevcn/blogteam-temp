package com.example.demo.services;

import com.example.demo.configs.props.JWTProps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    @Autowired
    private JWTProps jwtProps;

    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(
            claims, userId
        );
    }

    private String createToken(Map<String, Object> claims, String userId) {
        return Jwts.builder().claims(
            claims
        ).subject(
            userId
        ).issuedAt(
            new Date(
                System.currentTimeMillis()
            )
        ).expiration(
            new Date(
                System.currentTimeMillis() + jwtProps.getEXPIRATION_TIME_IN_MS()
            )
        ).signWith(
            getSignKey()
        ).compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(
            jwtProps.getSECRET_KEY()
        );
        return Keys.hmacShaKeyFor(
            keyBytes
        );
    }

    public String extractUserId(String token) {
        return extractClaim(
            token, Claims::getSubject
        );
    }

    public Date extractExpiration(String token) {
        return extractClaim(
            token, Claims::getExpiration
        );
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(
            token
        );
        return claimsResolver.apply(
            claims
        );
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(
            getSignKey()
        ).build().parseSignedClaims(
            token
        ).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(
            token
        ).before(
            new Date()
        );
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userId = extractUserId(
            token
        );
        return (userId.equals(
            userDetails.getUsername()
        ) && !isTokenExpired(
            token
        ));
    }
}
