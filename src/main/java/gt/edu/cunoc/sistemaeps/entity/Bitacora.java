package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.dto.BitacoraDto;
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
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "bitacora")
@Data
@NoArgsConstructor
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_bitacora")
    private Integer idBitacora;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "avance")
    private int avance;
    @Basic(optional = false)
    @Column(name = "revision_asesor")
    private boolean revisionAsesor;
    @Basic(optional = false)
    @Column(name = "revision_supervisor")
    private boolean revisionSupervisor;
    @Basic(optional = false)
    @Column(name = "revision_contraparte")
    private boolean revisionContraparte;
    @Basic(optional = false)
    @Column(name = "contiene_informe")
    private boolean contieneInforme;
    @Basic(optional = false)
    @Column(name = "fecha_reporte")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaReporte;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBitacoraFk")
    private List<Recurso> recursoList;
    @JoinColumn(name = "id_proyecto_fk", referencedColumnName = "id_proyecto")
    @ManyToOne(optional = false)
    private Proyecto idProyectoFk;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBitacoraFk")
    private List<ComentarioBitacora> comentarioBitacoraList;

    public Bitacora(BitacoraDto bitacoraDto) {
        this.descripcion = bitacoraDto.getDescripcion();
        this.avance = bitacoraDto.getAvance();
        this.revisionAsesor = Boolean.FALSE;
        this.contieneInforme = Boolean.FALSE;
        this.revisionSupervisor = Boolean.FALSE;
        this.revisionContraparte = Boolean.FALSE;
        this.fechaReporte = bitacoraDto.getFechaReporte();
        this.fecha = DateUtils.getCurrentDate();
    }
}
