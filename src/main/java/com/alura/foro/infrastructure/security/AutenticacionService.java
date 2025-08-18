package com.alura.foro.infrastructure.security;

import com.alura.foro.infrastructure.persistence.usuario.UsuarioSpringDataRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutenticacionService implements UserDetailsService {

    private final UsuarioSpringDataRepository usuarios;

    public AutenticacionService(UsuarioSpringDataRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = usuarios.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        if (!u.isActivo()) throw new UsernameNotFoundException("Usuario inactivo");

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + u.getRol()));
        return new User(u.getEmail(), u.getClave(), authorities);
    }
}
