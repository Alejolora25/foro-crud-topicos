package com.alura.foro.application.topico;

import com.alura.foro.domain.topico.EstadoTopico;
import com.alura.foro.domain.topico.Topico;
import com.alura.foro.domain.topico.TopicoRepository;
import com.alura.foro.infrastructure.exception.RecursoDuplicadoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarTopicoUseCase {

    private final TopicoRepository repositorio;

    public RegistrarTopicoUseCase(TopicoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional
    public Topico ejecutar(RegistrarTopicoCommand cmd) {
        if (repositorio.existePorTituloYMensaje(cmd.titulo(), cmd.mensaje())) {
            throw new RecursoDuplicadoException("Ya existe un tópico con ese título y mensaje");
        }

        Topico nuevo = Topico.builder()
                .titulo(cmd.titulo())
                .mensaje(cmd.mensaje())
                .estado(EstadoTopico.ABIERTO)
                .autor(cmd.autor())
                .curso(cmd.curso())
                .build();

        return repositorio.guardar(nuevo);
    }
}