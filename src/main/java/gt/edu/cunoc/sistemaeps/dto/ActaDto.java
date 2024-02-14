package gt.edu.cunoc.sistemaeps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gt.edu.cunoc.sistemaeps.entity.Acta;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */

@Data
@NoArgsConstructor
public class ActaDto {
    private Integer idActa;
    private LocalDate fecha;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaEvaluacion;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicioEvaluacion;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFinEvaluacion;
    private ProyectoDto proyecto;
    private Integer nota;
    private boolean actaGenerada;
    private String semestre;
    private String resultado;
    private String comentario;
    private String salon;
    private String tipo;
    
    public ActaDto(Acta acta){
        this.idActa = acta.getIdActa();
        this.fecha= acta.getFecha();
        this.fechaEvaluacion = acta.getFechaEvaluacion();
        this.horaInicioEvaluacion = acta.getHoraInicioEvaluacion().toLocalTime();
        this.horaFinEvaluacion = acta.getHoraFinEvaluacion().toLocalTime();
        this.proyecto = new ProyectoDto(acta.getIdProyectoFk());
        this.nota = acta.getNota();
        this.tipo = acta.getTipo();
        this.semestre = acta.getSemestre();
        this.resultado = acta.getResultado();
        this.comentario = acta.getComentario();
        this.salon = acta.getSalon();
        this.actaGenerada = acta.isActaGenerada();
    }
}
