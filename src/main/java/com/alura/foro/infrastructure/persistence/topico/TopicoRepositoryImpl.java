package com.alura.foro.infrastructure.persistence.topico;

import com.alura.foro.domain.topico.Topico;
import com.alura.foro.domain.topico.TopicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Topico> listar(String curso, Integer anio, Pageable pageable) {
        Page<TopicoJpaEntity> page;

        boolean filtroCurso = (curso != null && !curso.isBlank());
        boolean filtroAnio  = (anio != null);

        if (!filtroCurso && !filtroAnio) {
            page = jpa.findAll(pageable);
        } else if (filtroCurso && !filtroAnio) {
            page = jpa.findByCursoIgnoreCaseContaining(curso, pageable);
        } else if (!filtroCurso) { // solo año
            var inicio = LocalDateTime.of(anio, 1, 1, 0, 0);
            var fin    = inicio.plusYears(1);
            page = jpa.findByFechaCreacionBetween(inicio, fin, pageable);
        } else { // curso + año
            var inicio = LocalDateTime.of(anio, 1, 1, 0, 0);
            var fin    = inicio.plusYears(1);
            page = jpa.findByCursoIgnoreCaseContainingAndFechaCreacionBetween(curso, inicio, fin, pageable);
        }

        return page.map(TopicoMapper::toDomain);
    }

    @Override
    public boolean existePorTituloYMensajeExcluyendoId(String titulo, String mensaje, Long idExcluir) {
        return jpa.existsByTituloAndMensajeAndIdNot(titulo, mensaje, idExcluir);
    }
}