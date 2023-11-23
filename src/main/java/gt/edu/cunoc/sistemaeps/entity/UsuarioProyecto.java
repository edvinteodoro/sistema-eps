package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.util.DateUtils;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "usuario_proyecto")
@Data
public class UsuarioProyecto{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_usuario_proyecto")
    private Integer idUsuarioProyecto;
    @Basic(optional = false)
    @Column(name = "fecha_asignacion")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaAsignacion;
    @Column(name = "fecha_finalizacion")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaFinalizacion;
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioProyectoAnteriorFk")
    private List<CambioUsuario> cambioUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioProyectoNuevoFk")
    private List<CambioUsuario> cambioUsuarioList1;
    @JoinColumn(name = "id_proyecto_fk", referencedColumnName = "id_proyecto")
    @ManyToOne(optional = false)
    private Proyecto idProyectoFk;
    @JoinColumn(name = "id_rol_fk", referencedColumnName = "id_rol")
    @ManyToOne(optional = false)
    private Rol idRolFk;
    @JoinColumn(name = "id_usuario_fk", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuarioFk;
    
    public UsuarioProyecto(){
        this.fechaAsignacion=DateUtils.getCurrentDate();
        this.activo = Boolean.TRUE;
    }
}
