package com.alura.foro.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.issuer:forohub}")
    private String issuer;

    @Value("${api.security.token.expiration:PT2H}")
    private java.time.Duration expiration;

    public String generarToken(String subject, String rol) {
        try {
            Algorithm alg = Algorithm.HMAC256(secret);
            Instant ahora = Instant.now();
            Instant exp = ahora.plus(expiration);

            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(subject)          // normalmente el email/username
                    .withClaim("rol", rol)         // claim opcional
                    .withIssuedAt(Date.from(ahora))
                    .withExpiresAt(Date.from(exp))
                    .sign(alg);
        } catch (JWTCreationException e) {
            throw new IllegalStateException("No se pudo generar el JWT", e);
        }
    }

    /** Devuelve el 'sub' si el token es válido; lanza excepción si no lo es */
    public String validarYObtenerSubject(String token) {
        try {
            Algorithm alg = Algorithm.HMAC256(secret);
            DecodedJWT decoded = JWT.require(alg)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
            return decoded.getSubject();
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Token inválido o expirado", e);
        }
    }
}