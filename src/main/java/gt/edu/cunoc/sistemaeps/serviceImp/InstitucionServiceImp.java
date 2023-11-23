package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.InstitucionDto;
import gt.edu.cunoc.sistemaeps.entity.Institucion;
import gt.edu.cunoc.sistemaeps.entity.Municipio;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.InstitucionRepository;
import gt.edu.cunoc.sistemaeps.service.DepartamentoService;
import gt.edu.cunoc.sistemaeps.service.InstitucionService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class InstitucionServiceImp implements InstitucionService {

    private final InstitucionRepository institucionRepository;
    private final DepartamentoService departamentoService;
    private final UsuarioService usuarioService;

    public InstitucionServiceImp(InstitucionRepository institucionRepository,
            DepartamentoService departamentoService,UsuarioService usuarioService) {
        this.institucionRepository = institucionRepository;
        this.departamentoService = departamentoService;
        this.usuarioService = usuarioService;
    }
    
    public Institucion getInstitucion(Integer idInstitucion) throws Exception{
        return this.institucionRepository.findById(idInstitucion).get();
    }

    @Override
    public Institucion crearInstitucion(InstitucionDto institucionDto) throws Exception {
        Institucion institucion = new Institucion(institucionDto);
        Municipio municipio = this.departamentoService.getMunicipio(institucionDto.getMunicipio().getIdMunicipio());
        institucion.setIdMunicipioFk(municipio);
        return save(institucion);
    }

    @Override
    public Institucion save(Institucion institucion) throws Exception {
        return this.institucionRepository.save(institucion);
    }

    @Override
    public Institucion actualizarInstitucion(Integer idProyecto,InstitucionDto institucionDto) throws Exception {
        Institucion institucion = getInstitucion(institucionDto.getIdInstitucion()); 
        Proyecto proyecto = institucion.getProyecto();
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        if(!proyecto.getIdUsuarioFk().equals(usuario)){
            throw new Exception("No tiene permisos para actualizar institucion");
        }
        institucion.actualizar(institucionDto);
        return save(institucion);
    }

}
