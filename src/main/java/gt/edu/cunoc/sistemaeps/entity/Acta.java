package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.dto.ActaDto;
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
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "acta")
@Data
@NoArgsConstructor
public class Acta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_acta")
    private Integer idActa;
    @Basic(optional = false)
    @Column(name = "correlativo")
    private String correlativo;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @Column(name = "fecha_evaluacion")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaEvaluacion;
    @Column(name = "hora_inicio_evaluacion")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime horaInicioEvaluacion;
    @Basic(optional = false)
    @Column(name = "hora_fin_evaluacion")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime horaFinEvaluacion;
    @Basic(optional = false)
    @Column(name = "nota")
    private Integer nota;
    @Column(name = "semestre")
    private String semestre;
    @Basic(optional = false)
    @Column(name = "salon")
    private String salon;
    @Basic(optional = false)
    @Column(name = "resultado")
    private String resultado;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    @Basic(optional = false)
    @Column(name = "acta_generada")
    private boolean actaGenerada;
    @Column(name = "comentario")
    private String comentario;
    @JoinColumn(name = "id_proyecto_fk", referencedColumnName = "id_proyecto")
    @ManyToOne(optional = false)
    private Proyecto idProyectoFk;

    public Acta(ActaDto actaDto) {
        this.fecha = DateUtils.getCurrentDate();
        //this.fechaEvaluacion = actaDto.getFechaEvaluacion();
        this.horaInicioEvaluacion = DateUtils.getCurrentDate().atTime(actaDto.getHoraInicioEvaluacion());
        this.horaFinEvaluacion = DateUtils.getCurrentDate().atTime(actaDto.getHoraFinEvaluacion());
        this.nota = actaDto.getNota();
        this.resultado = actaDto.getResultado();
        System.out.println("resultado: " + actaDto.getResultado());
        this.activo = Boolean.TRUE;
        this.actaGenerada = Boolean.FALSE;
        //this.salon = actaDto.getSalon();
        this.comentario = actaDto.getComentario();
    }
}
