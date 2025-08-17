package com.alura.foro.infrastructure.persistence.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TopicoSpringDataRepository extends JpaRepository<TopicoJpaEntity, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    Page<TopicoJpaEntity> findByCursoIgnoreCaseContaining(String curso, Pageable pageable);
    Page<TopicoJpaEntity> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<TopicoJpaEntity> findByCursoIgnoreCaseContainingAndFechaCreacionBetween(
            String curso, LocalDateTime inicio, LocalDateTime fin, Pageable pageable
    );

    boolean existsByTituloAndMensajeAndIdNot(String titulo, String mensaje, Long id);
}
