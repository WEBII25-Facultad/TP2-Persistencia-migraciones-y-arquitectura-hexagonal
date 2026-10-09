package ar.edu.unvime.tp1favoritos.favoritos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos")
@Getter
@Setter
public class FavoritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(nullable = false, length = 500)
    private String nota;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAlta;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "lista_id", nullable = false)
    private ar.edu.unvime.tp1favoritos.listas.entity.ListaEntity lista;

    public FavoritoEntity() {
    }
}
