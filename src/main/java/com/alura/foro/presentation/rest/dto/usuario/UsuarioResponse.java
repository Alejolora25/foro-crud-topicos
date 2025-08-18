package com.alura.foro.presentation.rest.dto.usuario;

public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        String rol,
        boolean activo
) {}