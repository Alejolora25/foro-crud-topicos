package com.alura.foro.presentation.rest;

import com.alura.foro.application.topico.*;
import com.alura.foro.domain.topico.EstadoTopico;
import com.alura.foro.domain.topico.Topico;
import com.alura.foro.infrastructure.exception.GlobalExceptionHandler;
import com.alura.foro.infrastructure.exception.RecursoDuplicadoException;
import com.alura.foro.infrastructure.exception.RecursoNoEncontradoException;
import com.alura.foro.presentation.rest.dto.topico.ActualizarTopicoRequest;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TopicoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class TopicoControllerUpdateTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    // El controller requiere todos estos beans:
    @MockitoBean RegistrarTopicoUseCase registrar;
    @MockitoBean ListarTopicosUseCase listar;
    @MockitoBean ObtenerTopicoPorIdUseCase obtenerPorId;
    @MockitoBean ActualizarTopicoUseCase actualizar;
    @MockitoBean EliminarTopicoUseCase eliminar;

    @Test
    void actualizarTopico_devuelve200() throws Exception {
        var actualizado = Topico.builder()
                .id(7L).titulo("Nuevo titulo").mensaje("Nuevo mensaje")
                .fechaCreacion(LocalDateTime.now())
                .estado(EstadoTopico.ABIERTO)
                .autor("jesus.lora").curso("Spring Boot 3").build();

        Mockito.when(actualizar.ejecutar(any(ActualizarTopicoCommand.class))).thenReturn(actualizado);

        var body = new ActualizarTopicoRequest("Nuevo titulo","Nuevo mensaje","jesus.lora","Spring Boot 3");

        mvc.perform(put("/topicos/{id}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.titulo").value("Nuevo titulo"))
                .andExpect(jsonPath("$.mensaje").value("Nuevo mensaje"));
    }

    @Test
    void actualizarTopico_noExiste_devuelve404() throws Exception {
        Mockito.when(actualizar.ejecutar(any(ActualizarTopicoCommand.class)))
                .thenThrow(new RecursoNoEncontradoException("Tópico no encontrado"));

        var body = new ActualizarTopicoRequest("t","m","a","c");

        mvc.perform(put("/topicos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void actualizarTopico_duplicado_devuelve409() throws Exception {
        Mockito.when(actualizar.ejecutar(any(ActualizarTopicoCommand.class)))
                .thenThrow(new RecursoDuplicadoException("Ya existe un tópico con ese título y mensaje"));

        var body = new ActualizarTopicoRequest("t","m","a","c");

        mvc.perform(put("/topicos/{id}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }
}