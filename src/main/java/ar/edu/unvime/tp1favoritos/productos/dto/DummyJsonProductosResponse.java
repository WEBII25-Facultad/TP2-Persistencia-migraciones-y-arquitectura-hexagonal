package ar.edu.unvime.tp1favoritos.productos.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DummyJsonProductosResponse(
        List<DummyJsonProducto> products,
        int total,
        int skip,
        int limit
) {}
