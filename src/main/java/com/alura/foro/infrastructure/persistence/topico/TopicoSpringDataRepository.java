package com.alura.foro.infrastructure.persistence.topico;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoSpringDataRepository extends JpaRepository<TopicoJpaEntity, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);
}
