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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "cambio_usuario")
@Data
public class CambioUsuario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cambio_usuario")
    private Integer idCambioUsuario;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "motivo")
    private String motivo;
    @JoinColumn(name = "id_rol_fk", referencedColumnName = "id_rol")
    @ManyToOne(optional = false)
    private Rol idRolFk;
    @JoinColumn(name = "id_usuario_proyecto_anterior_fk", referencedColumnName = "id_usuario_proyecto")
    @ManyToOne(optional = false)
    private UsuarioProyecto idUsuarioProyectoAnteriorFk;
    @JoinColumn(name = "id_usuario_proyecto_nuevo_fk", referencedColumnName = "id_usuario_proyecto")
    @ManyToOne(optional = false)
    private UsuarioProyecto idUsuarioProyectoNuevoFk;
}
