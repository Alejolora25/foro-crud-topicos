package com.alura.foro.application.topico;

import com.alura.foro.domain.topico.TopicoRepository;
import com.alura.foro.infrastructure.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EliminarTopicoUseCase {

    private final TopicoRepository repositorio;

    public EliminarTopicoUseCase(TopicoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional
    public void ejecutar(Long id) {
        var existente = repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tópico no encontrado"));
        // Si llegamos aquí, existe
        repositorio.eliminarPorId(existente.getId());
    }
}
