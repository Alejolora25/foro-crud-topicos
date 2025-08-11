package com.alura.foro.domain.topico;

import java.util.List;
import java.util.Optional;

public interface TopicoRepository {
    Topico guardar(Topico topico);
    Optional<Topico> buscarPorId(Long id);
    List<Topico> listar(int pagina, int tamanio);
    void eliminarPorId(Long id);
}
