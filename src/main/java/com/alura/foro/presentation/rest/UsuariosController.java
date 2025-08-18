package com.alura.foro.presentation.rest;

import com.alura.foro.application.usuario.RegistrarUsuarioCommand;
import com.alura.foro.application.usuario.RegistrarUsuarioUseCase;
import com.alura.foro.presentation.rest.dto.usuario.CrearUsuarioRequest;
import com.alura.foro.presentation.rest.dto.usuario.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    private final RegistrarUsuarioUseCase registrar;

    public UsuariosController(RegistrarUsuarioUseCase registrar) {
        this.registrar = registrar;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@RequestBody @Valid CrearUsuarioRequest req) {
        var u = registrar.ejecutar(new RegistrarUsuarioCommand(req.nombre(), req.email(), req.clave()));
        var body = new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol(), u.isActivo());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
