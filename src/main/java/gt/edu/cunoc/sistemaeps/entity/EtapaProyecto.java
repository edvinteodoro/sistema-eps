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
@Table(name = "etapa_proyecto")
@Data
public class EtapaProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_etapa_proyecto")
    private Integer idEtapaProyecto;
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    @Basic(optional = false)
    @Column(name = "editable")
    private boolean editable;
    @Basic(optional = false)
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaFin;
    @JoinColumn(name = "id_etapa_fk", referencedColumnName = "id_etapa")
    @ManyToOne(optional = false)
    private Etapa idEtapaFk;
    @JoinColumn(name = "id_proyecto_fk", referencedColumnName = "id_proyecto")
    @ManyToOne(optional = false)
    private Proyecto idProyectoFk;
    @OneToMany(mappedBy = "etapaProyectoFk")
    private List<Comentario> comentarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEtapaProyectoFk")
    private List<ElementoProyecto> elementoProyectoList;

    public EtapaProyecto(){
        this.activo=Boolean.TRUE;
        this.editable=Boolean.FALSE;
        this.fechaInicio=DateUtils.getCurrentDate();
    }
}
