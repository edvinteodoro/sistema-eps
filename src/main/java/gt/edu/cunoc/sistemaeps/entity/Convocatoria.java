package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.dto.ConvocatoriaDto;
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
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "convocatoria")
@Data
@NoArgsConstructor
public class Convocatoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_convocatoria")
    private Integer idConvocatoria;
    @Basic(optional = false)
    @Column(name = "correlativo")
    private String correlativo;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @Basic(optional = false)
    @Column(name = "fecha_evaluacion")
    private LocalDate fechaEvaluacion;
    @Column(name = "hora_evaluacion")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime horaEvaluacion;
    @Basic(optional = false)
    @Column(name = "salon")
    private String salon;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    @Column(name = "comentario")
    private String comentario;
    @Column(name = "nombre_representante")
    private String nombreRepresentante;
    @JoinColumn(name = "id_titulo_representante_fk", referencedColumnName = "id_titulo")
    @ManyToOne
    private Titulo idTituloRepresentanteFk;
    @JoinColumn(name = "id_proyecto_fk", referencedColumnName = "id_proyecto")
    @ManyToOne(optional = false)
    private Proyecto idProyectoFk;

    public Convocatoria(ConvocatoriaDto convocatoriaDto) {
        this.fecha = DateUtils.getCurrentDate();
        this.fechaEvaluacion = convocatoriaDto.getFechaEvaluacion();
        this.horaEvaluacion = convocatoriaDto.getFechaEvaluacion().atTime(convocatoriaDto.getHoraEvaluacion());
        this.salon = convocatoriaDto.getSalon();
        this.activo = Boolean.TRUE;
        this.comentario = convocatoriaDto.getComentario();
        this.nombreRepresentante = convocatoriaDto.getRepresentante();
    }
}
