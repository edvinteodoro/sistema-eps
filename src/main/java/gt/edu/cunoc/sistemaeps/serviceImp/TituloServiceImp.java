package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Titulo;
import gt.edu.cunoc.sistemaeps.repository.TituloRepository;
import gt.edu.cunoc.sistemaeps.service.TituloService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class TituloServiceImp implements TituloService {

    public final TituloRepository tituloRepository;

    public TituloServiceImp(TituloRepository tituloRepository) {
        this.tituloRepository = tituloRepository;
    }

    @Override
    public Titulo getTitulo(Integer idTitulo) {
        return this.tituloRepository.findById(idTitulo).get();
    }

    @Override
    public List<Titulo> getTitulos() {
        return this.tituloRepository.findAll();
    }

}
