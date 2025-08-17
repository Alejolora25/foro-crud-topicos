package com.alura.foro.infrastructure.persistence.topico;

import com.alura.foro.domain.topico.Topico;
import com.alura.foro.domain.topico.TopicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TopicoRepositoryImpl implements TopicoRepository {

    private final TopicoSpringDataRepository jpa;

    @Override
    public Topico guardar(Topico topico) {
        if (topico.getFechaCreacion() == null) {
            topico.setFechaCreacion(LocalDateTime.now());
        }
        var saved = jpa.save(TopicoMapper.toEntity(topico));
        return TopicoMapper.toDomain(saved);
    }

    @Override
    public Optional<Topico> buscarPorId(Long id) {
        return jpa.findById(id).map(TopicoMapper::toDomain);
    }

    @Override
    public List<Topico> listar(int pagina, int tamanio) {
        var pageable = PageRequest.of(pagina, tamanio, Sort.by("fechaCreacion").descending());
        return jpa.findAll(pageable).map(TopicoMapper::toDomain).getContent();
    }

    @Override
    public void eliminarPorId(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existePorTituloYMensaje(String titulo, String mensaje) {
        return jpa.existsByTituloAndMensaje(titulo, mensaje);
    }
}