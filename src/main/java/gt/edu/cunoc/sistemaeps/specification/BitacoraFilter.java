package gt.edu.cunoc.sistemaeps.specification;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class BitacoraFilter {
    private String nombreEstudiante;
    private Boolean revisionContraparte;
    private String registroEstudiante;
    private Integer idUsuarioAsignado;
    private Integer idUsuario;
    private Integer idCarrera;
    private Integer idProyecto;
}
