package ar.edu.unvime.tp1favoritos.favoritos.repository;

import ar.edu.unvime.tp1favoritos.favoritos.domain.Favorito;
import ar.edu.unvime.tp1favoritos.favoritos.entity.FavoritoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.edu.unvime.tp1favoritos.listas.repository.ListaJpaRepository;

@Repository
public class FavoritoRepositoryAdapter implements FavoritoRepository {

    private final FavoritoJpaRepository jpaRepository;
    private final ListaJpaRepository listaJpaRepository;

    public FavoritoRepositoryAdapter(FavoritoJpaRepository jpaRepository, ListaJpaRepository listaJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.listaJpaRepository = listaJpaRepository;
    }

    @Override
    public List<Favorito> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::aDominio)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Favorito> findById(Long id) {
        return jpaRepository.findById(id).map(this::aDominio);
    }

    @Override
    public Favorito save(Favorito favorito) {
        FavoritoEntity entity = new FavoritoEntity();
        entity.setId(favorito.id());
        entity.setProductoId(favorito.productoId());
        entity.setNota(favorito.nota());
        entity.setFechaAlta(favorito.fechaAgregado());
        entity.setLista(listaJpaRepository.getReferenceById(favorito.listaId()));

        FavoritoEntity savedEntity = jpaRepository.save(entity);
        return aDominio(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Favorito> findByListaId(Long listaId) {
        return jpaRepository.findByListaId(listaId).stream()
                .map(this::aDominio)
                .collect(Collectors.toList());
    }

    private Favorito aDominio(FavoritoEntity entity) {
        return new Favorito(
                entity.getId(),
                entity.getProductoId(),
                entity.getNota(),
                entity.getFechaAlta(),
                entity.getLista().getId()
        );
    }
}
