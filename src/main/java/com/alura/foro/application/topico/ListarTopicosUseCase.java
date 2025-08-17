package com.alura.foro.application.topico;

import com.alura.foro.domain.topico.Topico;
import com.alura.foro.domain.topico.TopicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListarTopicosUseCase {
    private final TopicoRepository repositorio;

    public ListarTopicosUseCase(TopicoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Page<Topico> ejecutar(String curso, Integer anio, Pageable pageable) {
        return repositorio.listar(curso, anio, pageable);
    }
}
