package com.alura.foro.presentation.rest;

import com.alura.foro.application.topico.*;
import com.alura.foro.domain.topico.Topico;
import com.alura.foro.presentation.rest.dto.topico.ActualizarTopicoRequest;
import com.alura.foro.presentation.rest.dto.topico.CrearTopicoRequest;
import com.alura.foro.presentation.rest.dto.topico.TopicoResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final RegistrarTopicoUseCase registrar;
    private final ListarTopicosUseCase listar;
    private final ObtenerTopicoPorIdUseCase obtenerPorId;
    private final ActualizarTopicoUseCase actualizar;
    private final EliminarTopicoUseCase eliminar;


    public TopicoController(
            RegistrarTopicoUseCase registrar,
            ListarTopicosUseCase listar,
            ObtenerTopicoPorIdUseCase obtenerPorId,
            ActualizarTopicoUseCase actualizar,
            EliminarTopicoUseCase eliminar
    ) {
        this.registrar = registrar;
        this.listar = listar;
        this.obtenerPorId = obtenerPorId;
        this.actualizar = actualizar;
        this.eliminar = eliminar;
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

    @GetMapping
    public Page<TopicoResponse> listar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer anio,
            @PageableDefault(size = 10, sort = "fechaCreacion", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Page<Topico> page = listar.ejecutar(curso, anio, pageable);
        return page.map(t -> new TopicoResponse(
                t.getId(), t.getTitulo(), t.getMensaje(), t.getFechaCreacion(),
                t.getEstado(), t.getAutor(), t.getCurso()
        ));
    }

    @GetMapping("/{id}")
    public TopicoResponse obtener(@PathVariable Long id) {
        var t = obtenerPorId.ejecutar(id);
        return new TopicoResponse(
                t.getId(), t.getTitulo(), t.getMensaje(), t.getFechaCreacion(),
                t.getEstado(), t.getAutor(), t.getCurso()
        );
    }

    @PutMapping("/{id}")
    public TopicoResponse actualizar(
            @PathVariable Long id,
            @RequestBody @Valid ActualizarTopicoRequest req
    ) {
        var t = actualizar.ejecutar(
                new ActualizarTopicoCommand(id, req.titulo(), req.mensaje(), req.autor(), req.curso())
        );
        return new TopicoResponse(
                t.getId(), t.getTitulo(), t.getMensaje(), t.getFechaCreacion(),
                t.getEstado(), t.getAutor(), t.getCurso()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        eliminar.ejecutar(id);
    }
}
