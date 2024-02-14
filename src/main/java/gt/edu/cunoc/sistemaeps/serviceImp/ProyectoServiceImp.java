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
import gt.edu.cunoc.sistemaeps.service.EmailService;
import gt.edu.cunoc.sistemaeps.service.EtapaService;
import gt.edu.cunoc.sistemaeps.service.InstitucionService;
import gt.edu.cunoc.sistemaeps.service.PdfGeneratorService;
import gt.edu.cunoc.sistemaeps.service.PersonaService;
import gt.edu.cunoc.sistemaeps.service.ProyectoService;
import gt.edu.cunoc.sistemaeps.service.RolService;
import gt.edu.cunoc.sistemaeps.service.StorageService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.specification.ProyectoFilter;
import gt.edu.cunoc.sistemaeps.specification.ProyectoSpecification;
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

    //Elementos de proyecto
    private final int ID_ELEMENTO_TITULO = 1;
    private final int ID_ELEMENTO_CONVOCATORIA_ANTEPROYECTO_FIRMADA = 9;
    private final int ID_ELEMENTO_CARTA_ACEPTACION_CONTRAPARTE = 11;

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
    private final PdfGeneratorService pdfGeneratorService;
    private final StorageService storageService;
    private final EmailService emailService;
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
            EmailService emailService, ActaService actaService,
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
        this.pdfGeneratorService = pdfGeneratorService;
        this.storageService = storageService;
        this.emailService = emailService;
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
    public Page<Proyecto> getProyectos(Pageable pageable) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Rol rolUsuario = this.rolService.getLoggedUsuarioRol();
        ProyectoFilter filter = new ProyectoFilter();
        if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_ESTUDIANTE)) {
            filter.setRegistroEstudiante(usuario.getRegistroAcademico());
            Specification<Proyecto> spec = ProyectoSpecification.filterBy(filter);
            return proyectoRepository.findAll(spec, pageable);
        } else if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_SECRETARIA)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_SUPERVISOR)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_ASESOR)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_COORDINADOR_CARRERA)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_CONTRAPARTE)) {
            filter.setIdUsuarioAsignado(usuario.getIdUsuario());
            Specification<Proyecto> spec = ProyectoSpecification.filterBy(filter);
            return proyectoRepository.findAll(spec, pageable);
        } else if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_COORDINADOR_EPS)) {
            Specification<Proyecto> spec = ProyectoSpecification.filterBy(filter);
            return proyectoRepository.findAll(spec, pageable);
        } else {
            throw new Exception("Sin permisos para ver proyecto");
        }
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
            return etapaService.activarEtapaProyecto(etapaProyectoNueva);
        } else {
            return this.etapaService.crearEtapaProyecto(idEtapaNueva, proyecto);
        }
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
                asignarSecretaria(proyecto);
            }
            case EtapaUtils.ID_ETAPA_REVISION_SECRETARIA ->
                this.etapaService.activarModoRevision(etapaProyectoActiva);
            case EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR ->
                this.etapaService.activarModoRevision(etapaProyectoActiva);
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
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_REVISION_SECRETARIA -> {
                UsuarioProyecto secretariaProyecto = this.usuarioProyectoService.getSecretariaProyecto(idProyecto);
                if (!usuario.equals(secretariaProyecto.getIdUsuarioFk())) {
                    throw new Exception("No tiene permisos para solicitar cambios");
                }
                return solicitarCambios(etapaProyectoActiva, comentarioDto, usuario, secretariaProyecto.getIdRolFk());
            }
            case EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR -> {
                UsuarioProyecto supervisorProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
                if (!usuario.equals(supervisorProyecto.getIdUsuarioFk())) {
                    throw new Exception("No tiene permisos para solicitar cambios");
                }
                return solicitarCambios(etapaProyectoActiva, comentarioDto, usuario, supervisorProyecto.getIdRolFk());
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
        UsuarioProyecto secretariaProyecto = this.usuarioProyectoService.getSecretariaProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        Proyecto proyecto = this.getProyecto(idProyecto);
        if (!secretariaProyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para aprobar proyecto");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_REVISION_SECRETARIA)) {
            throw new Exception("No se puede aprobar proyecto en esta etapa del proyecto");
        }
        asignarSupervisor(proyecto);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR);
    }

    private UsuarioProyecto asignarSupervisor(Proyecto proyecto) throws Exception {
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_SUPERVISOR);
        return this.usuarioProyectoService.crearUsuarioProyecto(supervisor, proyecto, rol);
    }

    private UsuarioProyecto asignarCoordinadorCarrera(Proyecto proyecto) throws Exception {
        Usuario coordinadorCarrera = this.usuarioProyectoService.getCoordinadorCarreraDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_COORDINADOR_CARRERA);
        return this.usuarioProyectoService.crearUsuarioProyecto(coordinadorCarrera, proyecto, rol);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void aprobarProyectoSupervisor(Integer idProyecto, UsuarioDto asesorDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        UsuarioProyecto supervisorProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        Proyecto proyecto = this.getProyecto(idProyecto);
        if (!supervisorProyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para aprobar proyecto");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR)) {
            throw new Exception("No se puede aprobar proyecto en esta etapa del proyecto");
        }
        asignarCoordinadorCarrera(proyecto);
        asignarAsesor(proyecto, asesorDto);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CONVOCATORIA_ANTEPROYECTO);
    }

    private UsuarioProyecto asignarAsesor(Proyecto proyecto, UsuarioDto asesorDto) throws Exception {
        Usuario asesor = this.usuarioService.getUsuario(asesorDto.getIdUsuario());
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_ASESOR);
        return this.usuarioProyectoService.crearUsuarioProyecto(asesor, proyecto, rol);
    }

    private UsuarioProyecto asignarContraparte(Proyecto proyecto, UsuarioDto asesorDto) throws Exception {
        Usuario asesor = this.usuarioService.getUsuario(asesorDto.getIdUsuario());
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_CONTRAPARTE);
        return this.usuarioProyectoService.crearUsuarioProyecto(asesor, proyecto, rol);
    }

    @Override
    public Convocatoria getConvocatoriaAnteproyecto(Integer idProyecto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.convocatoriaService.getConvocatoriaAnteproyecto(proyecto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void crearConvocatoriaAnteproyecto(Integer idProyecto, ConvocatoriaDto convocatoriaDto) throws Exception {
        System.out.println("crear");
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        UsuarioProyecto supervisroProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!supervisroProyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para definir fecha de evaluacion");
        }
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_CONVOCATORIA_ANTEPROYECTO -> {
                this.convocatoriaService.generarConvocatoriaAnteproyecto(proyecto, convocatoriaDto);
                crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO);
            }
            case EtapaUtils.ID_ETAPA_CONVOCATORIA_EXAMEN_GENERAL ->{
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
        System.out.println("actualizar");
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        UsuarioProyecto supervisorProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!supervisorProyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para definir fecha de evaluacion");
        }
        System.out.println("etapa: " + etapaProyectoActiva.getIdEtapaFk().getIdEtapa());
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO, EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO -> {
                System.out.println("anteproyecto");
                EtapaProyecto etapaProyectoCargar = this.etapaService.getEtapaProyecto(idProyecto, EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO);
                this.convocatoriaService.actualizarConvocatoriaAnteproyecto(proyecto, convocatoriaDto);
                this.etapaService.desactivarEtapaProyecto(etapaProyectoActiva);
                this.etapaService.activarEtapaProyecto(etapaProyectoCargar);
                crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO);
            }
            case EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_EXAMEN_GENERAL, EtapaUtils.ID_ETAPA_EXAMEN_GENERAL -> {
                System.out.println("examen general");
                EtapaProyecto etapaProyectoCargar = this.etapaService.getEtapaProyecto(idProyecto, EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_EXAMEN_GENERAL);
                this.convocatoriaService.actualizarConvocatoriaExamenGeneral(proyecto, convocatoriaDto);
                this.etapaService.desactivarEtapaProyecto(etapaProyectoActiva);
                System.out.println("etapa: " + etapaProyectoCargar.getIdEtapaFk().getIdEtapa());
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
    public void cargarCartaAceptacionContraparte(Integer idProyecto, MultipartFile carta) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para cargar la carta");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_CARGA_CARTA_ACEPTACION_CONTRAPARTE)) {
            throw new Exception("No se puede cargar carta en esta etapa del proyecto.");
        }
        cargarCartaAceptacionContraparte(proyecto, carta);
        EtapaProyecto etapa = crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_HABILITACION_BITACORA);
        etapa.setEditable(Boolean.TRUE);
        this.etapaService.saveEtapaProyecto(etapa);
    }

    private void cargarCartaAceptacionContraparte(Proyecto proyecto, MultipartFile file) throws Exception {
        Elemento elemento = this.elementoService.getElemento(ID_ELEMENTO_CARTA_ACEPTACION_CONTRAPARTE);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_CARGA_CARTA_ACEPTACION_CONTRAPARTE);
        ElementoProyecto elementoConvocatoria = this.elementoService.crearElementoProyecto(proyecto, elemento,
                etapaProyecto, file);

        /*
        String urlFile = this.storageService.getFile(elementoConvocatoria.getInformacion());
        this.emailService.sendNotificationEmail("edvinteodoro-gonzalezrafael@cunoc.edu.gt",
                "CONVOCATORIA FIRMADA", "convocatoria firmada", urlFile);*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Acta crearActaAnteproyecto(Integer idProyecto, ActaDto actaDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        UsuarioProyecto supervisorProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
        if (!supervisorProyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para crear acta");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO)) {
            throw new Exception("No se puede crear acta en esta etapa del proyecto.");
        }
        EtapaProyecto etapa = crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CARGA_CARTA_ACEPTACION_CONTRAPARTE);
        etapa.setEditable(Boolean.TRUE);
        this.etapaService.saveEtapaProyecto(etapa);
        return this.actaService.crearActaAnteproyecto(proyecto, actaDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Acta generarActaAnteproyecto(Integer idProyecto, ActaDto actaDto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.actaService.generarActaAnterproyecto(proyecto, actaDto);
    }

    @Override
    public Acta getActaAnteproyecto(Integer idProyecto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        return this.actaService.getActaAnteproyecto(proyecto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void habilitarBitacora(Integer idProyecto, UsuarioDto asesorDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        UsuarioProyecto supervisorProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
        if (!supervisorProyecto.getIdUsuarioFk().equals(usuario)) {
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

    @Override
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
    }

    @Override
    public Usuario actualizarAsesor(Integer idProyecto, UsuarioDto usuarioDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
        UsuarioProyecto supervisorProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
        Usuario asesor = this.usuarioService.getUsuario(usuarioDto.getIdUsuario());
        Proyecto proyecto = getProyecto(idProyecto);
        if (!coordinadorEps.equals(usuario) && !supervisorProyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para cambiar asesor");
        }
        UsuarioProyecto asesorProyecto = this.usuarioProyectoService.actualizarAsesorProyecto(proyecto, asesor);
        return asesorProyecto.getIdUsuarioFk();
    }

    @Override
    public Usuario actualizarContraparte(Integer idProyecto, UsuarioDto usuarioDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
        UsuarioProyecto supervisorProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
        Usuario contraparte = this.usuarioService.getUsuario(usuarioDto.getIdUsuario());
        Proyecto proyecto = getProyecto(idProyecto);
        if (!coordinadorEps.equals(usuario) && !supervisorProyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para cambiar contraparte");
        }
        UsuarioProyecto contraparteProyecto = this.usuarioProyectoService.actualizarContraparteProyecto(proyecto, contraparte);
        return contraparteProyecto.getIdUsuarioFk();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finalizarBitacora(Integer idProyecto, MultipartFile cartaAsesor,
            MultipartFile finiquitoContraparte, MultipartFile informeFinal) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Proyecto proyecto = getProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("Sin permisos para realizar esta accion");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_BITACORA)) {
            throw new Exception("No se puede realizar esta accion en esta etapa del proyecto");
        }
        this.bitacoraService.finalizarBitacora(proyecto, cartaAsesor, finiquitoContraparte, informeFinal);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CONVOCATORIA_EXAMEN_GENERAL);

    }
}
