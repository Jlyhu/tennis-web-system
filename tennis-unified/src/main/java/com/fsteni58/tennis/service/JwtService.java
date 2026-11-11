
package com.fsteni58.tennis.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey key;

    public JwtService() {
        String secret = System.getenv("JWT_SECRET_BASE64");

        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                "Falta configurar JWT_SECRET_BASE64"
            );
        }

        this.key = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(secret)
        );
    }

    public String generarToken(UUID usuarioId, String rol) {

        Date ahora = new Date();

        return Jwts.builder()
            .subject(usuarioId.toString())
            .claim("rol", rol)
            .issuedAt(ahora)
            .expiration(
                new Date(ahora.getTime() + 86400000)
            )
            .signWith(key)
            .compact();
    }

    public UUID obtenerUsuarioId(String token) {

        Claims claims = validar(token);

        return UUID.fromString(claims.getSubject());
    }

    public String obtenerRol(String token) {

        return validar(token).get("rol", String.class);
    }

    private Claims validar(String token) {

        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
