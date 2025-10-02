package com.groceryshop.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.hmacShaKeyFor("o98u2ui2issiju2uh92ue8928emi92i90ie".getBytes());
    private final long validityMillis = 24 * 60 * 60 * 1000; // 24 hours

    public String generateToken(String phone) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(phone)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validityMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractPhone(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            extractPhone(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
