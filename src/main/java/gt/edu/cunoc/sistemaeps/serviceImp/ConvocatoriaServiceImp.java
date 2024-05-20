package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.ConvocatoriaDto;
import gt.edu.cunoc.sistemaeps.entity.Carrera;
import gt.edu.cunoc.sistemaeps.entity.Convocatoria;
import gt.edu.cunoc.sistemaeps.entity.Correlativo;
import gt.edu.cunoc.sistemaeps.entity.Elemento;
import gt.edu.cunoc.sistemaeps.entity.ElementoProyecto;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.ConvocatoriaRepository;
import gt.edu.cunoc.sistemaeps.service.ConvocatoriaService;
import gt.edu.cunoc.sistemaeps.service.CorrelativoService;
import gt.edu.cunoc.sistemaeps.service.ElementoService;
import gt.edu.cunoc.sistemaeps.service.EtapaService;
import gt.edu.cunoc.sistemaeps.service.NotificacionService;
import gt.edu.cunoc.sistemaeps.service.PdfGeneratorService;
import gt.edu.cunoc.sistemaeps.service.StorageService;
import gt.edu.cunoc.sistemaeps.service.TituloService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.util.DateUtils;
import gt.edu.cunoc.sistemaeps.util.ElementoUtils;
import gt.edu.cunoc.sistemaeps.util.EtapaUtils;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
@Service
public class ConvocatoriaServiceImp implements ConvocatoriaService {

    private final ConvocatoriaRepository convocatoriaRepository;
    private final CorrelativoService correlativoService;
    private final UsuarioProyectoService usuarioProyectoService;
    private final ElementoService elementoService;
    private final StorageService storageService;
    private final PdfGeneratorService pdfGeneratorService;
    private final EtapaService etapaService;
    private final NotificacionService notificacionService;
    private final TituloService tituloService;

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
    private final String KEY_NOMBRE_COORDINADOR_EPS = "${nombre_coordinador_eps}";
    private final String KEY_TITULO_ASESOR_EPS = "${titulo_coordinador_eps}";
    private final String KEY_NOMBRE_ASESOR_EPS = "${nombre_coordiandor_eps}";
    private final String KEY_INFORMACION_REPRESENTANTE_COORDIANDOR = "${informacion_representante}";
    private final String KEY_DIA_EVALUACION = "${dia_evaluacion}";
    private final String KEY_FECHA_EVALUACION = "${fecha_evaluacion}";
    private final String KEY_HORA_EVALUACION = "${hora_evaluacion}";
    private final String KEY_HORA_FIN_EVALUACION = "${hora_fin_evaluacion}";
    private final String KEY_SALON_EVALUACION = "${salon_evaluacion}";
    private final String KEY_SEMESTRE = "${semestre}";
    private final String KEY_ANIO = "${anio}";
    private final String KEY_CARRERA_ESTUDIANTE = "${carrera_estudiante}";
    private final String KEY_RESULTADO = "${resultado}";
    private final String CUIDAD = "Quetzaltenango";

    private final String CONVOCATORIA_TIPO_ANTEPROYECTO = "ANTEPROYECTO";
    private final String CONVOCATORIA_TIPO_EXAMEN_GENERAL = "EXAMEN GENERAL";

    public ConvocatoriaServiceImp(CorrelativoService correlativoService,
            UsuarioProyectoService usuarioProyectoService, ElementoService elementoService,
            StorageService storageService, PdfGeneratorService pdfGeneratorService,
            ConvocatoriaRepository convocatoriaRepository, EtapaService etapaService,
            NotificacionService notificacionService, TituloService tituloService) {
        this.correlativoService = correlativoService;
        this.usuarioProyectoService = usuarioProyectoService;
        this.elementoService = elementoService;
        this.storageService = storageService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.convocatoriaRepository = convocatoriaRepository;
        this.etapaService = etapaService;
        this.notificacionService = notificacionService;
        this.tituloService = tituloService;
    }

