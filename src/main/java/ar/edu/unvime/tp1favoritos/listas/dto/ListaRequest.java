package ar.edu.unvime.tp1favoritos.listas.dto;

import jakarta.validation.constraints.NotBlank;

public record ListaRequest(
        @NotBlank(message = "nombre no puede estar vacío")
        String nombre
) {}
