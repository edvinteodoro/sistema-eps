package gt.edu.cunoc.sistemaeps.entity;

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
@Table(name = "correlativo")
@Data
public class Correlativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_correlativo")
    private Integer idCorrelativo;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "numeracion_actual")
    private int numeracionActual;
    @Column(name = "ultima_actualizacion")
    @Temporal(TemporalType.DATE)
    private LocalDate ultimaActualizacion;
    @JoinColumn(name = "id_carrera_fk", referencedColumnName = "id_carrera")
    @ManyToOne
    private Carrera idCarreraFk;
    @JoinColumn(name = "id_etapa_fk", referencedColumnName = "id_etapa")
    @ManyToOne
    private Etapa idEtapaFk;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCorrelativoFk")
    private List<CorrelativoEstudiante> correlativoEstudianteList;

}
