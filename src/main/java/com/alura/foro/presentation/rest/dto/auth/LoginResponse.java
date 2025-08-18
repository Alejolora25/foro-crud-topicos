package com.alura.foro.presentation.rest.dto.auth;

public record LoginResponse(String accessToken, String tokenType, long expiresInSeconds) {}

