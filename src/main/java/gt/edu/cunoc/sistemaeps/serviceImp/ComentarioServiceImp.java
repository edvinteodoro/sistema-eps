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
                Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
                if (!secretaria.equals(usuario)) {
                    throw new Exception("No tiene permisos para agregar comentario");
                }
                return crearComentario(comentarioDto, etapaActiva, usuario, rol);
            }
            case RolUtils.ID_ROL_SUPERVISOR, RolUtils.ID_ROL_ASESOR, RolUtils.ID_ROL_CONTRAPARTE, RolUtils.ID_ROL_COORDINADOR_EPS, RolUtils.ID_ROL_COORDINADOR_CARRERA -> {
                Integer idCarrera = proyecto.getIdCarreraFk().getIdCarrera();
                UsuarioProyecto asesorProyecto = null;
                UsuarioProyecto contraparteProyecto=null;
                Usuario supervisor=null;
                Usuario coordinadorEps=null;
                Usuario coordinadorCarrera=null;
                try {
                    contraparteProyecto = this.usuarioProyectoService.getContraparteProyecto(idProyecto);
                } catch (Exception e) {
                }
                try {
                    asesorProyecto = this.usuarioProyectoService.getAsesorProyecto(idProyecto);
                } catch (Exception e) {
                }
                try {
                    supervisor = this.usuarioProyectoService.getSupervisorDisponible(idCarrera);
                } catch (Exception e) {
                }
                try {
                    coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
                } catch (Exception e) {
                }
                try {
                    coordinadorCarrera = this.usuarioProyectoService.getCoordinadorCarreraDisponible(idCarrera);
                } catch (Exception e) {
                }
                if (contraparteProyecto != null && contraparteProyecto.getIdUsuarioFk().equals(usuario)) {
                    rol = this.rolService.getRol(RolUtils.ID_ROL_CONTRAPARTE);
                } else if (asesorProyecto != null && asesorProyecto.getIdUsuarioFk().equals(usuario)) {
                    rol = this.rolService.getRol(RolUtils.ID_ROL_ASESOR);
                } else if (supervisor.equals(usuario) || coordinadorEps.equals(usuario) || coordinadorCarrera.equals(usuario)) {

                } else {
                    throw new Exception("No tiene permisos para agregar comentario");
                }
                return crearComentario(comentarioDto, etapaActiva, usuario, rol);
            }
            default ->
                throw new Exception("No tiene permisos para comentar proyecto");
        }
    }

}
