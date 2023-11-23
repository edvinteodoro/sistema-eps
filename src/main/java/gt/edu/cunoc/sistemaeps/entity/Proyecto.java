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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "proyecto")
@Data
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_proyecto")
    private Integer idProyecto;
    @Column(name = "tipo_proyecto")
    private String tipoProyecto;
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaFin;
    @Column(name = "resultado")
    private String resultado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProyectoFk")
    private List<EtapaProyecto> etapaProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProyectoFk")
    private List<ExtensionProyecto> extensionProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProyectoFk")
    private List<UsuarioProyecto> usuarioProyectoList;
    @JoinColumn(name = "id_carrera_fk", referencedColumnName = "id_carrera")
    @ManyToOne(optional = false)
    private Carrera idCarreraFk;
    @JoinColumn(name = "id_institucion_fk", referencedColumnName = "id_institucion")
    @OneToOne(optional = false)
    private Institucion idInstitucionFk;
    @JoinColumn(name = "id_usuario_fk", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioFk;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProyectoFk")
    private List<Bitacora> bitacoraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProyectoFk")
    private List<Persona> personaList;

    public Proyecto() {
        this.activo = true;
        this.fechaInicio= DateUtils.getCurrentDate();
    }
}
