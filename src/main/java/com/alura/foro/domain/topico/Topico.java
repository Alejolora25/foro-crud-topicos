package com.alura.foro.domain.topico;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Topico {
    private Long id;
    private String titulo;
    private String mensaje;

    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Builder.Default
    private EstadoTopico estado = EstadoTopico.ABIERTO;

    private String autor;
    private String curso;
}
