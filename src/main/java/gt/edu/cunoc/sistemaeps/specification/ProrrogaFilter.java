package gt.edu.cunoc.sistemaeps.specification;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */

@Data
@NoArgsConstructor
public class ProrrogaFilter {
    private String nombreEstudiante;
    private String registroEstudiante;
    private Integer idUsuarioAsignado;
}
