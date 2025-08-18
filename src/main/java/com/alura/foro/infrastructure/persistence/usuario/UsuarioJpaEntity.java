package com.alura.foro.infrastructure.persistence.usuario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String nombre;

    @Column(nullable=false, length=120, unique=true)
    private String email;

    @Column(nullable=false, length=255)
    private String clave; // contraseña encriptada (BCrypt)

    @Column(nullable=false, length=30)
    private String rol;   // p.ej. USER o ADMIN

    @Column(nullable=false)
    private boolean activo = true;
}