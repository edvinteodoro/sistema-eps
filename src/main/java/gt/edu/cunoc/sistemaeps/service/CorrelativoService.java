package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.EvaluacionDto;
import gt.edu.cunoc.sistemaeps.entity.Correlativo;
import gt.edu.cunoc.sistemaeps.entity.CorrelativoEstudiante;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import java.util.List;

/**
 *
 * @author edvin
 */
public interface CorrelativoService {
    public Correlativo save(Correlativo correlativo);
    public CorrelativoEstudiante crearCorrelativoConvocatoriaAnteproyecto(Usuario estudiante,
            Integer idCarrera, EvaluacionDto evaluacionDto) throws Exception;
    public CorrelativoEstudiante crearCorrelativoActaAnteproyecto(Usuario estudiante) throws Exception;
    public List<Correlativo> getAll();
}
