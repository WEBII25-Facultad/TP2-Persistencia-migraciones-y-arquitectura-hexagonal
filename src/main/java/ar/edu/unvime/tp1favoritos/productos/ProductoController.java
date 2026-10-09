package ar.edu.unvime.tp1favoritos.productos;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unvime.tp1favoritos.productos.dto.ProductoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "productos", description = "Catálogo de productos consumiendo DummyJSON")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public List<ProductoDTO> listar() {
        return service.obtenerTodos();
    }

    @Operation(summary = "Obtener un producto por id")
    @GetMapping("/{id}")
    public ProductoDTO obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }
}
