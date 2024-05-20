package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.ActaDto;
import gt.edu.cunoc.sistemaeps.entity.Acta;
import gt.edu.cunoc.sistemaeps.entity.Carrera;
import gt.edu.cunoc.sistemaeps.entity.Convocatoria;
import gt.edu.cunoc.sistemaeps.entity.Correlativo;
import gt.edu.cunoc.sistemaeps.entity.Elemento;
import gt.edu.cunoc.sistemaeps.entity.ElementoProyecto;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.ActaRepository;
import gt.edu.cunoc.sistemaeps.service.ActaService;
import gt.edu.cunoc.sistemaeps.service.ConvocatoriaService;
import gt.edu.cunoc.sistemaeps.service.CorrelativoService;
import gt.edu.cunoc.sistemaeps.service.ElementoService;
import gt.edu.cunoc.sistemaeps.service.EtapaService;
import gt.edu.cunoc.sistemaeps.service.NotificacionService;
import gt.edu.cunoc.sistemaeps.service.PdfGeneratorService;
import gt.edu.cunoc.sistemaeps.service.RolService;
import gt.edu.cunoc.sistemaeps.service.StorageService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.specification.ActaFilter;
import gt.edu.cunoc.sistemaeps.specification.ActaSpecification;
import gt.edu.cunoc.sistemaeps.util.DateUtils;
import gt.edu.cunoc.sistemaeps.util.ElementoUtils;
import gt.edu.cunoc.sistemaeps.util.EtapaUtils;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
@Service
public class ActaServiceImp implements ActaService {

    private final ActaRepository actaRepository;
    private final CorrelativoService correlativoService;
    private final UsuarioProyectoService usuarioProyectoService;
    private final ElementoService elementoService;
    private final StorageService storageService;
    private final PdfGeneratorService pdfGeneratorService;
    private final EtapaService etapaService;
    private final NotificacionService notificacionService;
    private final ConvocatoriaService convocatoriaService;
    private final UsuarioService usuarioService;
    private final RolService rolService;

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
    private final String KEY_NOMBRE_COORDINADOR_EPS = "${nombre_coordinador_eps}";
    private final String KEY_TITULO_ASESOR_EPS = "${titulo_coordinador_eps}";
    private final String KEY_NOMBRE_ASESOR_EPS = "${nombre_coordiandor_eps}";
    private final String KEY_INFORMACION_REPRESENTANTE_COORDIANDOR = "${informacion_representante}";
    private final String KEY_DIA_EVALUACION = "${dia_evaluacion}";
    private final String KEY_FECHA_EVALUACION = "${fecha_evaluacion}";
    private final String KEY_HORA_INICIO_EVALUACION = "${hora_inicio_evaluacion}";
    private final String KEY_HORA_FIN_EVALUACION = "${hora_fin_evaluacion}";
    private final String KEY_SALON_EVALUACION = "${salon_evaluacion}";
    private final String KEY_SEMESTRE = "${semestre}";
    private final String KEY_ANIO = "${anio}";
    private final String KEY_CARRERA_ESTUDIANTE = "${carrera_estudiante}";
    private final String KEY_RESULTADO = "${resultado}";
    private final String CUIDAD = "Quetzaltenango";

    private final String ACTA_TIPO_ANTEPROYECTO = "ANTEPROYECTO";
    private final String ACTA_TIPO_EXAMEN_GENERAL = "EXAMEN GENERAL";
    private final String ACTA_TIPO_FINALIZACION = "FINALIZACION";

    private final String EMAI_SUBJECT_ACTA_ANTEPROYECTO = "ACTA ANTEPROYECTO";

    public ActaServiceImp(CorrelativoService correlativoService,
            UsuarioProyectoService usuarioProyectoService, ActaRepository actaRepository,
            ElementoService elementoService, StorageService storageService,
            PdfGeneratorService pdfGeneratorService, EtapaService etapaService,
            NotificacionService notificacionService, ConvocatoriaService convocatoriaService,
            UsuarioService usuarioService, RolService rolService) {
        this.actaRepository = actaRepository;
        this.correlativoService = correlativoService;
        this.usuarioProyectoService = usuarioProyectoService;
        this.elementoService = elementoService;
        this.storageService = storageService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.etapaService = etapaService;
        this.notificacionService = notificacionService;
        this.convocatoriaService = convocatoriaService;
        this.usuarioService = usuarioService;
        this.rolService = rolService;

    }

