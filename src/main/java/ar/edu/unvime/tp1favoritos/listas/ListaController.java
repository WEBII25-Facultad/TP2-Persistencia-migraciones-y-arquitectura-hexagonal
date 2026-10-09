package ar.edu.unvime.tp1favoritos.listas;

import ar.edu.unvime.tp1favoritos.favoritos.dto.FavoritoResponse;
import ar.edu.unvime.tp1favoritos.listas.dto.ListaRequest;
import ar.edu.unvime.tp1favoritos.listas.dto.ListaResponse;
import ar.edu.unvime.tp1favoritos.listas.dto.MoverFavoritosRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "listas", description = "Gestión de listas para organizar favoritos")
public class ListaController {

    private final ListaService service;

    public ListaController(ListaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva lista", description = "Crea una lista con el nombre indicado.", responses = {
            @ApiResponse(responseCode = "201", description = "Lista creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
    })
    public ResponseEntity<ListaResponse> crear(@Valid @RequestBody ListaRequest request) {
        ListaResponse response = service.crear(request);
        return ResponseEntity.created(URI.create("/api/listas/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas las listas", description = "Devuelve todas las listas registradas.", responses = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    public ResponseEntity<List<ListaResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una lista por su ID", description = "Busca una lista específica por su identificador.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista encontrada"),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada")
    })
    public ResponseEntity<ListaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/{id}/favoritos")
    @Operation(summary = "Obtener los favoritos de una lista", description = "Devuelve todos los favoritos asociados a la lista indicada.", responses = {
            @ApiResponse(responseCode = "200", description = "Favoritos encontrados"),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada")
    })
    public ResponseEntity<List<FavoritoResponse>> favoritosDeLista(@PathVariable Long id) {
        return ResponseEntity.ok(service.favoritosDeLista(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una lista por su ID", description = "Elimina la lista indicada solo si está vacía.", responses = {
            @ApiResponse(responseCode = "204", description = "Lista eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Lista no encontrada"),
            @ApiResponse(responseCode = "409", description = "La lista todavía tiene favoritos")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{origenId}/mover-favoritos")
    @Operation(summary = "Mover favoritos de una lista a otra y eliminar la original", description = "Mueve todos los favoritos y elimina la lista de origen.", responses = {
            @ApiResponse(responseCode = "204", description = "Favoritos movidos correctamente"),
            @ApiResponse(responseCode = "400", description = "La lista origen y destino son la misma"),
            @ApiResponse(responseCode = "404", description = "Lista origen o destino no encontrada")
    })
    public ResponseEntity<Void> moverFavoritos(@PathVariable Long origenId, @Valid @RequestBody MoverFavoritosRequest request) {
        service.moverFavoritos(origenId, request);
        return ResponseEntity.noContent().build();
    }
}
