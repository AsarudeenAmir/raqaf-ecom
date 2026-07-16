package com.raqaf.ecom.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * CONCEPT: JWT (JSON Web Token) generation and validation.
 * The token itself is stateless - the server doesn't store sessions.
 * Everything needed to verify a request (username + expiry) is encoded
 * in the token and signed, so the server just re-verifies the signature.
 *
 * NOTE: this uses the jjwt 0.12.x fluent API. If you're following an older
 * tutorial online, you'll see Jwts.parserBuilder() / parseClaimsJws() /
 * getBody() - those are the 0.11.x API and were replaced in 0.12.x with
 * Jwts.parser() / parseSignedClaims() / getPayload().
 *
 * Interview question: "How is JWT different from session-based auth?"
 * Answer: No server-side session storage needed -> scales horizontally
 * more easily, but you lose the ability to instantly revoke a token
 * (mitigated with short expiry + refresh tokens).
 */
@Component
public class JwtUtil {

    // In production, load this from an environment variable, NEVER hardcode
    @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B59}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24 hours in ms
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
