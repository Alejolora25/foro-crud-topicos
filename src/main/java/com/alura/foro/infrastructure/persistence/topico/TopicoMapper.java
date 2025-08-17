package com.alura.foro.infrastructure.persistence.topico;

import com.alura.foro.domain.topico.Topico;

public final class TopicoMapper {
    private TopicoMapper(){}

    public static TopicoJpaEntity toEntity(Topico d) {
        if (d == null) return null;
        return TopicoJpaEntity.builder()
                .id(d.getId())
                .titulo(d.getTitulo())
                .mensaje(d.getMensaje())
                .fechaCreacion(d.getFechaCreacion())
                .estado(d.getEstado())
                .autor(d.getAutor())
                .curso(d.getCurso())
                .build();
    }

    public static Topico toDomain(TopicoJpaEntity e) {
        if (e == null) return null;
        return Topico.builder()
                .id(e.getId())
                .titulo(e.getTitulo())
                .mensaje(e.getMensaje())
                .fechaCreacion(e.getFechaCreacion())
                .estado(e.getEstado())
                .autor(e.getAutor())
                .curso(e.getCurso())
                .build();
    }
}
