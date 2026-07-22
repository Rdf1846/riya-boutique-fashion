package com.riyaboutique.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /*
     * Generates JWT Token
     */
    public String generateToken(UserDetails userDetails){

        return Jwts.builder()

                // User Identifier
                .subject(userDetails.getUsername())

                // Token Generation Time
                .issuedAt(new Date())

                // Token Expiry Time
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )

                // Digital Signature
                .signWith(getSignInKey())

                // Convert Builder into JWT String
                .compact();
    }

    /*
     * Converts Secret String into SecretKey object.
     */
    private SecretKey getSignInKey(){

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

}