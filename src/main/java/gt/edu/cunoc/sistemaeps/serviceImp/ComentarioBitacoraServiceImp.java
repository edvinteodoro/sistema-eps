package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.ComentarioBitacoraDto;
import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import gt.edu.cunoc.sistemaeps.entity.ComentarioBitacora;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import gt.edu.cunoc.sistemaeps.repository.ComentarioBitacoraRepository;
import gt.edu.cunoc.sistemaeps.service.BitacoraService;
import gt.edu.cunoc.sistemaeps.service.ComentarioBitacoraService;
import gt.edu.cunoc.sistemaeps.service.RolService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author edvin
 */
@Service
public class ComentarioBitacoraServiceImp implements ComentarioBitacoraService {

    private final ComentarioBitacoraRepository comentarioBitacoraRepository;
    private final UsuarioService usuarioService;
    private final BitacoraService bitacoraService;
    private final UsuarioProyectoService usuarioProyectoService;
    private final RolService rolService;

    public ComentarioBitacoraServiceImp(ComentarioBitacoraRepository comentarioBitacoraRepository,
            UsuarioService usuarioService, BitacoraService bitacoraService,
            UsuarioProyectoService usuarioProyectoService, RolService rolService) {
        this.comentarioBitacoraRepository = comentarioBitacoraRepository;
        this.usuarioService = usuarioService;
        this.bitacoraService = bitacoraService;
        this.usuarioProyectoService = usuarioProyectoService;
        this.rolService = rolService;
    }

    @Override
    public Page<ComentarioBitacora> getComentariosBitacora(Integer idBitacora, Pageable pageable) throws Exception {
        return this.comentarioBitacoraRepository.findComentariosBitacora(idBitacora, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ComentarioBitacora crearComentario(Integer idBitacora, ComentarioBitacoraDto comentarioBitacoraDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Bitacora bitacora = this.bitacoraService.getBitacora(idBitacora);
        Proyecto proyecto = bitacora.getIdProyectoFk();
        UsuarioProyecto supervisor = this.usuarioProyectoService.getSupervisorProyecto(proyecto.getIdProyecto());
        UsuarioProyecto asesor = this.usuarioProyectoService.getAsesorProyecto(proyecto.getIdProyecto());
        UsuarioProyecto contraparte = this.usuarioProyectoService.getContraparteProyecto(proyecto.getIdProyecto());
        if (proyecto.getIdUsuarioFk().equals(usuario)) {
            return crearComentario(bitacora, usuario, this.rolService.getLoggedUsuarioRol(), comentarioBitacoraDto);
        } else if (supervisor.getIdUsuarioFk().equals(usuario)) {
            return crearComentario(bitacora, usuario, supervisor.getIdRolFk(), comentarioBitacoraDto);
        } else if (asesor.getIdUsuarioFk().equals(usuario)) {
            return crearComentario(bitacora, usuario, asesor.getIdRolFk(), comentarioBitacoraDto);
        } else if (contraparte.getIdUsuarioFk().equals(usuario)) {
            return crearComentario(bitacora, usuario, contraparte.getIdRolFk(), comentarioBitacoraDto);
        } else {
            throw new Exception("No tiene permisos para hacer comentario");
        }

    }

    private ComentarioBitacora crearComentario(Bitacora bitacora, Usuario usuario,
            Rol rol, ComentarioBitacoraDto comentarioBitacoraDto) throws Exception {
        ComentarioBitacora comentario = new ComentarioBitacora(comentarioBitacoraDto);
        comentario.setIdBitacoraFk(bitacora);
        comentario.setIdUsuarioFk(usuario);
        comentario.setIdRolFk(rol);
        return this.comentarioBitacoraRepository.save(comentario);
    }

}
