package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.dto.EvaluacionDto;
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
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "correlativo_estudiante")
@Data
@NoArgsConstructor
public class CorrelativoEstudiante{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_correlativo_estudiante")
    private Integer idCorrelativoEstudiante;
    @Basic(optional = false)
    @Column(name = "numero_correlativo")
    private int numeroCorrelativo;
    @Basic(optional = false)
    @Column(name = "correlativo")
    private String correlativo;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @Column(name = "fecha_hora_evaluacion_anteproyecto")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaHoraEvaluacionAnteproyecto;
    @Column(name = "nota_evaluacion_anteproyecto")
    private Integer notaEvaluacionAnteproyecto;
    @Column(name = "resultado_evaluacion_anteproyecto")
    private String resultadoEvaluacionAnteproyecto;
    @Column(name = "comentario_resultado_evaluacion_anteproyecto")
    private String comentarioResultadoEvaluacionAnteproyecto;
    @Basic(optional = false)
    @Column(name = "anulado")
    private boolean anulado;
    @JoinColumn(name = "id_correlativo_fk", referencedColumnName = "id_correlativo")
    @ManyToOne(optional = false)
    private Correlativo idCorrelativoFk;
    @JoinColumn(name = "id_estudiante_fk", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idEstudianteFk;
    
    public CorrelativoEstudiante(EvaluacionDto evaluacionDto){
        this.fecha=DateUtils.getCurrentDate();
        this.fechaHoraEvaluacionAnteproyecto=LocalDateTime.of(evaluacionDto.getFecha(),evaluacionDto.getHora());
        this.notaEvaluacionAnteproyecto=evaluacionDto.getNota();
        this.resultadoEvaluacionAnteproyecto=evaluacionDto.getResultado();
        this.comentarioResultadoEvaluacionAnteproyecto = evaluacionDto.getComentario();
        this.anulado=false;
    }
}