    private Map<String, String> getCampos(Proyecto proyecto, String correlativoFormato, Convocatoria convocatoria, Acta acta) throws Exception {
        Carrera carrera = proyecto.getIdCarreraFk();
        ElementoProyecto tituloProyecto = this.elementoService
                .getElementoProyectoActivo(proyecto.getIdProyecto(), ElementoUtils.ID_ELEMENTO_TITULO);
        Usuario estudiante = proyecto.getIdUsuarioFk();
        Usuario coordinador = convocatoria.getIdCoordinadorCarreraFk();
        Usuario coordinadorEps = convocatoria.getIdCoordinadorEpsFk();
        Usuario supervisor = convocatoria.getIdSupervisorFk();
        Usuario asesor = convocatoria.getIdAsesorFk();
        Map<String, String> campos = new HashMap<>();
        campos.put(KEY_CORRELATIVO, correlativoFormato);
        campos.put(KEY_CUIDAD, CUIDAD);
        campos.put(KEY_DIA_EVALUACION, DateUtils.getDayFromDate(acta.getFechaEvaluacion()));
        campos.put(KEY_FECHA_EVALUACION, DateUtils.getFormatedDate(acta.getFechaEvaluacion()));
        campos.put(KEY_HORA_INICIO_EVALUACION, DateUtils.getFormatedTime(acta.getHoraInicioEvaluacion().toLocalTime()));
        campos.put(KEY_CARRERA, carrera.getNombre());
        campos.put(KEY_TITULO_COORDINADOR_CARRERA, coordinador.getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_COORDINADOR_CARRERA, coordinador.getNombreCompleto());
        if (coordinadorEps != null) {
            campos.put(KEY_TITULO_COORDINADOR_EPS, coordinadorEps.getIdTituloFk().getAbreviatura());
            campos.put(KEY_NOMBRE_COORDINADOR_EPS, coordinadorEps.getNombreCompleto());
        }
        campos.put(KEY_TITULO_ASESOR, asesor.getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_ASESOR, asesor.getNombreCompleto());
        campos.put(KEY_TITULO_SUPERVISOR, supervisor.getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_SUPERVISOR, supervisor.getNombreCompleto());
        campos.put(KEY_SALON_EVALUACION, acta.getSalon());
        campos.put(KEY_SEMESTRE, proyecto.getSemestre());
        campos.put(KEY_ANIO, String.valueOf(LocalDate.now().getYear()));
        campos.put(KEY_CARRERA_ESTUDIANTE, carrera.getNombre());
        campos.put(KEY_NOMBRE_ESTUDIANTE, estudiante.getNombreCompleto());
        campos.put(KEY_CARNE_ESTUDIANTE, estudiante.getDpi());
        campos.put(KEY_REGISTRO_ACADEMICO_ESTUDIANTE, estudiante.getRegistroAcademico());
        campos.put(KEY_TITULO_PROYECTO, tituloProyecto.getInformacion());
        campos.put(KEY_HORA_FIN_EVALUACION, DateUtils.getFormatedTime(acta.getHoraFinEvaluacion().toLocalTime()));
        if (acta.getResultado().equals("APROBADO")) {
            campos.put(KEY_RESULTADO, "APROBAR");
        } else if (acta.getResultado().equals("APROBADO CON CORRECCIONES")) {
            campos.put(KEY_RESULTADO, "APROBAR CON CORRECCIONES");
        } else if (acta.getResultado().equals("RECHAZADO")) {
            campos.put(KEY_RESULTADO, "RECHAZAR");
        } else {
            throw new Exception("Resultado de evaluacion no valido");
        }
        return campos;
    }

    private void desactivarActaAnteproyecto(Proyecto proyecto) throws Exception {
        Acta acta = getActaAnteproyecto(proyecto);
        if (acta != null) {
            acta.setActivo(Boolean.FALSE);
            this.actaRepository.save(acta);
        }
    }

    private void desactivarActaExamenGeneral(Proyecto proyecto) throws Exception {
        Acta acta = getActaExamenGeneral(proyecto);
        if (acta != null) {
            acta.setActivo(Boolean.FALSE);
            this.actaRepository.save(acta);
        }
    }

    private void desactivarActaAprobacion(Proyecto proyecto) throws Exception {
        Acta acta = getActaAprobacion(proyecto);
        if (acta != null) {
            acta.setActivo(Boolean.FALSE);
            this.actaRepository.save(acta);
        }
    }

