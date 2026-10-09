package ar.edu.unvime.tp1favoritos.favoritos.repository;

import java.util.List;
import java.util.Optional;

import ar.edu.unvime.tp1favoritos.favoritos.domain.Favorito;

public interface FavoritoRepository {
    List<Favorito> findAll();
    Optional<Favorito> findById(Long id);
    Favorito save(Favorito favorito);
    void deleteById(Long id);
    List<Favorito> findByListaId(Long listaId);
}
