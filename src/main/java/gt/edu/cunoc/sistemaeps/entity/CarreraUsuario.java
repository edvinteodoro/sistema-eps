package gt.edu.cunoc.sistemaeps.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "carrera_usuario")
@Data
public class CarreraUsuario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_carrera_usuario")
    private Integer idCarreraUsuario;
    @Column(name = "cantidad_proyectos")
    private Integer cantidadProyectos;
    @JoinColumn(name = "id_carrera_fk", referencedColumnName = "id_carrera")
    @ManyToOne(optional = false)
    private Carrera idCarreraFk;
    @JoinColumn(name = "id_usuario_fk", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioFk;
}
