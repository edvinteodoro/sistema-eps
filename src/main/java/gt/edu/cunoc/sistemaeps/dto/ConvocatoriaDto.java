package gt.edu.cunoc.sistemaeps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gt.edu.cunoc.sistemaeps.entity.Convocatoria;
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
public class ConvocatoriaDto {

    private Integer idConvocatoria;
    private LocalDate fecha;
    private LocalDate fechaEvaluacion;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaEvaluacionFormat;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaEvaluacion;
    private TituloDto tituloRepresentante;
    private String representante;
    private String salon;
    private String tipo;
    private String comentario;

    public ConvocatoriaDto(Convocatoria convocatoria) {
        this.idConvocatoria = convocatoria.getIdConvocatoria();
        this.fecha = convocatoria.getFecha();
        this.fechaEvaluacion = convocatoria.getFechaEvaluacion();
        this.fechaEvaluacionFormat = convocatoria.getFechaEvaluacion();
        this.horaEvaluacion = convocatoria.getHoraEvaluacion().toLocalTime();
        this.salon = convocatoria.getSalon();
        this.tipo = convocatoria.getTipo();
    }
}