    private Map<String, String> getCamposConvocatoria(Proyecto proyecto, String correlativoFormato, ConvocatoriaDto convocatoriaDto,Convocatoria convocatoria) throws Exception {
        Carrera carrera = proyecto.getIdCarreraFk();
        Usuario estudiante = proyecto.getIdUsuarioFk();
        ElementoProyecto tituloProyecto = this.elementoService
                .getElementoProyectoActivo(proyecto.getIdProyecto(), ElementoUtils.ID_ELEMENTO_TITULO);
        Map<String, String> campos = new HashMap<>();
        campos.put(KEY_CARRERA, carrera.getNombreCorto());
        campos.put(KEY_CORRELATIVO, correlativoFormato);
        campos.put(KEY_CUIDAD, CUIDAD);
        campos.put(KEY_FECHA_ACTUAL, DateUtils.getFormatedDate());
        campos.put(KEY_NOMBRE_ESTUDIANTE, estudiante.getNombreCompleto());
        campos.put(KEY_CARNE_ESTUDIANTE, estudiante.getDpi());
        campos.put(KEY_REGISTRO_ACADEMICO_ESTUDIANTE, estudiante.getRegistroAcademico());
        campos.put(KEY_TITULO_PROYECTO, tituloProyecto.getInformacion());
        campos.put(KEY_TITULO_COORDINADOR_CARRERA, convocatoria.getIdCoordinadorCarreraFk().getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_COORDINADOR_CARRERA, convocatoria.getIdCoordinadorCarreraFk().getNombreCompleto());
        campos.put(KEY_TITULO_ASESOR, convocatoria.getIdAsesorFk().getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_ASESOR, convocatoria.getIdAsesorFk().getNombreCompleto());
        campos.put(KEY_TITULO_SUPERVISOR, convocatoria.getIdSupervisorFk().getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_SUPERVISOR, convocatoria.getIdSupervisorFk().getNombreCompleto());
        campos.put(KEY_TITULO_COORDINADOR_EPS, convocatoria.getIdCoordinadorEpsFk().getIdTituloFk().getAbreviatura());
        campos.put(KEY_NOMBRE_COORDINADOR_EPS, convocatoria.getIdCoordinadorEpsFk().getNombreCompleto());
        campos.put(KEY_DIA_EVALUACION, DateUtils.getDayFromDate(convocatoriaDto.getFechaEvaluacion()));
        campos.put(KEY_FECHA_EVALUACION, DateUtils.getFormatedDate(convocatoriaDto.getFechaEvaluacion()));
        campos.put(KEY_HORA_EVALUACION, DateUtils.getFormatedTime(convocatoriaDto.getHoraEvaluacion()));
        campos.put(KEY_SALON_EVALUACION, convocatoriaDto.getSalon());
        if (convocatoriaDto.getRepresentante() != null && !convocatoriaDto.getRepresentante().isBlank()) {
            String informacion = "Evaluador: (Representante): "
                    + convocatoriaDto.getTituloRepresentante().getAbreviatura() + " "
                    + convocatoriaDto.getRepresentante();
            campos.put(KEY_INFORMACION_REPRESENTANTE_COORDIANDOR, informacion);
        } else {
            campos.put(KEY_INFORMACION_REPRESENTANTE_COORDIANDOR, "");
        }
        return campos;
    }

