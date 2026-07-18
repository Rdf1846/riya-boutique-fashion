package com.riyaboutique.auth.securityConfig;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "mySuperSecretKeyForRiyaBoutiqueFashionAuthentication2026";

    //Converting secret key string to key object
    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    //Generating the jwt token
    public String generateToken(String email)
    {
        return Jwts
                .builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60))
                .signWith(getSigningKey())
                .compact();
    }


}
