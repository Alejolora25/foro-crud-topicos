package com.alura.foro.application.topico;

import com.alura.foro.domain.topico.Topico;
import com.alura.foro.domain.topico.TopicoRepository;
import com.alura.foro.infrastructure.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public class ObtenerTopicoPorIdUseCase {
    private final TopicoRepository repositorio;

    public ObtenerTopicoPorIdUseCase(TopicoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Topico ejecutar(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tópico no encontrado"));
    }
}