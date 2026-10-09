package ar.edu.unvime.tp1favoritos.listas;

import ar.edu.unvime.tp1favoritos.error.ListaNoVaciaException;
import ar.edu.unvime.tp1favoritos.error.OperacionInvalidaException;
import ar.edu.unvime.tp1favoritos.error.RecursoNoEncontradoException;
import ar.edu.unvime.tp1favoritos.favoritos.domain.Favorito;
import ar.edu.unvime.tp1favoritos.favoritos.dto.FavoritoResponse;
import ar.edu.unvime.tp1favoritos.favoritos.repository.FavoritoRepository;
import ar.edu.unvime.tp1favoritos.listas.domain.Lista;
import ar.edu.unvime.tp1favoritos.listas.dto.ListaRequest;
import ar.edu.unvime.tp1favoritos.listas.dto.ListaResponse;
import ar.edu.unvime.tp1favoritos.listas.dto.MoverFavoritosRequest;
import ar.edu.unvime.tp1favoritos.listas.repository.ListaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListaService {

    private final ListaRepository listaRepository;
    private final FavoritoRepository favoritoRepository;

    public ListaService(ListaRepository listaRepository, FavoritoRepository favoritoRepository) {
        this.listaRepository = listaRepository;
        this.favoritoRepository = favoritoRepository;
    }

    public ListaResponse crear(ListaRequest request) {
        Lista nueva = new Lista(null, request.nombre());
        return aResponse(listaRepository.save(nueva));
    }

    public List<ListaResponse> listar() {
        return listaRepository.findAll().stream()
                .map(this::aResponse)
                .toList();
    }

    public ListaResponse obtenerPorId(Long id) {
        return aResponse(buscarOFallar(id));
    }

    public List<FavoritoResponse> favoritosDeLista(Long id) {
        buscarOFallar(id);
        return favoritoRepository.findByListaId(id).stream()
                .map(f -> new FavoritoResponse(f.id(), f.productoId(), f.nota(), f.fechaAgregado(), f.listaId()))
                .toList();
    }

    public void eliminar(Long id) {
        buscarOFallar(id);
        if (!favoritoRepository.findByListaId(id).isEmpty()) {
            throw new ListaNoVaciaException("La lista " + id + " todavía tiene favoritos, no se puede eliminar");
        }
        listaRepository.deleteById(id);
    }

    // @Transactional garantiza que las escrituras a la base de datos se apliquen todas juntas o ninguna en caso de error.
    @Transactional
    public void moverFavoritos(Long origenId, MoverFavoritosRequest request) {
        if (origenId.equals(request.listaDestinoId())) {
            throw new OperacionInvalidaException("La lista origen y la lista destino no pueden ser la misma");
        }

        buscarOFallar(origenId);
        Lista destino = buscarOFallar(request.listaDestinoId());

        List<Favorito> favoritos = favoritoRepository.findByListaId(origenId);
        for (Favorito f : favoritos) {
            favoritoRepository.save(new Favorito(f.id(), f.productoId(), f.nota(), f.fechaAgregado(), destino.id()));
        }

        listaRepository.deleteById(origenId);
    }

    private Lista buscarOFallar(Long id) {
        return listaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la lista con id " + id));
    }

    private ListaResponse aResponse(Lista lista) {
        return new ListaResponse(lista.id(), lista.nombre());
    }
}
