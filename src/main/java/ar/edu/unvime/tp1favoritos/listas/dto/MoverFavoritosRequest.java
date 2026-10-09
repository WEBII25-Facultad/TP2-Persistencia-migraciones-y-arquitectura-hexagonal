package ar.edu.unvime.tp1favoritos.listas.dto;

import jakarta.validation.constraints.NotNull;

public record MoverFavoritosRequest(
        @NotNull(message = "listaDestinoId es obligatorio")
        Long listaDestinoId
) {}
