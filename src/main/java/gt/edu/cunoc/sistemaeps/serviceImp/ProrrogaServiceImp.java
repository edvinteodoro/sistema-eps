package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.ProrrogaDto;
import gt.edu.cunoc.sistemaeps.entity.Prorroga;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.ProrrogaRepository;
import gt.edu.cunoc.sistemaeps.service.NotificacionService;
import gt.edu.cunoc.sistemaeps.service.ProrrogaService;
import gt.edu.cunoc.sistemaeps.service.ProyectoService;
import gt.edu.cunoc.sistemaeps.service.RolService;
import gt.edu.cunoc.sistemaeps.service.StorageService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.specification.ProrrogaFilter;
import gt.edu.cunoc.sistemaeps.specification.ProrrogaSpecification;
import gt.edu.cunoc.sistemaeps.util.DateUtils;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
@Service
public class ProrrogaServiceImp implements ProrrogaService {

    private final ProrrogaRepository prorrogaRepository;
    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final StorageService storageService;
    private final ProyectoService proyectoService;
    private final UsuarioProyectoService usuarioProyectoService;
    private final NotificacionService notificacionService;

    public ProrrogaServiceImp(ProrrogaRepository prorrogaRepository,
            UsuarioService usuarioService, RolService rolService,
            StorageService storageService, ProyectoService proyectoService,
            UsuarioProyectoService usuarioProyectoService,
            NotificacionService notificacionService) {
        this.prorrogaRepository = prorrogaRepository;
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.storageService = storageService;
        this.proyectoService = proyectoService;
        this.usuarioProyectoService = usuarioProyectoService;
        this.notificacionService = notificacionService;
    }

    @Override
    public Page<Prorroga> getProrrogas(Pageable pageable) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Rol rolUsuario = this.rolService.getLoggedUsuarioRol();
        ProrrogaFilter filter = new ProrrogaFilter();
        if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_ESTUDIANTE)) {
            filter.setRegistroEstudiante(usuario.getRegistroAcademico());
            Specification<Prorroga> spec = ProrrogaSpecification.filterBy(filter);
            return prorrogaRepository.findAll(spec, pageable);
        } else {
            filter.setIdUsuarioAsignado(usuario.getIdUsuario());
            Specification<Prorroga> spec = ProrrogaSpecification.filterBy(filter);
            return prorrogaRepository.findAll(spec, pageable);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Prorroga crearProrroga(Integer idProyecto, ProrrogaDto prorrogaDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = this.proyectoService.getProyecto(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("Usuario no tiene permisos para solicitar prorroga");
        }
        Prorroga prorroga = new Prorroga();
        prorroga.setDiasExtension(prorrogaDto.getDiasExtension());
        prorroga.setFechaSolicitud(DateUtils.getCurrentDate());
        prorroga.setIdProyectoFk(proyecto);
        prorroga.setLinkAmparo(saveFile(prorrogaDto.getAmparoFile(), usuario));
        prorroga.setLinkSolicitud(saveFile(prorrogaDto.getSolicitudFile(), usuario));
        notificarSolicitudProrroga(proyecto);
        return this.prorrogaRepository.save(prorroga);
    }

    private void notificarSolicitudProrroga(Proyecto proyecto) {
        try {
            Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
            this.notificacionService.notificarSolicitudProrroga(supervisor, proyecto);
        } catch (Exception e) {
        }
    }

    private void notificarRespuestaProrroga(Proyecto proyecto) {
        try {
            this.notificacionService.notificarSolicitudProrroga(proyecto.getIdUsuarioFk(), proyecto);
        } catch (Exception e) {
        }
    }

    @Override
    public Prorroga getProrroga(Integer idProrroga) throws Exception {
        Prorroga prorroga = this.prorrogaRepository.findById(idProrroga).get();
        if (prorroga == null) {
            throw new Exception("No se encontro el registro");
        }
        return prorroga;
    }

    @Override
    public Prorroga responderProrroga(Integer idProrroga, ProrrogaDto prorrogaDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Prorroga prorroga = this.prorrogaRepository.findById(idProrroga).get();
        Usuario supervisor = this.usuarioProyectoService
                .getSupervisorDisponible(prorroga.getIdProyectoFk().getIdCarreraFk().getIdCarrera());
        if (!supervisor.equals(usuario)) {
            throw new Exception("No tiene permisos para responder a esta solicitud de prorroga");
        }
        prorroga.setComentarioSupervisor(prorrogaDto.getComentarioSupervisor());
        prorroga.setAprobado(prorrogaDto.getAprobado());
        notificarRespuestaProrroga(prorroga.getIdProyectoFk());
        return prorrogaRepository.save(prorroga);
    }

    @Override
    public Prorroga actualizarProrroga(Integer idProrroga, ProrrogaDto prorrogaDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Prorroga prorroga = this.prorrogaRepository.findById(idProrroga).get();
        if (!prorroga.getIdProyectoFk().getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para responder a esta solicitud de prorroga");
        }
        if (prorroga.getAprobado() != null) {
            throw new Exception("Ya no se puede editar esta solicitud");
        }
        if (prorrogaDto.getDiasExtension() != null) {
            prorroga.setDiasExtension(prorrogaDto.getDiasExtension());
        }
        if (prorrogaDto.getAmparoFile() != null) {
            prorroga.setLinkAmparo(saveFile(prorrogaDto.getAmparoFile(), usuario));
        }
        if (prorrogaDto.getSolicitudFile() != null) {
            prorroga.setLinkSolicitud(saveFile(prorrogaDto.getSolicitudFile(), usuario));
        }
        return this.prorrogaRepository.save(prorroga);
    }

    private String saveFile(MultipartFile file, Usuario usuario) throws Exception {
        String registro = usuario.getRegistroAcademico();
        return this.storageService.saveFile(file, registro);
    }

}
