package com.alura.foro.presentation.rest;

import com.alura.foro.application.usuario.RegistrarUsuarioCommand;
import com.alura.foro.application.usuario.RegistrarUsuarioUseCase;
import com.alura.foro.infrastructure.exception.GlobalExceptionHandler;
import com.alura.foro.infrastructure.exception.RecursoDuplicadoException;
import com.alura.foro.infrastructure.persistence.usuario.UsuarioJpaEntity;
import com.alura.foro.presentation.rest.dto.usuario.CrearUsuarioRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuariosController.class)
@AutoConfigureMockMvc(addFilters = false) // sin filtros de seguridad
@Import(GlobalExceptionHandler.class)     // para mapear 409 a JSON
class UsuariosControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockitoBean RegistrarUsuarioUseCase registrar;

    @Test
    void crearUsuario_devuelve201() throws Exception {
        var entity = UsuarioJpaEntity.builder()
                .id(1L).nombre("Juan Perez").email("juan@forohub.com")
                .clave("$2a$10$hash") // da igual en controller
                .rol("USER").activo(true).build();

        Mockito.when(registrar.ejecutar(any(RegistrarUsuarioCommand.class))).thenReturn(entity);

        var body = new CrearUsuarioRequest("Juan Perez","juan@forohub.com","secreto123");

        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("juan@forohub.com"))
                .andExpect(jsonPath("$.rol").value("USER"))
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    void crearUsuario_duplicado_devuelve409() throws Exception {
        Mockito.when(registrar.ejecutar(any(RegistrarUsuarioCommand.class)))
                .thenThrow(new RecursoDuplicadoException("Ya existe un usuario con ese email"));

        var body = new CrearUsuarioRequest("Juan","juan@forohub.com","secreto123");

        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }
}