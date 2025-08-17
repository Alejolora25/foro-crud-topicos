package com.alura.foro.presentation.rest;

import com.alura.foro.application.topico.RegistrarTopicoCommand;
import com.alura.foro.application.topico.RegistrarTopicoUseCase;
import com.alura.foro.presentation.rest.dto.topico.CrearTopicoRequest;
import com.alura.foro.presentation.rest.dto.topico.TopicoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final RegistrarTopicoUseCase registrar;

    public TopicoController(RegistrarTopicoUseCase registrar) {
        this.registrar = registrar;
    }

    @PostMapping
    public ResponseEntity<TopicoResponse> crear(@RequestBody @Valid CrearTopicoRequest req) {
        var topico = registrar.ejecutar(
                new RegistrarTopicoCommand(req.titulo(), req.mensaje(), req.autor(), req.curso())
        );

        var body = new TopicoResponse(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getEstado(),
                topico.getAutor(),
                topico.getCurso()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