    @Override
    public Acta crearActaAnteproyecto(Proyecto proyecto, ActaDto actaDto) throws Exception {
        desactivarActaAnteproyecto(proyecto);
        Correlativo correlativo = this.correlativoService.getCorrelativo(EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO);
        correlativo.setNumeracionActual(correlativo.getNumeracionActual() + 1);
        String correlativoFormato = getCorrelativo(correlativo);
        Convocatoria convocatoria = this.convocatoriaService.getConvocatoriaAnteproyecto(proyecto);
        Acta acta = new Acta(actaDto);
        acta.setSalon(convocatoria.getSalon());
        acta.setTipo(ACTA_TIPO_ANTEPROYECTO);
        acta.setFechaEvaluacion(actaDto.getFechaEvaluacion());
        acta.setCorrelativo(correlativoFormato);
        acta.setIdProyectoFk(proyecto);
        this.correlativoService.save(correlativo);
        Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
        this.notificacionService.notificarCreacionActa(secretaria, proyecto);
        return this.actaRepository.save(acta);
    }

    @Override
    public Acta crearActaExamenGeneral(Proyecto proyecto, ActaDto actaDto) throws Exception {
        desactivarActaExamenGeneral(proyecto);
        Correlativo correlativo = this.correlativoService.getCorrelativo(EtapaUtils.ID_ETAPA_EXAMEN_GENERAL);
        correlativo.setNumeracionActual(correlativo.getNumeracionActual() + 1);
        String correlativoFormato = getCorrelativo(correlativo);
        Convocatoria convocatoria = this.convocatoriaService.getConvocatoriaExamenGeneral(proyecto);
        Acta acta = new Acta(actaDto);
        acta.setSalon(convocatoria.getSalon());
        acta.setTipo(ACTA_TIPO_EXAMEN_GENERAL);
        acta.setFechaEvaluacion(actaDto.getFechaEvaluacion());
        acta.setCorrelativo(correlativoFormato);
        acta.setIdProyectoFk(proyecto);
        this.correlativoService.save(correlativo);
        Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
        this.notificacionService.notificarCreacionActaExamenGeneral(secretaria, proyecto);
        return this.actaRepository.save(acta);
    }

    @Override
    public Acta crearActaFinalizacion(Proyecto proyecto, ActaDto actaDto) throws Exception {
        desactivarActaAprobacion(proyecto);
        Correlativo correlativo = this.correlativoService.getCorrelativo(EtapaUtils.ID_ETAPA_ACTA_FINALIZACION);
        correlativo.setNumeracionActual(correlativo.getNumeracionActual() + 1);
        String correlativoFormato = getCorrelativo(correlativo);
        Acta acta = new Acta(actaDto);
        acta.setFechaEvaluacion(actaDto.getFechaEvaluacionInput());
        acta.setHoraInicioEvaluacion(actaDto.getHoraInicioEvaluacion().atDate(LocalDate.now()));
        acta.setHoraFinEvaluacion(actaDto.getHoraFinEvaluacion().atDate(LocalDate.now()));
        acta.setCorrelativo(correlativoFormato);
        acta.setTipo(ACTA_TIPO_FINALIZACION);
        acta.setIdProyectoFk(proyecto);
        this.correlativoService.save(correlativo);
        Usuario secretaria = this.usuarioProyectoService.getSecretariaDisponible();
        this.notificacionService.notificarCreacionActaExamenGeneral(secretaria, proyecto);
        return this.actaRepository.save(acta);
    }

