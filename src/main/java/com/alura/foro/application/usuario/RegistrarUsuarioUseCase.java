package com.alura.foro.application.usuario;

import com.alura.foro.infrastructure.exception.RecursoDuplicadoException;
import com.alura.foro.infrastructure.persistence.usuario.UsuarioJpaEntity;
import com.alura.foro.infrastructure.persistence.usuario.UsuarioSpringDataRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarUsuarioUseCase {

    private final UsuarioSpringDataRepository repo;
    private final PasswordEncoder encoder;

    public RegistrarUsuarioUseCase(UsuarioSpringDataRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional
    public UsuarioJpaEntity ejecutar(RegistrarUsuarioCommand cmd) {
        repo.findByEmail(cmd.email()).ifPresent(u -> {
            throw new RecursoDuplicadoException("Ya existe un usuario con ese email");
        });

        var entity = UsuarioJpaEntity.builder()
                .nombre(cmd.nombre())
                .email(cmd.email())
                .clave(encoder.encode(cmd.clave()))
                .rol("USER")
                .activo(true)
                .build();

        return repo.save(entity);
    }
}
