package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import gt.edu.cunoc.sistemaeps.entity.Persona;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.PersonaRepository;
import gt.edu.cunoc.sistemaeps.service.PersonaService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class PersonaServiceImp implements PersonaService {

    private final PersonaRepository personaRepository;
    private final UsuarioService usuarioService;
    private final String ROL_ASESOR_TECNICO = "ASESOR TECNICO";

    public PersonaServiceImp(PersonaRepository personaRepository,
            UsuarioService usuarioService) {
        this.personaRepository = personaRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public Persona crearPersona(UsuarioDto usuarioDto, Proyecto proyecto, String rol) throws Exception {
        Persona persona = new Persona(usuarioDto);
        persona.setIdProyectoFk(proyecto);
        persona.setRol(rol);
        return save(persona);
    }

    @Override
    public Persona save(Persona persona) throws Exception {
        return this.personaRepository.save(persona);
    }

    @Override
    public void eliminiarPersona(Integer idPersona) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Persona persona = this.personaRepository.findById(idPersona).get();
        if(!persona.getIdProyectoFk().getIdUsuarioFk().equals(usuario)){
            throw new Exception("No tiene permisos para eliminar persona");
        }
        if (!persona.getRol().equals(ROL_ASESOR_TECNICO)) {
            throw new Exception("No se puede eliminar persona");
        }
        this.personaRepository.delete(persona);
    }

    @Override
    public Persona getContraparte(Integer idProyecto) throws Exception {
        return this.personaRepository.findPersona(idProyecto, RolUtils.ROL_CONTRAPARTE);
    }

    @Override
    public Persona getAsesor(Integer idProyecto) throws Exception {
        return this.personaRepository.findPersona(idProyecto, RolUtils.ROL_ASESOR);
    }

    @Override
    public List<Persona> getAsesoresTecnicos(Integer idProyecto) throws Exception {
        return this.personaRepository.findPersonas(idProyecto, RolUtils.ROL_ASESOR_TECNICO);
    }

    @Override
    public Persona getPersona(Integer idPersona) throws Exception {
        return this.personaRepository.findById(idPersona).get();
    }

    @Override
    public Persona actualizarPersona(Integer idProyecto, Integer idPersona, UsuarioDto usuarioDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Persona persona = getPersona(idPersona);
        Proyecto proyecto = persona.getIdProyectoFk();
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("Sin permisos para realizar cambios");
        }
        persona.updatePersona(usuarioDto);
        return save(persona);
    }
}