    @Override
    public Acta generarActaAnterproyecto(Proyecto proyecto, ActaDto actaDto) throws Exception {
        Convocatoria convocatoria = this.convocatoriaService.getConvocatoriaAnteproyecto(proyecto);
        Acta acta = getActaAnteproyecto(proyecto);
        acta.setHoraInicioEvaluacion(actaDto.getHoraInicioEvaluacion().atDate(LocalDate.now()));
        acta.setHoraFinEvaluacion(actaDto.getHoraFinEvaluacion().atDate(LocalDate.now()));
        acta.setActaGenerada(Boolean.TRUE);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO);
        Map<String, String> campos = getCampos(proyecto, acta.getCorrelativo(), convocatoria, acta);
        Elemento elemento = this.elementoService.getElemento(ElementoUtils.ID_ELEMENTO_ACTA_ANTEPROYECTO);
        MultipartFile convocatoriaPdf = generarPdf(campos, elemento);
        ElementoProyecto elementoConvocatoria = this.elementoService
                .crearElementoProyecto(proyecto, elemento, etapaProyecto, convocatoriaPdf);
        return actaRepository.save(acta);
    }

    @Override
    public Acta generarActaExamenGeneral(Proyecto proyecto, ActaDto actaDto) throws Exception {
        Acta acta = getActaExamenGeneral(proyecto);
        Convocatoria convocatoria = this.convocatoriaService.getConvocatoriaExamenGeneral(proyecto);
        acta.setHoraInicioEvaluacion(actaDto.getHoraInicioEvaluacion().atDate(LocalDate.now()));
        acta.setHoraFinEvaluacion(actaDto.getHoraFinEvaluacion().atDate(LocalDate.now()));
        acta.setActaGenerada(Boolean.TRUE);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_EXAMEN_GENERAL);
        Map<String, String> campos = getCampos(proyecto, acta.getCorrelativo(), convocatoria, acta);
        Elemento elemento = this.elementoService.getElemento(ElementoUtils.ID_ELEMENTO_ACTA_EXAMEN_GENERAL);
        MultipartFile convocatoriaPdf = generarPdf(campos, elemento);
        ElementoProyecto elementoConvocatoria = this.elementoService
                .crearElementoProyecto(proyecto, elemento, etapaProyecto, convocatoriaPdf);
        return actaRepository.save(acta);
    }

    @Override
    public Acta generarActaAprobacion(Proyecto proyecto, ActaDto actaDto) throws Exception {
        Convocatoria convocatoria = new Convocatoria();
        Usuario supervisor = this.usuarioProyectoService
                .getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Usuario asesor = this.usuarioProyectoService
                .getAsesorProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
        Usuario coordinador = this.usuarioProyectoService
                .getCoordinadorCarreraDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
        convocatoria.setIdSupervisorFk(supervisor);
        convocatoria.setIdAsesorFk(asesor);
        convocatoria.setIdCoordinadorCarreraFk(coordinador);
        convocatoria.setIdCoordinadorEpsFk(coordinadorEps);
        Acta acta = getActaAprobacion(proyecto);
        acta.setHoraInicioEvaluacion(actaDto.getHoraInicioEvaluacion().atDate(LocalDate.now()));
        acta.setHoraFinEvaluacion(actaDto.getHoraFinEvaluacion().atDate(LocalDate.now()));
        acta.setActaGenerada(Boolean.TRUE);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_ACTA_FINALIZACION);
        Map<String, String> campos = getCampos(proyecto, acta.getCorrelativo(), convocatoria, acta);
        Elemento elemento = this.elementoService.getElemento(ElementoUtils.ID_ELEMENTO_ACTA_FINALIZACION);
        MultipartFile convocatoriaPdf = generarPdf(campos, elemento);
        ElementoProyecto elementoConvocatoria = this.elementoService
                .crearElementoProyecto(proyecto, elemento, etapaProyecto, convocatoriaPdf);
        return actaRepository.save(acta);
    }

    @Override
    public Acta getActaAnteproyecto(Proyecto proyecto) throws Exception {
        Acta acta = this.actaRepository.findActaActiva(proyecto.getIdProyecto(),
                ACTA_TIPO_ANTEPROYECTO, Boolean.TRUE);
        return acta;
    }

    @Override
    public Acta getActaExamenGeneral(Proyecto proyecto) throws Exception {
        Acta acta = this.actaRepository.findActaActiva(proyecto.getIdProyecto(),
                ACTA_TIPO_EXAMEN_GENERAL, Boolean.TRUE);
        return acta;
    }

    @Override
    public Acta getActaAprobacion(Proyecto proyecto) throws Exception {
        Acta acta = this.actaRepository.findActaActiva(proyecto.getIdProyecto(),
                ACTA_TIPO_FINALIZACION, Boolean.TRUE);
        return acta;
    }

    @Override
    public Page<Acta> getActas(String nombre, String registroAcademico, Pageable pageable) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        List<Rol> rolesUsuario = this.rolService.getRolesUsuario(usuario.getIdUsuario());
        Rol rolSecretaria = this.rolService.getRol(RolUtils.ID_ROL_SECRETARIA);
        ActaFilter filter = new ActaFilter();
        filter.setNombreEstudiante(nombre);
        filter.setRegistroEstudiante(registroAcademico);
        Specification<Acta> spec = ActaSpecification.filterBy(filter);
        if (!rolesUsuario.contains(rolSecretaria)) {
            throw new Exception("No tiene acceso a las actas");
        }
        return this.actaRepository.findAll(spec, pageable);
    }

    private String getCorrelativo(Correlativo correlativo) {
        String formato = "%02d-%d";
        Integer anio = correlativo.getUltimaActualizacion().getYear();
        return String.format(formato, correlativo.getNumeracionActual(), anio);
    }

    private MultipartFile generarPdf(Map<String, String> campos, Elemento elemento) throws Exception {
        String templateUrl = this.storageService.getFile(elemento.getTemplate());
        InputStream template = new URL(templateUrl).openStream();
        MultipartFile convocatoria = this.pdfGeneratorService.generatePdf(campos, template, elemento.getNombreArchivo());
        return convocatoria;
    }

    @Override
    public Acta getActa(Integer idActa) throws Exception {
        return this.actaRepository.findById(idActa).get();
    }

    @Override
    public Acta actualizarActa(Integer idActa, ActaDto actaDto) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
