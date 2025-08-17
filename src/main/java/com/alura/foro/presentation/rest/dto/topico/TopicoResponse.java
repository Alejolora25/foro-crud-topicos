package com.alura.foro.presentation.rest.dto.topico;

import com.alura.foro.domain.topico.EstadoTopico;

import java.time.LocalDateTime;

public record TopicoResponse(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        EstadoTopico estado,
        String autor,
        String curso
) {}