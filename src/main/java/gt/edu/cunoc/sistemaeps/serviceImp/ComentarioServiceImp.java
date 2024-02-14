package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.ComentarioDto;
import gt.edu.cunoc.sistemaeps.entity.Comentario;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import gt.edu.cunoc.sistemaeps.repository.ComentarioRepository;
import gt.edu.cunoc.sistemaeps.service.ComentarioService;
import gt.edu.cunoc.sistemaeps.service.EtapaService;
import gt.edu.cunoc.sistemaeps.service.RolService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author edvin
 */
@Service
public class ComentarioServiceImp implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UsuarioService usuarioService;
    private final EtapaService etapaService;
    private final RolService rolService;
    private final UsuarioProyectoService usuarioProyectoService;

    public ComentarioServiceImp(ComentarioRepository comentarioRepository,
            UsuarioService usuarioService, EtapaService etapaService,
            RolService rolService, UsuarioProyectoService usuarioProyectoService) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioService = usuarioService;
        this.etapaService = etapaService;
        this.rolService = rolService;
        this.usuarioProyectoService = usuarioProyectoService;
    }

    @Override
    public Comentario saveComentario(Comentario comentario) throws Exception {
        return this.comentarioRepository.save(comentario);
    }

    @Override
    public Comentario crearComentario(ComentarioDto comentarioDto,
            EtapaProyecto etapaProyecto, Usuario usuario, Rol rol) throws Exception {
        Comentario comentario = new Comentario(comentarioDto);
        comentario.setIdUsuarioFk(usuario);
        comentario.setEtapaProyectoFk(etapaProyecto);
        comentario.setIdRolFk(rol);
        return saveComentario(comentario);
    }

    @Override
    public Page<Comentario> getComentarios(Integer idProyecto, Pageable pageable) throws Exception {
        return this.comentarioRepository.findComentariosProyecto(idProyecto, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comentario crearComentario(Integer idProyecto, ComentarioDto comentarioDto) throws Exception {
        EtapaProyecto etapaActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = etapaActiva.getIdProyectoFk();
        Rol rol = this.rolService.getLoggedUsuarioRol();
        switch (rol.getIdRol()) {
            case RolUtils.ID_ROL_ESTUDIANTE -> {
                if (!proyecto.getIdUsuarioFk().equals(usuario)) {
                    throw new Exception("No tiene permisos para agregar comentario");
                }
                return crearComentario(comentarioDto, etapaActiva, usuario, rol);
            }
            case RolUtils.ID_ROL_SECRETARIA -> {
                UsuarioProyecto secretariaProyecto = this.usuarioProyectoService.getSecretariaProyecto(idProyecto);
                if (!secretariaProyecto.getIdUsuarioFk().equals(usuario)) {
                    throw new Exception("No tiene permisos para agregar comentario");
                }
                return crearComentario(comentarioDto, etapaActiva, usuario, rol);
            }
            case RolUtils.ID_ROL_SUPERVISOR -> {
                UsuarioProyecto supervisorProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
                if (!supervisorProyecto.getIdUsuarioFk().equals(usuario)) {
                    throw new Exception("No tiene permisos para agregar comentario");
                }
                return crearComentario(comentarioDto, etapaActiva, usuario, rol);
            }
            case RolUtils.ID_ROL_ASESOR -> {
                UsuarioProyecto asesorProyecto = this.usuarioProyectoService.getAsesorProyecto(idProyecto);
                if (!asesorProyecto.getIdUsuarioFk().equals(usuario)) {
                    throw new Exception("No tiene permisos para agregar comentario");
                }
                return crearComentario(comentarioDto, etapaActiva, usuario, rol);
            }
            case RolUtils.ID_ROL_CONTRAPARTE -> {
                UsuarioProyecto contraparteProyecto = this.usuarioProyectoService.getContraparteProyecto(idProyecto);
                if (!contraparteProyecto.getIdUsuarioFk().equals(usuario)) {
                    throw new Exception("No tiene permisos para agregar comentario");
                }
                return crearComentario(comentarioDto, etapaActiva, usuario, rol);
            }
            default ->
                throw new Exception("No tiene permisos para comentar proyecto");
        }
    }

}
