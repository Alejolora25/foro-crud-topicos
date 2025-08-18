package com.alura.foro.application.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrarUsuarioCommand(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 72) String clave
) {}