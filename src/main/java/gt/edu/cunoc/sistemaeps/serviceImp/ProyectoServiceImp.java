package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.ComentarioDto;
import gt.edu.cunoc.sistemaeps.dto.EvaluacionDto;
import gt.edu.cunoc.sistemaeps.dto.ProyectoDto;
import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import gt.edu.cunoc.sistemaeps.entity.Carrera;
import gt.edu.cunoc.sistemaeps.entity.CorrelativoEstudiante;
import gt.edu.cunoc.sistemaeps.entity.Elemento;
import gt.edu.cunoc.sistemaeps.entity.ElementoProyecto;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Institucion;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import gt.edu.cunoc.sistemaeps.repository.ProyectoRepository;
import gt.edu.cunoc.sistemaeps.service.CarreraService;
import gt.edu.cunoc.sistemaeps.service.ComentarioService;
import gt.edu.cunoc.sistemaeps.service.CorrelativoService;
import gt.edu.cunoc.sistemaeps.service.ElementoService;
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
import gt.edu.cunoc.sistemaeps.util.DateUtils;
import gt.edu.cunoc.sistemaeps.util.EtapaUtils;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
    private final int ID_ELEMENTO_CONVOCATORIA_ANTEPROYECTO = 8;

    //Datos documentos generados
    private final String KEY_CARRERA = "${carrera}";
    private final String KEY_CORRELATIVO = "${correlativo}";
    private final String KEY_CUIDAD = "${cuidad}";
    private final String KEY_FECHA_ACTUAL = "${fecha_actual}";
    private final String KEY_NOMBRE_ESTUDIANTE = "${nombre_estudiante}";
    private final String KEY_REGISTRO_ACADEMICO_ESTUDIANTE = "${registro_academico_estudiante}";
    private final String KEY_CARNE_ESTUDIANTE = "${carne_estudiante}";
    private final String KEY_TITULO_PROYECTO = "${titulo_proyecto}";
    private final String KEY_TITULO_COORDINADOR_CARRERA = "${titulo_coordinador_carrera}";
    private final String KEY_NOMBRE_COORDINADOR_CARRERA = "${nombre_coordinador_carrera}";
    private final String KEY_TITULO_ASESOR = "${titulo_asesor}";
    private final String KEY_NOMBRE_ASESOR = "${nombre_asesor}";
    private final String KEY_TITULO_SUPERVISOR = "${titulo_supervisor}";
    private final String KEY_NOMBRE_SUPERVISOR = "${nombre_supervisor}";
    private final String KEY_TITULO_COORDINADOR_EPS = "${titulo_coordinador_eps}";
    private final String KEY_NOMBRE_COORDINADOR_EPS = "${nombre_coordiandor_eps}";
    private final String KEY_INFORMACION_REPRESENTANTE_COORDIANDOR = "${informacion_representante}";
    private final String KEY_DIA_EVALUACION = "${dia_evaluacion}";
    private final String KEY_FECHA_EVALUACION = "${fecha_evaluacion}";
    private final String KEY_HORA_EVALUACION = "${hora_evaluacion}";
    private final String KEY_SALON_EVALUACION = "${hora_evaluacion}";
    private final String CUIDAD = "Quetzaltenango";

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
    private final CorrelativoService correlativoService;
    private final PdfGeneratorService pdfGeneratorService;
    private final StorageService storageService;

    public ProyectoServiceImp(ProyectoRepository proyectoRepository, InstitucionService institucionService,
            PersonaService personaService, UsuarioService usuarioService,
            CarreraService carreraService, EtapaService etapaService,
            UsuarioProyectoService usuarioProyectoService,
            ComentarioService comentarioService, RolService rolService,
            ElementoService elementoService, CorrelativoService correlativoService,
            PdfGeneratorService pdfGeneratorService, StorageService storageService) {
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
        this.correlativoService = correlativoService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.storageService = storageService;
    }

    @Override
    public Proyecto getProyecto(Integer idProyecto) {
        return this.proyectoRepository.findById(idProyecto).get();
    }

    public Proyecto saveProyecto(Proyecto proyecto) throws Exception {
        return this.proyectoRepository.save(proyecto);
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
    @Transactional
    public Proyecto crearProyecto(ProyectoDto proyectoDto) throws Exception {
        if (!rolService.getLoggedUsuarioRol().getIdRol().equals(RolUtils.ID_ROL_ESTUDIANTE)) {
            throw new Exception("Usuario no tiene permisos para crear proyecto");
        }
        Institucion institucion = this.institucionService.crearInstitucion(proyectoDto.getInstitucion());
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Carrera carrera = this.carreraService.getCarrera(proyectoDto.getCarrera().getIdCarrera());
        Proyecto proyecto = new Proyecto();
        proyecto.setTipoProyecto(proyectoDto.getTipoProyecto()); 
        proyecto.setIdInstitucionFk(institucion);
        proyecto.setIdUsuarioFk(usuario);
        proyecto.setIdCarreraFk(carrera);
        proyecto = saveProyecto(proyecto);
        this.personaService.crearPersona(proyectoDto.getAsesor(), proyecto,RolUtils.ROL_ASESOR);
        this.personaService.crearPersona(proyectoDto.getContraparte(), proyecto,RolUtils.ROL_CONTRAPARTE);
        EtapaProyecto etapaActiva = this.etapaService.crearEtapaProyecto(EtapaUtils.ID_ETAPA_CREACION_PROYECTO, proyecto);
        this.etapaService.activarModoEdicion(etapaActiva);
        return proyecto;
    }

    private void crearEtapaProyecto(EtapaProyecto etapaProyectoActiva,
            Proyecto proyecto, Integer idEtapaNueva) throws Exception {
        this.etapaService.finalizarEtapaProyecto(etapaProyectoActiva);
        this.etapaService.crearEtapaProyecto(idEtapaNueva, proyecto);
    }
    
    
    private UsuarioProyecto asignarSecretaria(Proyecto proyecto) throws Exception {
        Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_SECRETARIA);
        return this.usuarioProyectoService.crearUsuarioProyecto(secretaria, proyecto, rol);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void solicitarRevision(Integer idProyecto) throws Exception {
        System.out.println("no error");
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        Proyecto proyecto = getProyecto(idProyecto);
        if (!usuario.equals(proyecto.getIdUsuarioFk())) {
            throw new Exception("No tiene permiso para solicitar revision");
        }
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_CREACION_PROYECTO -> {
                System.out.println("no error 2");
                crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_REVISION_SECRETARIA);
                System.out.println("no error 3");
                asignarSecretaria(proyecto);
                System.out.println("no error 6");
            }
            case EtapaUtils.ID_ETAPA_REVISION_SECRETARIA ->
                this.etapaService.activarModoRevision(etapaProyectoActiva);
            case EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR ->
                this.etapaService.activarModoEdicion(etapaProyectoActiva);
            default ->
                throw new Exception("No se puede solictar revision");
        }
    }

    @Override
    @Transactional
    public void solicitarCambios(Integer idProyecto, ComentarioDto comentarioDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        switch (etapaProyectoActiva.getIdEtapaFk().getIdEtapa()) {
            case EtapaUtils.ID_ETAPA_REVISION_SECRETARIA -> {
                UsuarioProyecto secretariaProyecto = this.usuarioProyectoService.getSecretariaProyecto(idProyecto);
                if (!usuario.equals(secretariaProyecto.getIdUsuarioFk())) {
                    throw new Exception("No tiene permisos para solicitar cambios");
                }
                solicitarCambios(etapaProyectoActiva, comentarioDto, usuario, secretariaProyecto.getIdRolFk());
            }
            case EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR -> {
                UsuarioProyecto supervisorProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
                if (!usuario.equals(supervisorProyecto.getIdUsuarioFk())) {
                    throw new Exception("No tiene permisos para solicitar cambios");
                }
                solicitarCambios(etapaProyectoActiva, comentarioDto, usuario, supervisorProyecto.getIdRolFk());
            }
            default ->
                throw new Exception("No se puede solicitar cambios en esta etapa");
        }
    }

    private void solicitarCambios(EtapaProyecto etapaProyectoActiva,
            ComentarioDto comentarioDto, Usuario usuario, Rol rol) throws Exception {
        this.etapaService.activarModoEdicion(etapaProyectoActiva);
        this.comentarioService.crearComentario(comentarioDto, etapaProyectoActiva, usuario, rol);
    }

    @Override
    @Transactional
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
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR);
        asignarSupervisor(proyecto);
    }

    private UsuarioProyecto asignarSupervisor(Proyecto proyecto) throws Exception {
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera()); 
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_SUPERVISOR);
        return this.usuarioProyectoService.crearUsuarioProyecto(supervisor, proyecto, rol);
    }

    @Transactional
    public void aprobarProyectoSupervisor(Integer idProyecto, UsuarioDto asesorDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        UsuarioProyecto supervisroProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        Proyecto proyecto = this.getProyecto(idProyecto);
        if (!supervisroProyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para aprobar proyecto");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_REVISION_SUPERVISOR)) {
            throw new Exception("No se puede aprobar proyecto en esta etapa del proyecto");
        }
        asignarAsesor(proyecto, asesorDto);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_PROGRAMACION_PRESENTACION_ANTEPROYECTO);
    }

    @Transactional
    private UsuarioProyecto asignarAsesor(Proyecto proyecto, UsuarioDto asesorDto) throws Exception {
        Usuario asesor = this.usuarioService.getUsuario(asesorDto.getIdUsuario());
        Rol rol = this.rolService.getRol(RolUtils.ID_ROL_ASESOR);
        return this.usuarioProyectoService.crearUsuarioProyecto(asesor, proyecto, rol);
    }

    @Transactional
    public void definirFechaEvaluacionAnteproyecto(Integer idProyecto, EvaluacionDto evaluacionDto) throws Exception {
        Proyecto proyecto = getProyecto(idProyecto);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Elemento elemento = this.elementoService.getElemento(ID_ELEMENTO_CONVOCATORIA_ANTEPROYECTO);
        UsuarioProyecto supervisroProyecto = this.usuarioProyectoService.getSupervisorProyecto(idProyecto);
        EtapaProyecto etapaProyectoActiva = this.etapaService.getEtapaProyectoActivo(idProyecto);
        if (!supervisroProyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para definir fecha de evaluacion");
        }
        if (!etapaProyectoActiva.getIdEtapaFk().getIdEtapa().equals(EtapaUtils.ID_ETAPA_PROGRAMACION_PRESENTACION_ANTEPROYECTO)) {
            throw new Exception("No se puede definir fecha de evaluacion en esta etapa del proyecto");
        }
        Map<String, String> campos = getCamposConvocatoria(proyecto, evaluacionDto);
        MultipartFile convocatoria = generarPdf(campos, elemento);
        this.elementoService.crearElementoProyecto(proyecto, elemento, etapaProyectoActiva, convocatoria);
        crearEtapaProyecto(etapaProyectoActiva, proyecto, EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO);
    }

    private MultipartFile generarPdf(Map<String, String> campos, Elemento elemento) throws Exception {
        String templateUrl = this.storageService.getFile(elemento.getTemplate());
        InputStream template = new URL(templateUrl).openStream();
        MultipartFile convocatoria = this.pdfGeneratorService.generatePdf(campos, template, elemento.getNombreArchivo());
        return convocatoria;
    }

    private Map<String, String> getCamposConvocatoria(Proyecto proyecto, EvaluacionDto evaluacionDto) throws Exception {
        Carrera carrera = proyecto.getIdCarreraFk();
        Usuario estudiante = proyecto.getIdUsuarioFk();
        ElementoProyecto tituloProyecto = this.elementoService
                .getElementoProyectoActivo(proyecto.getIdProyecto(), ID_ELEMENTO_TITULO);
        Usuario coordinador = this.usuarioProyectoService
                .getCoordinadoCarreraProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
        Usuario supervisor = this.usuarioProyectoService
                .getSupervisorProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
        Usuario asesor = this.usuarioProyectoService
                .getAsesorProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
        Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
        CorrelativoEstudiante correlativo = this.correlativoService
                .crearCorrelativoConvocatoriaAnteproyecto(proyecto.getIdUsuarioFk(), carrera.getIdCarrera(), evaluacionDto);
        Map<String, String> campos = new HashMap<>();
        campos.put(KEY_CARRERA, carrera.getNombreCorto());
        campos.put(KEY_CORRELATIVO, correlativo.getCorrelativo());
        campos.put(KEY_CUIDAD, CUIDAD);
        campos.put(KEY_FECHA_ACTUAL, DateUtils.getFormatedDate());
        campos.put(KEY_NOMBRE_ESTUDIANTE, estudiante.getNombreCompleto());
        campos.put(KEY_CARNE_ESTUDIANTE, estudiante.getDpi());
        campos.put(KEY_REGISTRO_ACADEMICO_ESTUDIANTE, estudiante.getRegistroAcademico());
        campos.put(KEY_TITULO_PROYECTO, tituloProyecto.getInformacion());
        campos.put(KEY_TITULO_COORDINADOR_CARRERA, coordinador.getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_COORDINADOR_CARRERA, coordinador.getNombreCompleto());
        campos.put(KEY_TITULO_ASESOR, asesor.getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_ASESOR, asesor.getNombreCompleto());
        campos.put(KEY_TITULO_SUPERVISOR, supervisor.getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_SUPERVISOR, supervisor.getNombreCompleto());
        campos.put(KEY_TITULO_COORDINADOR_EPS, coordinadorEps.getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_COORDINADOR_EPS, coordinadorEps.getNombreCompleto());
        campos.put(KEY_DIA_EVALUACION, DateUtils.getDayFromDate(evaluacionDto.getFecha()));
        campos.put(KEY_FECHA_EVALUACION, DateUtils.getFormatedDate(evaluacionDto.getFecha()));
        campos.put(KEY_HORA_EVALUACION, DateUtils.getFormatedTime(evaluacionDto.getHora()));
        campos.put(KEY_SALON_EVALUACION, evaluacionDto.getSalon());
        if (evaluacionDto.getRepresentante() != null) {
            String informacion = "Evaluador: (Representante): "
                    + evaluacionDto.getTituloRepresentante().getAbreviatura() + " "
                    + evaluacionDto.getRepresentante();
            campos.put(KEY_INFORMACION_REPRESENTANTE_COORDIANDOR, informacion);
        }
        return campos;
    }
}
