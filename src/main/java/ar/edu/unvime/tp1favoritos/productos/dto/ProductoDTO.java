package ar.edu.unvime.tp1favoritos.productos.dto;

public record ProductoDTO(
        Long id,
        String nombre,
        String descripcion,
        String categoria,
        String marca,
        double precio,
        double descuentoPorcentaje,
        int stock,
        double calificacion,
        String imagenUrl
) {}
