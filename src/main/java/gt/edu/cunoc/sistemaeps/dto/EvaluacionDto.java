package gt.edu.cunoc.sistemaeps.dto;

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
public class EvaluacionDto {
    private LocalDate fecha;
    private String dia;
    private LocalTime hora;
    private String representante;
    private TituloDto tituloRepresentante;
    private Integer nota;
    private String resultado; 
    private String comentario;
    private String salon;
}
