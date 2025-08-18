package com.alura.foro.presentation.rest;

import com.alura.foro.presentation.rest.dto.auth.LoginRequest;
import com.alura.foro.infrastructure.security.TokenService;

import com.alura.foro.presentation.rest.dto.auth.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticación")
@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager authManager;
    private final TokenService tokens;

    @Value("${api.security.token.expiration:PT2H}")
    private java.time.Duration expiration;

    public AuthController(AuthenticationManager authManager, TokenService tokens) {
        this.authManager = authManager;
        this.tokens = tokens;
    }

    @Operation(summary = "Login (público)", description = "Genera y devuelve un JWT", security = {})
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest req) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(req.email(), req.clave());
        Authentication authResult = authManager.authenticate(authToken);

        var principal = (UserDetails) authResult.getPrincipal();
        // Si guardaste rol en authorities como ROLE_X:
        var rol = principal.getAuthorities().stream().findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("USER");

        String jwt = tokens.generarToken(principal.getUsername(), rol);

        return ResponseEntity.ok(new LoginResponse(jwt, "Bearer", expiration.toSeconds()));
    }
}
