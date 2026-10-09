package ar.edu.unvime.tp1favoritos.productos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import ar.edu.unvime.tp1favoritos.error.RecursoNoEncontradoException;
import ar.edu.unvime.tp1favoritos.error.ServicioExternoException;
import ar.edu.unvime.tp1favoritos.productos.dto.DummyJsonProducto;
import ar.edu.unvime.tp1favoritos.productos.dto.DummyJsonProductosResponse;
import ar.edu.unvime.tp1favoritos.productos.dto.ProductoDTO;

@Service
public class ProductoService {

    private final RestClient restClient;

    public ProductoService(RestClient dummyJsonRestClient) {
        this.restClient = dummyJsonRestClient;
    }

    public List<ProductoDTO> obtenerTodos() {
        try {
            DummyJsonProductosResponse respuesta = restClient.get()
                    .uri("/products")
                    .retrieve()
                    .body(DummyJsonProductosResponse.class);

            return respuesta.products().stream()
                    .map(this::aProductoDTO)
                    .toList();

        } catch (ResourceAccessException e) {
            throw new ServicioExternoException(
                    "No se pudo contactar a DummyJSON (timeout o caída del servicio)");
        }
    }

    public ProductoDTO obtenerPorId(Long id) {
        try {
            DummyJsonProducto producto = restClient.get()
                    .uri("/products/{id}", id)
                    .retrieve()
                    .body(DummyJsonProducto.class);

            return aProductoDTO(producto);

        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNoEncontradoException(
                    "No existe el producto con id " + id);
        } catch (ResourceAccessException e) {
            throw new ServicioExternoException(
                    "No se pudo contactar a DummyJSON (timeout o caída del servicio)");
        }
    }

    private ProductoDTO aProductoDTO(DummyJsonProducto e) {
        return new ProductoDTO(
                e.id(), e.title(), e.description(), e.category(), e.brand(),
                e.price(), e.discountPercentage(), e.stock(),
                e.rating(), e.thumbnail());
    }
}
