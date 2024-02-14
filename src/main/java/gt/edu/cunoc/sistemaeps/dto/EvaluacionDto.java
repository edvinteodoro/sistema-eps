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
    private LocalTime hora;
    private TituloDto tituloRepresentante;
    private String representante;
    private String salon;
    private String comentario;
    private String dia;
    
}
