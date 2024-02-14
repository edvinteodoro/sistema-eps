package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import gt.edu.cunoc.sistemaeps.entity.Persona;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import java.util.List;

/**
 *
 * @author edvin
 */
public interface PersonaService {

    
    public Persona crearPersona(UsuarioDto usuarioDto, Proyecto proyecto,String rol) throws Exception;

    public Persona save(Persona persona) throws Exception;

    public Persona getPersona(Integer idPersona) throws Exception;

    public Persona getContraparte(Integer idProyecto) throws Exception;

    public Persona getAsesor(Integer idProyecto) throws Exception;

    public List<Persona> getAsesoresTecnicos(Integer idProyecto) throws Exception;
    
    public Persona actualizarPersona(Integer idProyecto, Integer idPersona,UsuarioDto usuarioDto) throws Exception;
    
    public void eliminiarPersona(Integer idPersona) throws Exception;
}
