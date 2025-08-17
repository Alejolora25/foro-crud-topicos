package com.alura.foro.application.topico;

import com.alura.foro.domain.topico.Topico;
import com.alura.foro.domain.topico.TopicoRepository;
import com.alura.foro.infrastructure.exception.RecursoDuplicadoException;
import com.alura.foro.infrastructure.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActualizarTopicoUseCase {

    private final TopicoRepository repositorio;

    public ActualizarTopicoUseCase(TopicoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional
    public Topico ejecutar(ActualizarTopicoCommand cmd) {
        var existente = repositorio.buscarPorId(cmd.id())
                .orElseThrow(() -> new RecursoNoEncontradoException("Tópico no encontrado"));

        // Regla anti-duplicados (título+mensaje) EXCLUYENDO el propio id
        if (repositorio.existePorTituloYMensajeExcluyendoId(cmd.titulo(), cmd.mensaje(), cmd.id())) {
            throw new RecursoDuplicadoException("Ya existe un tópico con ese título y mensaje");
        }

        // Actualizar campos obligatorios
        existente.setTitulo(cmd.titulo());
        existente.setMensaje(cmd.mensaje());
        existente.setAutor(cmd.autor());
        existente.setCurso(cmd.curso());

        return repositorio.guardar(existente);
    }
}

