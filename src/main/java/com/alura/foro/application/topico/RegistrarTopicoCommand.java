package com.alura.foro.application.topico;

import jakarta.validation.constraints.NotBlank;

public record RegistrarTopicoCommand(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotBlank String autor,
        @NotBlank String curso
) {}
