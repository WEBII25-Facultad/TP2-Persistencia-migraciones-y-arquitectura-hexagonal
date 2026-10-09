package ar.edu.unvime.tp1favoritos.favoritos.domain;

import java.time.LocalDateTime;

public record Favorito(
        Long id,
        Long productoId,
        String nota,
        LocalDateTime fechaAgregado,
        Long listaId
) {}
