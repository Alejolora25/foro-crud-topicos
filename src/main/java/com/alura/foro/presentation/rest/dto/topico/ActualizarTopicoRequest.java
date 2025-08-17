package com.alura.foro.presentation.rest.dto.topico;

import jakarta.validation.constraints.NotBlank;

public record ActualizarTopicoRequest(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotBlank String autor,
        @NotBlank String curso
) {}