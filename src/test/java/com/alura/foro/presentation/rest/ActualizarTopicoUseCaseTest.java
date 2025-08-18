package com.alura.foro.presentation.rest;

import com.alura.foro.application.topico.ActualizarTopicoCommand;
import com.alura.foro.application.topico.ActualizarTopicoUseCase;
import com.alura.foro.domain.topico.EstadoTopico;
import com.alura.foro.domain.topico.Topico;
import com.alura.foro.domain.topico.TopicoRepository;
import com.alura.foro.infrastructure.exception.RecursoDuplicadoException;
import com.alura.foro.infrastructure.exception.RecursoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActualizarTopicoUseCaseTest {

    @Mock TopicoRepository repositorio;
    @InjectMocks
    ActualizarTopicoUseCase useCase;

    @Test
    void lanza404_siNoExiste() {
        when(repositorio.buscarPorId(99L)).thenReturn(Optional.empty());

        var cmd = new ActualizarTopicoCommand(99L, "t", "m", "a", "c");
        assertThrows(RecursoNoEncontradoException.class, () -> useCase.ejecutar(cmd));
    }

    @Test
    void lanza409_siDuplicado() {
        var existente = Topico.builder()
                .id(7L).titulo("old").mensaje("old").estado(EstadoTopico.ABIERTO)
                .autor("a").curso("c").build();

        when(repositorio.buscarPorId(7L)).thenReturn(Optional.of(existente));
        when(repositorio.existePorTituloYMensajeExcluyendoId("t","m",7L)).thenReturn(true);

        var cmd = new ActualizarTopicoCommand(7L, "t", "m", "a", "c");
        assertThrows(RecursoDuplicadoException.class, () -> useCase.ejecutar(cmd));
    }

    @Test
    void actualizaYGuarda_siValido() {
        var existente = Topico.builder()
                .id(7L).titulo("old").mensaje("old").estado(EstadoTopico.ABIERTO)
                .autor("a").curso("c").build();

        when(repositorio.buscarPorId(7L)).thenReturn(Optional.of(existente));
        when(repositorio.existePorTituloYMensajeExcluyendoId("t","m",7L)).thenReturn(false);
        when(repositorio.guardar(any(Topico.class))).thenAnswer(inv -> inv.getArgument(0));

        var cmd = new ActualizarTopicoCommand(7L, "t", "m", "a2", "c2");
        var out = useCase.ejecutar(cmd);

        assertEquals("t", out.getTitulo());
        assertEquals("m", out.getMensaje());
        assertEquals("a2", out.getAutor());
        assertEquals("c2", out.getCurso());
        verify(repositorio).guardar(any(Topico.class));
    }
}
