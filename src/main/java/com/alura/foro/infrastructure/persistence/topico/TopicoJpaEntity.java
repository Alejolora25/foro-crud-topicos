package com.alura.foro.infrastructure.persistence.topico;

import com.alura.foro.domain.topico.EstadoTopico;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "topicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=160)
    private String titulo;

    @Column(nullable=false, columnDefinition="TEXT")
    private String mensaje;

    @Column(name="fecha_creacion", nullable=false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=30)
    private EstadoTopico estado;

    @Column(nullable=false, length=120)
    private String autor;

    @Column(nullable=false, length=120)
    private String curso;
}