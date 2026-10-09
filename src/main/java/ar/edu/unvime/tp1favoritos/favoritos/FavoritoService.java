package ar.edu.unvime.tp1favoritos.favoritos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import ar.edu.unvime.tp1favoritos.error.RecursoNoEncontradoException;
import ar.edu.unvime.tp1favoritos.favoritos.domain.Favorito;
import ar.edu.unvime.tp1favoritos.favoritos.dto.FavoritoRequest;
import ar.edu.unvime.tp1favoritos.favoritos.dto.FavoritoResponse;
import ar.edu.unvime.tp1favoritos.favoritos.repository.FavoritoRepository;

import ar.edu.unvime.tp1favoritos.listas.repository.ListaRepository;

@Service
public class FavoritoService {

    private final FavoritoRepository repository;
    private final ListaRepository listaRepository;

    public FavoritoService(FavoritoRepository repository, ListaRepository listaRepository) {
        this.repository = repository;
        this.listaRepository = listaRepository;
    }

    public FavoritoResponse crear(FavoritoRequest request) {
        validarLista(request.listaId());
        Favorito nuevo = new Favorito(
                null,
                request.productoId(),
                request.nota(),
                LocalDateTime.now(),
                request.listaId());

        return aResponse(repository.save(nuevo));
    }

    public List<FavoritoResponse> listar() {
        return repository.findAll().stream()
                .map(this::aResponse)
                .toList();
    }

    public FavoritoResponse obtenerPorId(Long id) {
        return aResponse(buscarOFallar(id));
    }

    public FavoritoResponse actualizar(Long id, FavoritoRequest request) {
        validarLista(request.listaId());
        Favorito existente = buscarOFallar(id);

        Favorito actualizado = new Favorito(
                existente.id(),
                request.productoId(),
                request.nota(),
                existente.fechaAgregado(), // no se pisa la fecha original
                request.listaId());

        return aResponse(repository.save(actualizado));
    }

    public void eliminar(Long id) {
        buscarOFallar(id); // si no existe, lanza 404 antes de intentar borrar
        repository.deleteById(id);
    }

    private Favorito buscarOFallar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el favorito con id " + id));
    }

    private void validarLista(Long listaId) {
        if (listaRepository.findById(listaId).isEmpty()) {
            throw new RecursoNoEncontradoException("No existe la lista con id " + listaId);
        }
    }

    private FavoritoResponse aResponse(Favorito f) {
        return new FavoritoResponse(f.id(), f.productoId(), f.nota(), f.fechaAgregado(), f.listaId());
    }
}
