package ar.edu.unvime.tp1favoritos.listas.repository;

import ar.edu.unvime.tp1favoritos.listas.domain.Lista;
import ar.edu.unvime.tp1favoritos.listas.entity.ListaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ListaRepositoryAdapter implements ListaRepository {

    private final ListaJpaRepository jpaRepository;

    public ListaRepositoryAdapter(ListaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Lista> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::aDominio)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Lista> findById(Long id) {
        return jpaRepository.findById(id).map(this::aDominio);
    }

    @Override
    public Lista save(Lista lista) {
        ListaEntity entity = new ListaEntity();
        entity.setId(lista.id());
        entity.setNombre(lista.nombre());

        ListaEntity savedEntity = jpaRepository.save(entity);
        return aDominio(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private Lista aDominio(ListaEntity entity) {
        return new Lista(entity.getId(), entity.getNombre());
    }
}
