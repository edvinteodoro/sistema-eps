package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.ActaDto;
import gt.edu.cunoc.sistemaeps.dto.BitacoraDto;
import gt.edu.cunoc.sistemaeps.dto.ComentarioDto;
import gt.edu.cunoc.sistemaeps.dto.ConvocatoriaDto;
import gt.edu.cunoc.sistemaeps.dto.ProyectoDto;
import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import gt.edu.cunoc.sistemaeps.entity.Acta;
import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import gt.edu.cunoc.sistemaeps.entity.Carrera;
import gt.edu.cunoc.sistemaeps.entity.Comentario;
import gt.edu.cunoc.sistemaeps.entity.Convocatoria;
import gt.edu.cunoc.sistemaeps.entity.Elemento;
import gt.edu.cunoc.sistemaeps.entity.ElementoProyecto;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Institucion;
import gt.edu.cunoc.sistemaeps.entity.Persona;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import gt.edu.cunoc.sistemaeps.repository.ProyectoRepository;
import gt.edu.cunoc.sistemaeps.service.ActaService;
import gt.edu.cunoc.sistemaeps.service.BitacoraService;
import gt.edu.cunoc.sistemaeps.service.CarreraService;
import gt.edu.cunoc.sistemaeps.service.ComentarioService;
import gt.edu.cunoc.sistemaeps.service.ConvocatoriaService;
import gt.edu.cunoc.sistemaeps.service.ElementoService;
import gt.edu.cunoc.sistemaeps.service.EtapaService;
import gt.edu.cunoc.sistemaeps.service.InstitucionService;
import gt.edu.cunoc.sistemaeps.service.NotificacionService;
import gt.edu.cunoc.sistemaeps.service.PdfGeneratorService;
import gt.edu.cunoc.sistemaeps.service.PersonaService;
import gt.edu.cunoc.sistemaeps.service.ProyectoService;
import gt.edu.cunoc.sistemaeps.service.RolService;
import gt.edu.cunoc.sistemaeps.service.StorageService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.specification.ProyectoFilter;
import gt.edu.cunoc.sistemaeps.specification.ProyectoSpecification;
import gt.edu.cunoc.sistemaeps.util.ElementoUtils;
import gt.edu.cunoc.sistemaeps.util.EtapaUtils;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import java.util.List;
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
public class ProyectoServiceImp implements ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final InstitucionService institucionService;
    private final PersonaService personaService;
    private final UsuarioService usuarioService;
    private final CarreraService carreraService;
    private final EtapaService etapaService;
    private final UsuarioProyectoService usuarioProyectoService;
    private final ComentarioService comentarioService;
    private final RolService rolService;
    private final ElementoService elementoService;
    private final NotificacionService notificacionService;
    private final ConvocatoriaService convocatoriaService;
    private final ActaService actaService;
    private final BitacoraService bitacoraService;

    public ProyectoServiceImp(ProyectoRepository proyectoRepository, InstitucionService institucionService,
            PersonaService personaService, UsuarioService usuarioService,
            CarreraService carreraService, EtapaService etapaService,
            UsuarioProyectoService usuarioProyectoService,
            ComentarioService comentarioService, RolService rolService,
            ElementoService elementoService, ConvocatoriaService convocatoriaService,
            PdfGeneratorService pdfGeneratorService, StorageService storageService,
            NotificacionService notificacionService, ActaService actaService,
            BitacoraService bitacoraService) {
        this.proyectoRepository = proyectoRepository;
        this.institucionService = institucionService;
        this.personaService = personaService;
        this.usuarioService = usuarioService;
        this.carreraService = carreraService;
        this.etapaService = etapaService;
        this.usuarioProyectoService = usuarioProyectoService;
        this.comentarioService = comentarioService;
        this.rolService = rolService;
        this.elementoService = elementoService;
        this.notificacionService = notificacionService;
        this.convocatoriaService = convocatoriaService;
        this.actaService = actaService;
        this.bitacoraService = bitacoraService;
    }

    @Override
    public Proyecto getProyecto(Integer idProyecto) {
        return this.proyectoRepository.findById(idProyecto).get();
    }

    public Proyecto saveProyecto(Proyecto proyecto) throws Exception {
        return this.proyectoRepository.save(proyecto);
    }

    @Override
    public List<Proyecto> getProyectosActivos() throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        return this.proyectoRepository.findProyectoActivos(usuario.getIdUsuario(), Boolean.TRUE);
    }

    @Override
    public Page<Proyecto> getProyectos(String nombre, String registroAcademico, Pageable pageable) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Rol rolUsuario = this.rolService.getLoggedUsuarioRol();
        ProyectoFilter filter = new ProyectoFilter();
        filter.setNombreEstudiante(nombre);
        filter.setRegistroEstudiante(registroAcademico);
        if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_ESTUDIANTE)) {
            filter.setRegistroEstudiante(usuario.getRegistroAcademico());
            Specification<Proyecto> spec = ProyectoSpecification.filterBy(filter);
            return proyectoRepository.findAll(spec, pageable);
        } else if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_ASESOR)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_CONTRAPARTE)) {
            filter.setIdUsuarioAsignado(usuario.getIdUsuario());
            Specification<Proyecto> spec = ProyectoSpecification.filterBy(filter);
            return proyectoRepository.findAll(spec, pageable);
        } else if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_COORDINADOR_EPS)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_SECRETARIA)) {
            Specification<Proyecto> spec = ProyectoSpecification.filterBy(filter);
            return proyectoRepository.findAll(spec, pageable);
        } else if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_SUPERVISOR)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_COORDINADOR_CARRERA)) {
            if (usuario.getRolUsuarioList().size() > 1
                    && usuario.getRolUsuarioList().get(1).getIdRolFk().equals(this.rolService.getRol(RolUtils.ID_ROL_COORDINADOR_EPS))) {
                Specification<Proyecto> spec = ProyectoSpecification.filterBy(filter);
                return proyectoRepository.findAll(spec, pageable);
            }
            Carrera carrera = this.carreraService.getCarrerasUsuario(usuario.getIdUsuario()).get(0).getIdCarreraFk();
            return proyectoRepository.findProyectos(nombre, registroAcademico, usuario.getIdUsuario(), carrera.getIdCarrera(), Boolean.TRUE, pageable);
        } else {
            throw new Exception("Sin permisos para ver proyecto");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Proyecto finalizarProyecto(Integer idProyecto,
            ComentarioDto comentario) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        if (!supervisor.equals(usuario)) {
            throw new Exception("Usuario no tiene permisos para finalizar el proyecto");
        }
        this.comentarioService.crearComentario(idProyecto, comentario);
        proyecto.setActivo(Boolean.FALSE);
        this.notificacionService.notificarFinalizacionProyecto(proyecto.getIdUsuarioFk(), proyecto, comentario);
        return this.proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto actualizarProyecto(Integer idProyecto, ProyectoDto proyectoDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("Usuario no tiene permisos para actualizar proyecto");
        }
        proyecto.setSemestre(proyectoDto.getSemestre());
        return this.proyectoRepository.save(proyecto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Proyecto crearProyecto(ProyectoDto proyectoDto) throws Exception {
        if (!rolService.getLoggedUsuarioRol().getIdRol().equals(RolUtils.ID_ROL_ESTUDIANTE)) {
            throw new Exception("Usuario no tiene permisos para crear proyecto");
        }
        Institucion institucion = this.institucionService.crearInstitucion(proyectoDto.getInstitucion());
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Carrera carrera = this.carreraService.getCarrera(proyectoDto.getCarrera().getIdCarrera());
        Proyecto proyecto = new Proyecto();
        proyecto.setSemestre(proyectoDto.getSemestre());
        proyecto.setIdInstitucionFk(institucion);
        proyecto.setIdUsuarioFk(usuario);
        proyecto.setIdCarreraFk(carrera);
        proyecto = saveProyecto(proyecto);
        this.personaService.crearPersona(proyectoDto.getAsesor(), proyecto, RolUtils.ROL_ASESOR);
        this.personaService.crearPersona(proyectoDto.getContraparte(), proyecto, RolUtils.ROL_CONTRAPARTE);
        EtapaProyecto etapaActiva = this.etapaService.crearEtapaProyecto(EtapaUtils.ID_ETAPA_CREACION_PROYECTO, proyecto);
        this.etapaService.activarModoEdicion(etapaActiva);
        return proyecto;
    }

    private EtapaProyecto crearEtapaProyecto(EtapaProyecto etapaProyectoActiva,
            Proyecto proyecto, Integer idEtapaNueva) throws Exception {
        this.etapaService.finalizarEtapaProyecto(etapaProyectoActiva);
        EtapaProyecto etapaProyectoNueva = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(), idEtapaNueva);
        if (etapaProyectoNueva != null) {
            etapaProyectoNueva = etapaService.activarEtapaProyecto(etapaProyectoNueva);
        } else {
            etapaProyectoNueva = this.etapaService.crearEtapaProyecto(idEtapaNueva, proyecto);
        }
        if (etapaProyectoNueva.getIdEtapaFk() != null) {
            if (etapaProyectoNueva.getIdEtapaFk().getIdRolFk() != null && !etapaProyectoNueva.getIdEtapaFk().getIdRolFk().getIdRol().equals(RolUtils.ID_ROL_ESTUDIANTE)) {
                if (etapaProyectoNueva.getIdEtapaFk().getIdRolFk().getIdRol().equals(RolUtils.ID_ROL_SUPERVISOR)) {
                    Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
                    this.notificacionService.notificarEtapaNuevo(supervisor, etapaProyectoNueva);
                }
                if (etapaProyectoNueva.getIdEtapaFk().getIdRolFk().getIdRol().equals(RolUtils.ID_ROL_COORDINADOR_EPS)) {
                    Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
                    this.notificacionService.notificarEtapaNuevo(coordinadorEps, etapaProyectoNueva);
                }
                if (etapaProyectoNueva.getIdEtapaFk().getIdRolFk().getIdRol().equals(RolUtils.ID_ROL_SECRETARIA)) {
                    Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
                    this.notificacionService.notificarEtapaNuevo(secretaria, etapaProyectoNueva);
                }
            }
            this.notificacionService.notificarEtapaNuevo(proyecto.getIdUsuarioFk(), etapaProyectoNueva);
        }
        return etapaProyectoNueva;
    }

    private UsuarioProyecto asignarSecretaria(Proyecto proyecto) throws Exception {
        Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_SECRETARIA);
        return this.usuarioProyectoService.crearUsuarioProyecto(secretaria, proyecto, rol);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void solicitarRevision(Integer idProyecto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        Proyecto proyecto = getProyecto(idProyecto);
        if (!usuario.equals(proyecto.getIdUsuarioFk())) {
            throw new Exception("No tiene permiso para solicitar revision");
        }
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_CREACION_PROYECTO -> {
                crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_REVISION_SECRETARIA);
                Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
                this.notificacionService.notificarSolicitudRevision(secretaria, proyecto);
            }
            case EtapaUtils.ID_ETAPA_REVISION_SECRETARIA -> {
                this.etapaService.activarModoRevision(etapaProyectoActiva);
                Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
                this.notificacionService.notificarSolicitudRevision(secretaria, proyecto);
            }
            case EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR, EtapaUtils.ID_ETAPA_REVISION_INFORME_FINAL -> {
                this.etapaService.activarModoRevision(etapaProyectoActiva);
                Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
                this.notificacionService.notificarSolicitudRevision(supervisor, proyecto);
            }
            case EtapaUtils.ID_ETAPA_HABILITACION_BITACORA ->
                this.etapaService.activarModoRevision(etapaProyectoActiva);
            default ->
                throw new Exception("No se puede solictar revision");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comentario solicitarCambios(Integer idProyecto, ComentarioDto comentarioDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = this.getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_REVISION_SECRETARIA -> {
                Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
                if (!secretaria.equals(usuario)) {
                    throw new Exception("No tiene permisos para solicitar cambios");
                }
                this.notificacionService.notificarSolicitudCambios(usuario, proyecto, comentarioDto);
                return solicitarCambios(etapaProyectoActiva, comentarioDto, usuario, this.rolService.getRol(RolUtils.ID_ROL_SECRETARIA));
            }
            case EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR, EtapaUtils.ID_ETAPA_REVISION_INFORME_FINAL -> {
                Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
                if (!supervisor.equals(usuario)) {
                    throw new Exception("No tiene permisos para solicitar cambios");
                }
                this.notificacionService.notificarSolicitudCambios(usuario, proyecto, comentarioDto);
                return solicitarCambios(etapaProyectoActiva, comentarioDto, usuario, this.rolService.getRol(RolUtils.ID_ROL_SUPERVISOR));
            }
            default ->
                throw new Exception("No se puede solicitar cambios en esta etapa");
        }
    }

    private Comentario solicitarCambios(EtapaProyecto etapaProyectoActiva,
            ComentarioDto comentarioDto, Usuario usuario, Rol rol) throws Exception {
        this.etapaService.activarModoEdicion(etapaProyectoActiva);
        return this.comentarioService.crearComentario(comentarioDto, etapaProyectoActiva, usuario, rol);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void aprobarProyectoSecretaria(Integer idProyecto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        Proyecto proyecto = this.getProyecto(idProyecto);
        if (!secretaria.equals(usuario)) {
            throw new Exception("No tiene permisos para aprobar proyecto");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_REVISION_SECRETARIA)) {
            throw new Exception("No se puede aprobar proyecto en esta etapa del proyecto");
        }
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR);
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        this.notificacionService.notificarSolicitudRevision(supervisor, proyecto);
    }

    private UsuarioProyecto asignarSupervisor(Proyecto proyecto) throws Exception {
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_SUPERVISOR);
        return this.usuarioProyectoService.crearUsuarioProyecto(supervisor, proyecto, rol);
    }

    /*private UsuarioProyecto asignarCoordinadorCarrera(Proyecto proyecto) throws Exception {
        Usuario coordinadorCarrera = this.usuarioProyectoService.getCoordinadorCarreraDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_COORDINADOR_CARRERA);
        return this.usuarioProyectoService.crearUsuarioProyecto(coordinadorCarrera, proyecto, rol);
    }*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void aprobarProyectoSupervisor(Integer idProyecto, UsuarioDto asesorDto) throws Exception {
        Proyecto proyecto = this.getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!supervisor.equals(usuario)) {
            throw new Exception("No tiene permisos para aprobar proyecto");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR)) {
            throw new Exception("No se puede aprobar proyecto en esta etapa del proyecto");
        }
        asignarAsesor(proyecto, asesorDto);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CONVOCATORIA_ANTEPROYECTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void aprobarInformeFinalSupervisor(Integer idProyecto) throws Exception {
        Proyecto proyecto = this.getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!supervisor.equals(usuario)) {
            throw new Exception("No tiene permisos para aprobar informe final");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_REVISION_INFORME_FINAL)) {
            throw new Exception("No se puede aprobar proyecto en esta etapa del proyecto");
        }
        EtapaProyecto etapa = crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_DICTAMEN_REVISION);
        etapa.setEditable(Boolean.TRUE);
        this.etapaService.saveEtapaProyecto(etapa);
    }

    private UsuarioProyecto asignarAsesor(Proyecto proyecto, UsuarioDto asesorDto) throws Exception {
        Usuario asesor = this.usuarioService.getUsuario(asesorDto.getIdUsuario());
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_ASESOR);
        this.notificacionService.notificarAsignacionUsuarioProyecto(asesor, rol, proyecto);
        return this.usuarioProyectoService.crearUsuarioProyecto(asesor, proyecto, rol);
    }

    private UsuarioProyecto asignarContraparte(Proyecto proyecto, UsuarioDto contraparteDto) throws Exception {
        Usuario contraparte = this.usuarioService.getUsuario(contraparteDto.getIdUsuario());
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_CONTRAPARTE);
        this.notificacionService.notificarAsignacionUsuarioProyecto(contraparte, rol, proyecto);
        return this.usuarioProyectoService.crearUsuarioProyecto(contraparte, proyecto, rol);
    }

    @Override
    public Convocatoria getConvocatoriaAnteproyecto(Integer idProyecto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.convocatoriaService.getConvocatoriaAnteproyecto(proyecto);
    }

    @Override
    public Convocatoria getConvocatoriaExamenGeneral(Integer idProyecto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.convocatoriaService.getConvocatoriaExamenGeneral(proyecto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void crearConvocatoriaAnteproyecto(Integer idProyecto, ConvocatoriaDto convocatoriaDto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!supervisor.equals(usuario)) {
            throw new Exception("No tiene permisos para definir fecha de evaluacion");
        }
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_CONVOCATORIA_ANTEPROYECTO -> {
                this.convocatoriaService.generarConvocatoriaAnteproyecto(proyecto, convocatoriaDto);
                crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO);
            }
            case EtapaUtils.ID_ETAPA_CONVOCATORIA_EXAMEN_GENERAL -> {
                this.convocatoriaService.generarConvocatoriaExamenGeneral(proyecto, convocatoriaDto);
                crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_EXAMEN_GENERAL);
            }
            default ->
                throw new Exception("No se puede definir fecha de evaluacion en esta etapa del proyecto");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void actualizarConvocatoriaAnteproyecto(Integer idProyecto, ConvocatoriaDto convocatoriaDto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!supervisor.equals(usuario)) {
            throw new Exception("No tiene permisos para definir fecha de evaluacion");
        }
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO, EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO -> {
                EtapaProyecto etapaProyectoCargar = this.etapaService.getEtapaProyecto(idProyecto, EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO);
                this.convocatoriaService.actualizarConvocatoriaAnteproyecto(proyecto, convocatoriaDto);
                this.etapaService.desactivarEtapaProyecto(etapaProyectoActiva);
                this.etapaService.activarEtapaProyecto(etapaProyectoCargar);
                crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO);
            }
            case EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_EXAMEN_GENERAL, EtapaUtils.ID_ETAPA_EXAMEN_GENERAL -> {
                EtapaProyecto etapaProyectoCargar = this.etapaService.getEtapaProyecto(idProyecto, EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_EXAMEN_GENERAL);
                this.convocatoriaService.actualizarConvocatoriaExamenGeneral(proyecto, convocatoriaDto);
                this.etapaService.desactivarEtapaProyecto(etapaProyectoActiva);
                this.etapaService.activarEtapaProyecto(etapaProyectoCargar);
            }
            default ->
                throw new Exception("No se puede modificar fecha evaluacion del anteproyecto");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cargarConvocatoria(Integer idProyecto, MultipartFile convocatoria) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!coordinadorEps.equals(usuario)) {
            throw new Exception("No tiene permisos para definir fecha de evaluacion");
        }
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO -> {
                this.convocatoriaService.cargarConvocatoria(proyecto, convocatoria);
                crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO);
                /*
                EtapaProyecto etapa = 
                etapa.setEditable(Boolean.TRUE);
                this.etapaService.saveEtapaProyecto(etapa);
                 */
            }
            case EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_EXAMEN_GENERAL -> {
                this.convocatoriaService.cargarConvocatoriaExamenGeneral(proyecto, convocatoria);
                crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_EXAMEN_GENERAL);
            }
            default ->
                throw new Exception("No se puede cargar convocatoria de evaluacion del anteproyecto en esta etapa.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cargarCartaAceptacionContraparte(Integer idProyecto, MultipartFile cartaAceptacion,
            MultipartFile oficioContraparte) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para cargar la carta");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_CARGA_CARTA_ACEPTACION_CONTRAPARTE)) {
            throw new Exception("No se puede cargar carta en esta etapa del proyecto.");
        }
        cargarElementoProyecto(proyecto, cartaAceptacion, ElementoUtils.ID_ELEMENTO_CARTA_ACEPTACION_CONTRAPARTE, EtapaUtils.ID_ETAPA_CARGA_CARTA_ACEPTACION_CONTRAPARTE);
        cargarElementoProyecto(proyecto, oficioContraparte, ElementoUtils.ID_ELEMENTO_OFICIO_CONTRAPARTE, EtapaUtils.ID_ETAPA_CARGA_CARTA_ACEPTACION_CONTRAPARTE);
        EtapaProyecto etapa = crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_HABILITACION_BITACORA);
        etapa.setEditable(Boolean.TRUE);
        this.etapaService.saveEtapaProyecto(etapa);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Acta crearActa(Integer idProyecto, ActaDto actaDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO -> {
                Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
                if (!supervisor.equals(usuario)) {
                    throw new Exception("No tiene permisos para crear acta");
                }
                EtapaProyecto etapa = crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CARGA_CARTA_ACEPTACION_CONTRAPARTE);
                this.etapaService.saveEtapaProyecto(etapa);
                if (actaDto.getResultado().equals("RECHAZADO")) {
                    proyecto.setActivo(Boolean.FALSE);
                    this.proyectoRepository.save(proyecto);
                } else {
                    etapa.setEditable(Boolean.TRUE);
                }
                if (actaDto.getComentario() != null && !actaDto.getComentario().isBlank()) {
                    this.comentarioService.crearComentario(idProyecto, new ComentarioDto(actaDto.getComentario()));
                }
                return this.actaService.crearActaAnteproyecto(proyecto, actaDto);
            }
            case EtapaUtils.ID_ETAPA_EXAMEN_GENERAL -> {
                Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
                if (!supervisor.equals(usuario)) {
                    throw new Exception("No tiene permisos para crear acta");
                }
                EtapaProyecto etapa = crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_REDACCION_ARTICULO);
                if (actaDto.getResultado().equals("RECHAZADO")) {
                    proyecto.setActivo(Boolean.FALSE);
                    this.proyectoRepository.save(proyecto);
                } else {
                    etapa.setEditable(Boolean.TRUE);
                }
                if (actaDto.getComentario() != null && !actaDto.getComentario().isBlank()) {
                    this.comentarioService.crearComentario(idProyecto, new ComentarioDto(actaDto.getComentario()));
                }
                this.etapaService.saveEtapaProyecto(etapa);
                return this.actaService.crearActaExamenGeneral(proyecto, actaDto);
            }
            case EtapaUtils.ID_ETAPA_ACTA_FINALIZACION -> {
                Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
                if (!coordinadorEps.equals(usuario)) {
                    throw new Exception("No tiene permisos para crear acta");
                }
                crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_FINALIZADO);
                proyecto.setActivo(Boolean.FALSE);
                this.proyectoRepository.save(proyecto);
                return this.actaService.crearActaFinalizacion(proyecto, actaDto);
            }
            default ->
                throw new Exception("No se puede crear acta en esta etapa del proyecto.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Acta generarActaAnteproyecto(Integer idProyecto, ActaDto actaDto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.actaService.generarActaAnterproyecto(proyecto, actaDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Acta generarActaExamenGeneral(Integer idProyecto, ActaDto actaDto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.actaService.generarActaExamenGeneral(proyecto, actaDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Acta generarActaAprobacion(Integer idProyecto, ActaDto actaDto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.actaService.generarActaAprobacion(proyecto, actaDto);
    }

    @Override
    public Acta getActaAnteproyecto(Integer idProyecto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.actaService.getActaAnteproyecto(proyecto);
    }

    @Override
    public Acta getActaExamenGeneral(Integer idProyecto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.actaService.getActaExamenGeneral(proyecto);
    }

    @Override
    public Acta getActaAprobacion(Integer idProyecto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.actaService.getActaAprobacion(proyecto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void habilitarBitacora(Integer idProyecto, UsuarioDto asesorDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        if (!supervisor.equals(usuario)) {
            throw new Exception("No tiene permisos para habilitar bitacora");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_HABILITACION_BITACORA)) {
            throw new Exception("No se puede habilitar bitacora en esta etapa de proyecto");
        }
        asignarContraparte(proyecto, asesorDto);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_BITACORA);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Bitacora crearBitacora(Integer idProyecto, BitacoraDto bitacoraDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para agregar registro de bitacora");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_BITACORA)) {
            throw new Exception("No se puede agregar registros a bitacora en esta etapa de proyecto");
        }
        return this.bitacoraService.crearBitacora(proyecto, bitacoraDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Persona agregarAsesorTecnico(Integer idProyecto, UsuarioDto usuarioDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para agregar registro de bitacora");
        }
        return this.personaService.crearPersona(usuarioDto, proyecto, RolUtils.ROL_ASESOR_TECNICO);
    }

    /*@Override
    public Usuario actualizarSupervisor(Integer idProyecto, UsuarioDto usuarioDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
        Usuario supervisor = this.usuarioService.getUsuario(usuarioDto.getIdUsuario());
        Proyecto proyecto = getProyecto(idProyecto);
        if (!coordinadorEps.equals(usuario)) {
            throw new Exception("No tiene permisos para cambiar supervisor");
        }
        UsuarioProyecto supervisorProyecto = this.usuarioProyectoService.actualizarSupervisorProyecto(proyecto, supervisor);
        return supervisorProyecto.getIdUsuarioFk();
    }*/
    @Override
    public Usuario actualizarAsesor(Integer idProyecto, UsuarioDto usuarioDto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_ASESOR);
        //Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Usuario asesor = this.usuarioService.getUsuario(usuarioDto.getIdUsuario());
        if (!supervisor.equals(usuario)) {
            throw new Exception("No tiene permisos para cambiar asesor");
        }
        UsuarioProyecto asesorProyecto = this.usuarioProyectoService.actualizarAsesorProyecto(proyecto, asesor);
        this.notificacionService.notificarAsignacionUsuarioProyecto(asesor, rol, proyecto);
        return asesorProyecto.getIdUsuarioFk();
    }

    @Override
    public Usuario actualizarContraparte(Integer idProyecto, UsuarioDto usuarioDto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_CONTRAPARTE);
        //Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Usuario contraparte = this.usuarioService.getUsuario(usuarioDto.getIdUsuario());
        if (!supervisor.equals(usuario)) {
            throw new Exception("No tiene permisos para cambiar contraparte");
        }
        UsuarioProyecto contraparteProyecto = this.usuarioProyectoService.actualizarContraparteProyecto(proyecto, contraparte);
        this.notificacionService.notificarAsignacionUsuarioProyecto(contraparte, rol, proyecto);
        return contraparteProyecto.getIdUsuarioFk();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finalizarBitacora(Integer idProyecto,
            MultipartFile finiquitoContraparte) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("Sin permisos para realizar esta accion");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_BITACORA)) {
            throw new Exception("No se puede realizar esta accion en esta etapa del proyecto");
        }
        this.bitacoraService.finalizarBitacora(proyecto, finiquitoContraparte);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_APROBACION_BITACORA);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void aprobarBitacora(Integer idProyecto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!supervisor.equals(usuario)) {
            throw new Exception("Sin permisos para realizar esta accion");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_APROBACION_BITACORA)) {
            throw new Exception("No se puede realizar esta accion en esta etapa del proyecto");
        }
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CARGA_INFORME_FINAL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechazarBitacora(Integer idProyecto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!supervisor.equals(usuario)) {
            throw new Exception("Sin permisos para realizar esta accion");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_APROBACION_BITACORA)) {
            throw new Exception("No se puede realizar esta accion en esta etapa del proyecto");
        }
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_BITACORA);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cargarInformeFinal(Integer idProyecto, MultipartFile cartaAsesor,
            MultipartFile informeFinal) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("Sin permisos para realizar esta accion");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_CARGA_INFORME_FINAL)) {
            throw new Exception("No se puede realizar esta accion en esta etapa del proyecto");
        }
        cargarElementoProyecto(proyecto, cartaAsesor, ElementoUtils.ID_ELEMENTO_CARTA_FINALIZACION_ASESOR, EtapaUtils.ID_ETAPA_CARGA_INFORME_FINAL);
        cargarElementoProyecto(proyecto, informeFinal, ElementoUtils.ID_ELEMENTO_INFORME_FINAL, EtapaUtils.ID_ETAPA_CARGA_INFORME_FINAL);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CONVOCATORIA_EXAMEN_GENERAL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cargarArticulo(Integer idProyecto, MultipartFile articulo,
            MultipartFile traduccionArticulo, MultipartFile constanciaLinguistica) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("Sin permisos para realizar esta accion");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_REDACCION_ARTICULO)) {
            throw new Exception("No se puede realizar esta accion en esta etapa del proyecto");
        }
        cargarElementoProyecto(proyecto, articulo, ElementoUtils.ID_ELEMENTO_ARTICULO, EtapaUtils.ID_ETAPA_REDACCION_ARTICULO);
        cargarElementoProyecto(proyecto, traduccionArticulo, ElementoUtils.ID_ELEMENTO_TRADUCCION_ARTICULO, EtapaUtils.ID_ETAPA_REDACCION_ARTICULO);
        cargarElementoProyecto(proyecto, constanciaLinguistica, ElementoUtils.ID_ELEMENTO_CONSTANCIA_LINGUISTICA, EtapaUtils.ID_ETAPA_REDACCION_ARTICULO);
        EtapaProyecto etapa = crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_REVISION_INFORME_FINAL);
        etapa.setEditable(Boolean.TRUE);
        this.etapaService.saveEtapaProyecto(etapa);
    }

    private ElementoProyecto cargarElementoProyecto(Proyecto proyecto, MultipartFile file, Integer idElemento, Integer idEtapa) throws Exception {
        Elemento elemento = this.elementoService.getElemento(idElemento);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(), idEtapa);
        ElementoProyecto elementoProyecto = this.elementoService.crearElementoProyecto(proyecto, elemento,
                etapaProyecto, file);
        return elementoProyecto;
    }

    /* @Override
    @Transactional(rollbackFor = Exception.class)
    public void cargarConstanciaLinguistica(Integer idProyecto, MultipartFile constanciaLinguistica) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("Sin permisos para realizar esta accion");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_REVISION_LINGUISTICA)) {
            System.out.println("etapa db: " + etapaProyectoActiva.getIdEtapaFk().getIdEtapa());
            System.out.println("etapa cons: " + EtapaUtils.ID_ETAPA_DICTAMEN_REVISION);

            throw new Exception("No se puede realizar esta accion en esta etapa del proyecto");
        }
        cargarElementoProyecto(proyecto, constanciaLinguistica, ElementoUtils.ID_ELEMENTO_CONSTANCIA_LINGUISTICA, EtapaUtils.ID_ETAPA_REVISION_LINGUISTICA);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_DICTAMEN_REVISION);
    }*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cargarDictamenRevsion(Integer idProyecto, MultipartFile dictamenRevision,
            MultipartFile cartaRevision) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!supervisor.equals(usuario)) {
            throw new Exception("Sin permisos para realizar esta accion");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_DICTAMEN_REVISION)) {
            throw new Exception("No se puede realizar esta accion en esta etapa del proyecto");
        }
        cargarElementoProyecto(proyecto, dictamenRevision, ElementoUtils.ID_ELEMENTO_DICTAMEN_REVISION, EtapaUtils.ID_ETAPA_DICTAMEN_REVISION);
        cargarElementoProyecto(proyecto, cartaRevision, ElementoUtils.ID_ELEMENTO_CARTA_REVISION, EtapaUtils.ID_ETAPA_DICTAMEN_REVISION);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_ACTA_FINALIZACION);
    }
}
