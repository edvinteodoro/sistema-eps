package gt.edu.cunoc.sistemaeps.specification;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class ProyectoFilter {
    private String nombreEstudiante;
    private Boolean activo;
    private String registroEstudiante;
    private Integer idUsuarioAsignado;
    private Integer idSecretaria;
    private Integer idSupervisor;
    private Integer idAsesor;
    private Integer idCoordinadorCarrera;
}
