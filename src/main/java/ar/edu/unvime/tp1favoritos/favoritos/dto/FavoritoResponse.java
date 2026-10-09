package ar.edu.unvime.tp1favoritos.favoritos.dto;

import java.time.LocalDateTime;

public record FavoritoResponse(
        Long id,
        Long productoId,
        String nota,
        LocalDateTime fechaAgregado,
        Long listaId
) {}