    @Override
    public Convocatoria generarConvocatoriaAnteproyecto(Proyecto proyecto, ConvocatoriaDto convocatoriaDto) throws Exception {
        Correlativo correlativo = this.correlativoService.getCorrelativo(proyecto.getIdCarreraFk().getIdCarrera(),
                EtapaUtils.ID_ETAPA_CONVOCATORIA_ANTEPROYECTO);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_CONVOCATORIA_ANTEPROYECTO);
        Usuario supervisor = this.usuarioProyectoService
                .getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Usuario asesor = this.usuarioProyectoService
                .getAsesorProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
        Usuario coordinador = this.usuarioProyectoService
                .getCoordinadorCarreraDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
        correlativo.setNumeracionActual(correlativo.getNumeracionActual() + 1);
        String correlativoFormato = getCorrelativo(correlativo);
        Convocatoria convocatoria = new Convocatoria(convocatoriaDto);
        convocatoria.setCorrelativo(correlativoFormato);
        convocatoria.setIdProyectoFk(proyecto);
        convocatoria.setTipo(CONVOCATORIA_TIPO_ANTEPROYECTO);
        convocatoria.setIdSupervisorFk(supervisor);
        convocatoria.setIdCoordinadorCarreraFk(coordinador);
        convocatoria.setIdAsesorFk(asesor);
        convocatoria.setIdCoordinadorEpsFk(coordinadorEps);
        if (convocatoriaDto.getTituloRepresentante() != null) {
            convocatoria.setIdTituloRepresentanteFk(this.tituloService
                    .getTitulo(convocatoriaDto.getTituloRepresentante().getIdTitulo()));
        }
        Map<String, String> campos = getCamposConvocatoria(proyecto, correlativoFormato, convocatoriaDto,convocatoria);
        Elemento elemento = this.elementoService.getElemento(ElementoUtils.ID_ELEMENTO_CONVOCATORIA_ANTEPROYECTO);
        MultipartFile convocatoriaPdf = generarPdf(campos, elemento);
        ElementoProyecto elementoConvocatoria = this.elementoService
                .crearElementoProyecto(proyecto, elemento, etapaProyecto, convocatoriaPdf);
        String urlFile = this.storageService.getFile(elementoConvocatoria.getInformacion());
        this.notificacionService.notificarGeneracionConvocatoria(coordinadorEps, proyecto, urlFile);
        correlativoService.save(correlativo);
        return convocatoriaRepository.save(convocatoria);
    }

    @Override
    public Convocatoria generarConvocatoriaExamenGeneral(Proyecto proyecto, ConvocatoriaDto convocatoriaDto) throws Exception {
        Correlativo correlativo = this.correlativoService.getCorrelativo(EtapaUtils.ID_ETAPA_CONVOCATORIA_EXAMEN_GENERAL);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_CONVOCATORIA_EXAMEN_GENERAL);
        Usuario supervisor = this.usuarioProyectoService
                .getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Usuario asesor = this.usuarioProyectoService
                .getAsesorProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
        Usuario coordinador = this.usuarioProyectoService
                .getCoordinadorCarreraDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
        correlativo.setNumeracionActual(correlativo.getNumeracionActual() + 1);
        String correlativoFormato = getCorrelativo(correlativo);
        Convocatoria convocatoria = new Convocatoria(convocatoriaDto);
        convocatoria.setCorrelativo(correlativoFormato);
        convocatoria.setIdProyectoFk(proyecto);
        convocatoria.setTipo(CONVOCATORIA_TIPO_EXAMEN_GENERAL);
        convocatoria.setIdSupervisorFk(supervisor);
        convocatoria.setIdCoordinadorCarreraFk(coordinador);
        convocatoria.setIdAsesorFk(asesor); 
        convocatoria.setIdCoordinadorEpsFk(coordinadorEps);
        if (convocatoriaDto.getTituloRepresentante() != null) {
            convocatoria.setIdTituloRepresentanteFk(this.tituloService
                    .getTitulo(convocatoriaDto.getTituloRepresentante().getIdTitulo()));
        }
        Map<String, String> campos = getCamposConvocatoria(proyecto, correlativoFormato, convocatoriaDto,convocatoria);
        Elemento elemento = this.elementoService.getElemento(ElementoUtils.ID_ELEMENTO_CONVOCATORIA_EXAMEN_GENERAL);
        MultipartFile convocatoriaPdf = generarPdf(campos, elemento);
        ElementoProyecto elementoConvocatoria = this.elementoService
                .crearElementoProyecto(proyecto, elemento, etapaProyecto, convocatoriaPdf);
        String urlFile = this.storageService.getFile(elementoConvocatoria.getInformacion());
        this.notificacionService.notificarGeneracionConvocatoriaExamenGeneral(coordinadorEps, proyecto, urlFile);
        correlativoService.save(correlativo);
        return convocatoriaRepository.save(convocatoria);
    }

    @Override
    public Convocatoria actualizarConvocatoriaAnteproyecto(Proyecto proyecto, ConvocatoriaDto convocatoriaDto) throws Exception {
        Convocatoria convocatoriaActiva = getConvocatoriaAnteproyecto(proyecto);
        convocatoriaActiva.setActivo(Boolean.FALSE);
        this.convocatoriaRepository.save(convocatoriaActiva);
        return generarConvocatoriaAnteproyecto(proyecto, convocatoriaDto);
    }

    @Override
    public Convocatoria actualizarConvocatoriaExamenGeneral(Proyecto proyecto, ConvocatoriaDto convocatoriaDto) throws Exception {
        Convocatoria convocatoriaActiva = getConvocatoriaExamenGeneral(proyecto);
        convocatoriaActiva.setActivo(Boolean.FALSE);
        this.convocatoriaRepository.save(convocatoriaActiva);
        return generarConvocatoriaExamenGeneral(proyecto, convocatoriaDto);
    }

    @Override
    public Convocatoria getConvocatoriaAnteproyecto(Proyecto proyecto) throws Exception {
        Convocatoria convocatoriaActiva = this.convocatoriaRepository
                .findConvocatoriaActiva(proyecto.getIdProyecto(), Boolean.TRUE, CONVOCATORIA_TIPO_ANTEPROYECTO);
        return convocatoriaActiva;
    }

    @Override
    public Convocatoria getConvocatoriaExamenGeneral(Proyecto proyecto) throws Exception {
        Convocatoria convocatoriaActiva = this.convocatoriaRepository
                .findConvocatoriaActiva(proyecto.getIdProyecto(), Boolean.TRUE, CONVOCATORIA_TIPO_EXAMEN_GENERAL);
        return convocatoriaActiva;
    }

    @Override
    public void cargarConvocatoria(Proyecto proyecto, MultipartFile file) throws Exception {
        Elemento elemento = this.elementoService.getElemento(ElementoUtils.ID_ELEMENTO_CONVOCATORIA_ANTEPROYECTO_FIRMADA);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_ANTEPROYECTO);
        ElementoProyecto elementoConvocatoria = this.elementoService.crearElementoProyecto(proyecto, elemento,
                etapaProyecto, file);
        String urlFile = this.storageService.getFile(elementoConvocatoria.getInformacion());
        notificarCargaConvocatoria(proyecto, urlFile);

    }

    private void notificarCargaConvocatoria(Proyecto proyecto, String urlFile) {
        try {
            Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
            Usuario coordinadorCarrera = this.usuarioProyectoService.getCoordinadorCarreraDisponible(proyecto.getIdCarreraFk().getIdCarrera());
            Usuario asesor = this.usuarioProyectoService.getAsesorProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
            this.notificacionService.notificarConvocatoriaAnteproyecto(supervisor, proyecto, getConvocatoriaAnteproyecto(proyecto), urlFile);
            this.notificacionService.notificarConvocatoriaAnteproyecto(coordinadorCarrera, proyecto, getConvocatoriaAnteproyecto(proyecto), urlFile);
            this.notificacionService.notificarConvocatoriaAnteproyecto(asesor, proyecto, getConvocatoriaAnteproyecto(proyecto), urlFile);
            this.notificacionService.notificarConvocatoriaAnteproyecto(proyecto.getIdUsuarioFk(), proyecto, getConvocatoriaAnteproyecto(proyecto), urlFile);
        } catch (Exception e) {
        }
    }

    @Override
    public void cargarConvocatoriaExamenGeneral(Proyecto proyecto, MultipartFile file) throws Exception {
        Elemento elemento = this.elementoService.getElemento(ElementoUtils.ID_ELEMENTO_CONVOCATORIA_EXAMEN_GENERAL_FIRMADA);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_CARGA_CONVOCATORIA_EXAMEN_GENERAL);
        ElementoProyecto elementoConvocatoria = this.elementoService.crearElementoProyecto(proyecto, elemento,
                etapaProyecto, file);
        String urlFile = this.storageService.getFile(elementoConvocatoria.getInformacion());
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Usuario coordinadorCarrera = this.usuarioProyectoService.getCoordinadorCarreraDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Usuario asesor = this.usuarioProyectoService.getAsesorProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
        this.notificacionService.notificarConvocatoriaExamenGeneral(supervisor, proyecto, getConvocatoriaExamenGeneral(proyecto), urlFile);
        this.notificacionService.notificarConvocatoriaExamenGeneral(coordinadorCarrera, proyecto, getConvocatoriaExamenGeneral(proyecto), urlFile);
        this.notificacionService.notificarConvocatoriaExamenGeneral(asesor, proyecto, getConvocatoriaExamenGeneral(proyecto), urlFile);
        this.notificacionService.notificarConvocatoriaExamenGeneral(proyecto.getIdUsuarioFk(), proyecto, getConvocatoriaExamenGeneral(proyecto), urlFile);
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

    public String buildMessage(String template, Map<String, String> values) {
        return String.format(template, values.get("${titulo_proyecto}"),
                values.get("${nombre_estudiante}"), values.get("${registro_academico_estudiante}"));
    }

}
