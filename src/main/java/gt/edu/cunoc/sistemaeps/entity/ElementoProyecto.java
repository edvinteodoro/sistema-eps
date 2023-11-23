package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.util.DateUtils;
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
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "elemento_proyecto")
@Data
public class ElementoProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_elementos_proyecto")
    private Integer idElementosProyecto;
    @Basic(optional = false)
    @Column(name = "informacion")
    private String informacion;
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaCreacion;
    @JoinColumn(name = "id_elemento_fk", referencedColumnName = "id_elemento")
    @ManyToOne(optional = false)
    private Elemento idElementoFk;
    @JoinColumn(name = "id_etapa_proyecto_fk", referencedColumnName = "id_etapa_proyecto")
    @ManyToOne(optional = false)
    private EtapaProyecto idEtapaProyectoFk;
    
    public ElementoProyecto(){
        this.activo=Boolean.TRUE;
        this.fechaCreacion=DateUtils.getCurrentDate();
    }
}
