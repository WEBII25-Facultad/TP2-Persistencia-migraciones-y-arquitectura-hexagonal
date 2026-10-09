package ar.edu.unvime.tp1favoritos.favoritos.repository;

import ar.edu.unvime.tp1favoritos.favoritos.entity.FavoritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritoJpaRepository extends JpaRepository<FavoritoEntity, Long> {
    List<FavoritoEntity> findByListaId(Long listaId);
}
