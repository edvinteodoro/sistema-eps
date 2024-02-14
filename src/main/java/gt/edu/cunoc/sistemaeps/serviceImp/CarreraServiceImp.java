package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Carrera;
import gt.edu.cunoc.sistemaeps.entity.CarreraUsuario;
import gt.edu.cunoc.sistemaeps.repository.CarreraRepository;
import gt.edu.cunoc.sistemaeps.repository.CarreraUsuarioRepository;
import gt.edu.cunoc.sistemaeps.service.CarreraService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class CarreraServiceImp implements CarreraService {

    private final CarreraRepository carreraRepository;
    private final CarreraUsuarioRepository carreraUsuarioRepository;

    public CarreraServiceImp(CarreraRepository carreraRepository,
            CarreraUsuarioRepository carreraUsuarioRepository) {
        this.carreraRepository = carreraRepository;
        this.carreraUsuarioRepository=carreraUsuarioRepository;
    }

    @Override
    public Carrera getCarrera(Integer idCarrera) throws Exception {
        return this.carreraRepository.findById(idCarrera).get();
    }

    @Override
    public List<Carrera> getCarreras() throws Exception {
        return this.carreraRepository.findAll();
    }
    
    @Override
    public List<CarreraUsuario> getCarrerasUsuario(String registroAcademico) throws Exception {
        return this.carreraUsuarioRepository.findCarrerasUsuario(registroAcademico); 
    }
    
    @Override
    public List<CarreraUsuario> getCarrerasUsuario(Integer idUsuario) throws Exception{
        return this.carreraUsuarioRepository.findCarrerasUsuario(idUsuario);
    }

}
