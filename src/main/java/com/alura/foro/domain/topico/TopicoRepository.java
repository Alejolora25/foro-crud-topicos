package com.alura.foro.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TopicoRepository {
    Topico guardar(Topico topico);
    Optional<Topico> buscarPorId(Long id);
    List<Topico> listar(int pagina, int tamanio);
    void eliminarPorId(Long id);
    boolean existePorTituloYMensaje(String titulo, String mensaje);
    boolean existePorTituloYMensajeExcluyendoId(String titulo, String mensaje, Long idExcluir);
    Page<Topico> listar(String curso, Integer anio, Pageable pageable);
}
