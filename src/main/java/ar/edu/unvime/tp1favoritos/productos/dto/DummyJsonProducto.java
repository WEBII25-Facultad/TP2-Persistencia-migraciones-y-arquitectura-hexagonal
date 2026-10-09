package ar.edu.unvime.tp1favoritos.productos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DummyJsonProducto(
        Long id,
        String title,
        String description,
        String category,
        String brand,
        double price,
        double discountPercentage,
        int stock,
        double rating,
        String thumbnail
) {}
