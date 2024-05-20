package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.entity.Carrera;
import gt.edu.cunoc.sistemaeps.entity.CarreraUsuario;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import java.util.List;

/**
 *
 * @author edvin
 */
public interface CarreraService {

    public Carrera getCarrera(Integer idCarrera) throws Exception;
    public List<Carrera> getCarreras() throws Exception;
    public List<CarreraUsuario> getCarrerasUsuario(String registroAcademico) throws Exception;
    public List<CarreraUsuario> getCarrerasUsuario(Integer idUsuario) throws Exception;
    public void eliminarCarreras(Usuario usuario) throws Exception;
}
