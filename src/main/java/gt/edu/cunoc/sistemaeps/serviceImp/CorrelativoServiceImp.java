package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Correlativo;
import gt.edu.cunoc.sistemaeps.repository.CorrelativoRepository;
import gt.edu.cunoc.sistemaeps.service.CorrelativoService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class CorrelativoServiceImp implements CorrelativoService {

    private final CorrelativoRepository correlativoRepository;

    public CorrelativoServiceImp(CorrelativoRepository correlativoRepository) {
        this.correlativoRepository = correlativoRepository;
    }

    @Override
    public Correlativo save(Correlativo correlativo) {
        return correlativoRepository.save(correlativo);
    }

    @Override
    public List<Correlativo> getAll() {
        return this.correlativoRepository.findAll();
    }

    @Override
    public Correlativo getCorrelativo(Integer idCarrera, Integer idEtapa) {
        return this.correlativoRepository.findCorrelativo(idCarrera, idEtapa);
    }
    
    @Override
    public Correlativo getCorrelativo(Integer idEtapa) {
        return this.correlativoRepository.findCorrelativo(idEtapa);
    }
}
