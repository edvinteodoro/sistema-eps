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
import gt.edu.cunoc.sistemaeps.service.EmailService;
import gt.edu.cunoc.sistemaeps.service.EtapaService;
import gt.edu.cunoc.sistemaeps.service.PdfGeneratorService;
import gt.edu.cunoc.sistemaeps.service.RolService;
import gt.edu.cunoc.sistemaeps.service.StorageService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.util.DateUtils;
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
    private final EmailService emailService;
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
    private final String ACTA_TIPO_APROBACION = "APROBACION";

    private final int ID_ELEMENTO_TITULO = 1;
    private final int ID_ELEMENTO_ACTA_ANTEPROYECTO = 10;
    private final int ID_ELEMENTO_ACTA_EXAMEN_GENERAL = 17;

    private final String EMAI_SUBJECT_ACTA_ANTEPROYECTO = "ACTA ANTEPROYECTO";

    public ActaServiceImp(CorrelativoService correlativoService,
            UsuarioProyectoService usuarioProyectoService, ActaRepository actaRepository,
            ElementoService elementoService, StorageService storageService,
            PdfGeneratorService pdfGeneratorService, EtapaService etapaService,
            EmailService emailService, ConvocatoriaService convocatoriaService,
            UsuarioService usuarioService, RolService rolService) {
        this.actaRepository = actaRepository;
        this.correlativoService = correlativoService;
        this.usuarioProyectoService = usuarioProyectoService;
        this.elementoService = elementoService;
        this.storageService = storageService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.etapaService = etapaService;
        this.emailService = emailService;
        this.convocatoriaService = convocatoriaService;
        this.usuarioService = usuarioService;
        this.rolService = rolService;

    }

    private Map<String, String> getCampos(Proyecto proyecto, String correlativoFormato, Acta acta) throws Exception {
        Convocatoria convocatoria = this.convocatoriaService.getConvocatoriaAnteproyecto(proyecto);
        Carrera carrera = proyecto.getIdCarreraFk();
        ElementoProyecto tituloProyecto = this.elementoService
                .getElementoProyectoActivo(proyecto.getIdProyecto(), ID_ELEMENTO_TITULO);
        Usuario estudiante = proyecto.getIdUsuarioFk();
        Usuario coordinador = this.usuarioProyectoService
                .getCoordinadoCarreraProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
        Usuario supervisor = this.usuarioProyectoService
                .getSupervisorProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
        Usuario asesor = this.usuarioProyectoService
                .getAsesorProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
        Map<String, String> campos = new HashMap<>();
        campos.put(KEY_CORRELATIVO, correlativoFormato);
        campos.put(KEY_CUIDAD, CUIDAD);
        campos.put(KEY_DIA_EVALUACION, DateUtils.getDayFromDate(convocatoria.getFechaEvaluacion()));
        campos.put(KEY_FECHA_EVALUACION, DateUtils.getFormatedDate(convocatoria.getFechaEvaluacion()));
        campos.put(KEY_HORA_INICIO_EVALUACION, DateUtils.getFormatedTime(convocatoria.getHoraEvaluacion().toLocalTime()));
        campos.put(KEY_CARRERA, carrera.getNombre());
        campos.put(KEY_TITULO_COORDINADOR_CARRERA, coordinador.getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_COORDINADOR_CARRERA, coordinador.getNombreCompleto());
        campos.put(KEY_TITULO_ASESOR, asesor.getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_ASESOR, asesor.getNombreCompleto());
        campos.put(KEY_TITULO_SUPERVISOR, supervisor.getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_SUPERVISOR, supervisor.getNombreCompleto());
        campos.put(KEY_SALON_EVALUACION, convocatoria.getSalon());
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

    @Override
    public Acta crearActaAnteproyecto(Proyecto proyecto, ActaDto actaDto) throws Exception {
        Correlativo correlativo = this.correlativoService.getCorrelativo(EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO);
        correlativo.setNumeracionActual(correlativo.getNumeracionActual() + 1);
        String correlativoFormato = getCorrelativo(correlativo);
        Convocatoria convocatoria = this.convocatoriaService.getConvocatoriaAnteproyecto(proyecto);
        Acta acta = new Acta(actaDto);
        acta.setSalon(convocatoria.getSalon());
        acta.setFechaEvaluacion(convocatoria.getFechaEvaluacion());
        acta.setCorrelativo(correlativoFormato);
        acta.setIdProyectoFk(proyecto);
        this.correlativoService.save(correlativo);
        return this.actaRepository.save(acta);
    }

    @Override
    public Acta crearActaExamenGeneral(Proyecto proyecto, ActaDto actaDto) throws Exception {
        Correlativo correlativo = this.correlativoService.getCorrelativo(EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO);
        correlativo.setNumeracionActual(correlativo.getNumeracionActual() + 1);
        String correlativoFormato = getCorrelativo(correlativo);
        Convocatoria convocatoria = this.convocatoriaService.getConvocatoriaExamenGeneral(proyecto);
        Acta acta = new Acta(actaDto);
        acta.setSalon(convocatoria.getSalon());
        acta.setFechaEvaluacion(convocatoria.getFechaEvaluacion());
        acta.setCorrelativo(correlativoFormato);
        acta.setIdProyectoFk(proyecto);
        this.correlativoService.save(correlativo);
        return this.actaRepository.save(acta);
    }

    @Override
    public Acta generarActaAnterproyecto(Proyecto proyecto, ActaDto actaDto) throws Exception {
        Acta acta = getActaAnteproyecto(proyecto);
        acta.setHoraInicioEvaluacion(actaDto.getHoraInicioEvaluacion().atDate(LocalDate.now()));
        acta.setHoraFinEvaluacion(actaDto.getHoraFinEvaluacion().atDate(LocalDate.now()));
        acta.setTipo(ACTA_TIPO_ANTEPROYECTO);
        acta.setActaGenerada(Boolean.TRUE);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_EVALUACION_ANTEPROYECTO);
        Map<String, String> campos = getCampos(proyecto, acta.getCorrelativo(), acta);
        Elemento elemento = this.elementoService.getElemento(ID_ELEMENTO_ACTA_ANTEPROYECTO);
        MultipartFile convocatoriaPdf = generarPdf(campos, elemento);
        ElementoProyecto elementoConvocatoria = this.elementoService
                .crearElementoProyecto(proyecto, elemento, etapaProyecto, convocatoriaPdf);
        String urlFile = this.storageService.getFile(elementoConvocatoria.getInformacion());
        this.emailService.sendDocumentEmail("edvinteodoro-gonzalezrafael@cunoc.edu.gt",
                EMAI_SUBJECT_ACTA_ANTEPROYECTO, proyecto.getIdUsuarioFk().getNombreCompleto(),
                proyecto.getIdUsuarioFk().getRegistroAcademico(),
                proyecto.getIdCarreraFk().getNombre(), urlFile);
        return actaRepository.save(acta);
    }

    @Override
    public Acta generarActaExamenGeneral(Proyecto proyecto, ActaDto actaDto) throws Exception {
        Acta acta = getActaExamenGeneral(proyecto);
        acta.setHoraInicioEvaluacion(actaDto.getHoraInicioEvaluacion().atDate(LocalDate.now()));
        acta.setHoraFinEvaluacion(actaDto.getHoraFinEvaluacion().atDate(LocalDate.now()));
        acta.setTipo(ACTA_TIPO_EXAMEN_GENERAL);
        acta.setActaGenerada(Boolean.TRUE);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_EXAMEN_GENERAL);
        Map<String, String> campos = getCampos(proyecto, acta.getCorrelativo(), acta);
        Elemento elemento = this.elementoService.getElemento(ID_ELEMENTO_ACTA_EXAMEN_GENERAL);
        MultipartFile convocatoriaPdf = generarPdf(campos, elemento);
        ElementoProyecto elementoConvocatoria = this.elementoService
                .crearElementoProyecto(proyecto, elemento, etapaProyecto, convocatoriaPdf);
        //String urlFile = this.storageService.getFile(elementoConvocatoria.getInformacion());
        /*this.emailService.sendDocumentEmail("edvinteodoro-gonzalezrafael@cunoc.edu.gt",
                EMAI_SUBJECT_ACTA_ANTEPROYECTO, proyecto.getIdUsuarioFk().getNombreCompleto(),
                proyecto.getIdUsuarioFk().getRegistroAcademico(),
                proyecto.getIdCarreraFk().getNombre(), urlFile);*/
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
    public Page<Acta> getActasAnteproyecto(Pageable pageable) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        List<Rol> rolesUsuario = this.rolService.getRolesUsuario(usuario.getIdUsuario());
        Rol rolSecretaria = this.rolService.getRol(RolUtils.ID_ROL_SECRETARIA);
        if (!rolesUsuario.contains(rolSecretaria)) {
            throw new Exception("No tiene acceso a las actas");
        }
        return this.actaRepository.findAll(pageable);
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
    public Acta getActaAnteproyecto(Integer idActa) throws Exception {
        return this.actaRepository.findById(idActa).get();
    }

    @Override
    public Acta actualizarActa(Integer idActa, ActaDto actaDto) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
