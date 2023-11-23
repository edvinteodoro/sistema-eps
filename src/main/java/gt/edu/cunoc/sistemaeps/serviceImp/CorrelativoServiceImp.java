package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.EvaluacionDto;
import gt.edu.cunoc.sistemaeps.entity.Correlativo;
import gt.edu.cunoc.sistemaeps.entity.CorrelativoEstudiante;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.CorrelativoEstudianteRepository;
import gt.edu.cunoc.sistemaeps.repository.CorrelativoRepository;
import gt.edu.cunoc.sistemaeps.service.CorrelativoService;
import gt.edu.cunoc.sistemaeps.util.EtapaUtils;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class CorrelativoServiceImp implements CorrelativoService {

    private final CorrelativoRepository correlativoRepository;
    private final CorrelativoEstudianteRepository correlativoEstudianteRepository;

    public CorrelativoServiceImp(CorrelativoRepository correlativoRepository,
            CorrelativoEstudianteRepository correlativoEstudianteRepository) {
        this.correlativoRepository = correlativoRepository;
        this.correlativoEstudianteRepository = correlativoEstudianteRepository;
    }

    private CorrelativoEstudiante save(CorrelativoEstudiante correlativoEstudiante) {
        return correlativoEstudianteRepository.save(correlativoEstudiante);
    }

    @Override
    public Correlativo save(Correlativo correlativo) {
        return correlativoRepository.save(correlativo);
    }
    
    @Override
    public List<Correlativo> getAll() {
        return this.correlativoRepository.findAll();
    }

    private Correlativo getCorrelativo(Integer idCarrera, Integer idEtapa) {
        return this.correlativoRepository.findCorrelativo(idCarrera, idEtapa);
    }

    private CorrelativoEstudiante getCorrelativoEstudianteActivo(Integer idUsuario,
            Integer idCorrelativo) {
        return this.correlativoEstudianteRepository
                .findCorrelativoEstudianteActivo(idUsuario, idCorrelativo, Boolean.FALSE);
    }

    public Correlativo getCorrelativo(Integer idEtapa) {
        return this.correlativoRepository.findCorrelativo(idEtapa);
    }

    @Override
    public CorrelativoEstudiante crearCorrelativoConvocatoriaAnteproyecto(Usuario estudiante,
            Integer idCarrera, EvaluacionDto evaluacionDto) throws Exception {
        Correlativo correlativo = getCorrelativo(idCarrera, EtapaUtils.ID_ETAPA_PROGRAMACION_PRESENTACION_ANTEPROYECTO);
        String formato = "%02d-%d";
        anularCorrelativoEstudianteActivo(estudiante,correlativo);
        return generarCorrelativo(correlativo, estudiante, formato);
    }

    @Override
    public CorrelativoEstudiante crearCorrelativoActaAnteproyecto(Usuario estudiante) throws Exception{
        Correlativo correlativo = getCorrelativo(EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO);
        String formato = "%02d-%d";
        anularCorrelativoEstudianteActivo(estudiante,correlativo);
        return generarCorrelativo(correlativo, estudiante, formato);
    }

    private void anularCorrelativoEstudianteActivo(Usuario estudiante, Correlativo correlativo) {
        CorrelativoEstudiante correlativoEstudianteActivo = getCorrelativoEstudianteActivo(
                estudiante.getIdUsuario(), correlativo.getIdCorrelativo());
        if (correlativoEstudianteActivo != null) {
            correlativoEstudianteActivo.setAnulado(Boolean.TRUE);
            save(correlativoEstudianteActivo);
        }
    }

    private CorrelativoEstudiante generarCorrelativo(Correlativo correlativo,
            Usuario estudiante, String formato) {
        Integer numeroCorrelativo = correlativo.getNumeracionActual() + 1;
        Integer anio = correlativo.getUltimaActualizacion().getYear();
        String correlativoString = String.format(formato, numeroCorrelativo, anio);
        CorrelativoEstudiante correlativoEstudiante = new CorrelativoEstudiante();
        correlativoEstudiante.setNumeroCorrelativo(numeroCorrelativo);
        correlativoEstudiante.setCorrelativo(correlativoString);
        correlativoEstudiante.setIdEstudianteFk(estudiante);
        correlativoEstudiante.setIdCorrelativoFk(correlativo);
        correlativo.setNumeracionActual(numeroCorrelativo);
        save(correlativo);
        return save(correlativoEstudiante);
    }
}
