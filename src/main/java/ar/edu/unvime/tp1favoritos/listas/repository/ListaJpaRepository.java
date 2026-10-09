package ar.edu.unvime.tp1favoritos.listas.repository;

import ar.edu.unvime.tp1favoritos.listas.entity.ListaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaJpaRepository extends JpaRepository<ListaEntity, Long> {
}
