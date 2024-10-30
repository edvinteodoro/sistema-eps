package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.controller.UsuarioController;
import gt.edu.cunoc.sistemaeps.dto.ComentarioDto;
import gt.edu.cunoc.sistemaeps.entity.Convocatoria;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.service.ElementoService;
import gt.edu.cunoc.sistemaeps.service.EmailService;
import gt.edu.cunoc.sistemaeps.service.NotificacionService;
import gt.edu.cunoc.sistemaeps.util.DateUtils;
import gt.edu.cunoc.sistemaeps.util.ElementoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class NotificacionServiceImp implements NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final String activeProfile;

    private static final String ASIGNACION_MSG = "Se le ha asignado al proyecto, bajo el rol de %s.";
    private static final String SOLICITUD_REVISION_MSG = "Se le ha solicitado realizar la revisión del proyecto.";
    private static final String SOLICITUD_CAMBIOS_MSG = "Se le ha solicitado realizar cambios: %s";
    private static final String FINALIZACION_PROYECTO_MSG = "El supervisor ha finalizado el proyecto.";
    private static final String CREACION_ACTA_MSG = "Se ha generado acta, puede descargarlo en la seccion de actas.";

    private static final String GENERACION_CONVOCATORIA_ANTEPROYECTO_MSG = "Se ha generado convocatoria de evaluacion de anteproyecto, debera firmarla y cargarla al sistema.";
    private static final String CONVOCATORIA_ANTEPROYECTO_MSG = "Se ha definido la fecha y hora de evaluacion del anteproyecto. Dia: %s, hora: %s";
    private static final String GENERACION_CONVOCATORIA_EXAMEN_GENERAL_MSG = "Se ha generado convocatoria de examen general de proyecto, debera firmarla y cargarla al sistema.";
    private static final String CONVOCATORIA_EXAMEN_GENERAL_MSG = "Se ha definido la fecha y hora de evaluacion del proyecto. Dia: %s, hora: %s";

    private static final String ETAPA_NUEVA_MSG = "El proyecto ahora se encuentra en la etapa: %s, descripcion: %s";

    private static final String REGISTRO_BITACORA_MSG = "se ha creado un nuevo registro de bitacora.";
    private static final String SOLICITUD_PRORROGA_MSG = "se ha creado una solicitud de prorroga.";
    private static final String RESPUESTA_PRORROGA_MSG = "se ha registrado una respuesta a su solicitud de prorroga.";

    private final String EMAIL_SUBJECT_ETAPA_NUEVA = "ETAPA DE PROYECTO";
    private final String EMAIL_SUBJECT_ASIGNACION_USUARIO = "ASIGNACION A PROYECTO";
    private final String EMAIL_SUBJECT_SOLICITUD_REVISION = "SOLICITUD DE REVISION";
    private final String EMAIL_SUBJECT_SOLICITUD_CAMBIOS = "SOLICITUD DE CAMBIOS";
    private final String EMAIL_SUBJECT_FINALIZACION_PROYECTO = "PROYECTO FINALIZADO";
    private final String EMAIL_SUBJECT_CONVOCATORIA_ANTEPROYECTO = "CONVOCATORIA DE ANTEPROYECTO";
    private final String EMAIL_SUBJECT_CONVOCATORIA_EXAMEN_GENERAL = "CONVOCATORIA EXAMEN GENERAL";
    private final String EMAIL_SUBJECT_ACTA_ANTEPROYECTO = "ACTA DE ANTEPROYECTO";
    private final String EMAIL_SUBJECT_ACTA_EXAMEN_GENERAL = "ACTA DE EXAMEN GENERAL";
    private final String EMAIL_SUBJECT_ACTA_FINALIZACION = "ACTA DE FINALIZACION";
    private final String EMAIL_SUBJECT_BITACORA = "BITACORA";
    private final String EMAIL_SUBJECT_PRORROGA = "PRORROGA";

    private final EmailService emailService;
    private final ElementoService elementoService;

    public NotificacionServiceImp(@Value("${spring.profiles.active}") String activeProfile,
            EmailService emailService,
            ElementoService elementoService) {
        this.activeProfile = activeProfile;
        this.emailService = emailService;
        this.elementoService = elementoService;
    }

    @Async
    @Override
    public void notificarEtapaNuevo(Usuario usuario, EtapaProyecto etapaProyecto) {
        String mensaje = String.format(ETAPA_NUEVA_MSG, etapaProyecto.getIdEtapaFk().getNombre(), etapaProyecto.getIdEtapaFk().getDescripcion());
        String to = getEmailRecipient(usuario);
        sendNotificacionEmail(EMAIL_SUBJECT_ETAPA_NUEVA, etapaProyecto.getIdProyectoFk(), to, mensaje);
    }

    @Async
    @Override
    public void notificarAsignacionUsuarioProyecto(Usuario usuario, Rol rol, Proyecto proyecto) {
        String mensaje = String.format(ASIGNACION_MSG, rol.getTitulo());
        String to = getEmailRecipient(usuario);
        sendNotificacionEmail(EMAIL_SUBJECT_ASIGNACION_USUARIO, proyecto, to, mensaje);
    }

    @Async
    @Override
    public void notificarSolicitudRevision(Usuario usuario, Proyecto proyecto) {
        String to = getEmailRecipient(usuario);
        sendNotificacionEmail(EMAIL_SUBJECT_SOLICITUD_REVISION, proyecto, to, SOLICITUD_REVISION_MSG);
    }

    @Async
    @Override
    public void notificarSolicitudCambios(Usuario usuario, Proyecto proyecto, ComentarioDto comentarioDto) {
        String to = getEmailRecipient(usuario);
        String mensaje = String.format(SOLICITUD_CAMBIOS_MSG, comentarioDto.getComentario());
        sendNotificacionEmail(EMAIL_SUBJECT_SOLICITUD_CAMBIOS, proyecto, to, mensaje);
    }

    @Async
    @Override
    public void notificarFinalizacionProyecto(Usuario usuario, Proyecto proyecto, ComentarioDto comentarioDto) {
        String to = getEmailRecipient(usuario);
        String mensaje = String.format(FINALIZACION_PROYECTO_MSG, comentarioDto.getComentario());
        sendNotificacionEmail(EMAIL_SUBJECT_FINALIZACION_PROYECTO, proyecto, to, mensaje);
    }

    @Async
    @Override
    public void notificarGeneracionConvocatoria(Usuario usuario, Proyecto proyecto, String urlFile) {
        String to = getEmailRecipient(usuario);
        this.sendDocumentoEmail(proyecto, to, EMAIL_SUBJECT_CONVOCATORIA_ANTEPROYECTO, GENERACION_CONVOCATORIA_ANTEPROYECTO_MSG, urlFile);
    }

    @Async
    @Override
    public void notificarGeneracionConvocatoriaExamenGeneral(Usuario usuario, Proyecto proyecto, String urlFile) {
        String to = getEmailRecipient(usuario);
        this.sendDocumentoEmail(proyecto, to, EMAIL_SUBJECT_CONVOCATORIA_EXAMEN_GENERAL, GENERACION_CONVOCATORIA_EXAMEN_GENERAL_MSG, urlFile);
    }

    @Async
    @Override
    public void notificarConvocatoriaAnteproyecto(Usuario usuario, Proyecto proyecto, Convocatoria convocatoria, String urlFile) {
        String to = getEmailRecipient(usuario);
        String mensaje = String.format(CONVOCATORIA_ANTEPROYECTO_MSG, DateUtils.getFormatedDate(convocatoria.getFechaEvaluacion()),
                DateUtils.getFormatedTime(convocatoria.getHoraEvaluacion().toLocalTime()));
        this.sendDocumentoEmail(proyecto, to, EMAIL_SUBJECT_CONVOCATORIA_ANTEPROYECTO, mensaje, urlFile);
    }

    @Async
    @Override
    public void notificarConvocatoriaExamenGeneral(Usuario usuario, Proyecto proyecto, Convocatoria convocatoria, String urlFile) {
        String to = getEmailRecipient(usuario);
        String mensaje = String.format(CONVOCATORIA_EXAMEN_GENERAL_MSG, DateUtils.getFormatedDate(convocatoria.getFechaEvaluacion()),
                DateUtils.getFormatedTime(convocatoria.getHoraEvaluacion().toLocalTime()));
        this.sendDocumentoEmail(proyecto, to, EMAIL_SUBJECT_CONVOCATORIA_EXAMEN_GENERAL, mensaje, urlFile);
    }

    @Async
    @Override
    public void notificarCreacionActa(Usuario usuario, Proyecto proyecto) {
        String to = getEmailRecipient(usuario);
        this.sendNotificacionEmail(EMAIL_SUBJECT_ACTA_ANTEPROYECTO, proyecto, to, CREACION_ACTA_MSG);
    }

    @Async
    @Override
    public void notificarCreacionActaExamenGeneral(Usuario usuario, Proyecto proyecto) {
        String to = getEmailRecipient(usuario);
        this.sendNotificacionEmail(EMAIL_SUBJECT_ACTA_EXAMEN_GENERAL, proyecto, to, CREACION_ACTA_MSG);
    }

    @Async
    @Override
    public void notificarRegistroBitacora(Usuario usuario, Proyecto proyecto) {
        String to = getEmailRecipient(usuario);
        sendNotificacionEmail(EMAIL_SUBJECT_BITACORA, proyecto, to, REGISTRO_BITACORA_MSG);
    }

    @Async
    @Override
    public void notificarSolicitudProrroga(Usuario usuario, Proyecto proyecto) {
        String to = getEmailRecipient(usuario);
        sendNotificacionEmail(EMAIL_SUBJECT_PRORROGA, proyecto, to, SOLICITUD_PRORROGA_MSG);
    }

    @Async
    @Override
    public void notificarRespuestaProrroga(Usuario usuario, Proyecto proyecto) {
        String to = getEmailRecipient(usuario);
        sendNotificacionEmail(EMAIL_SUBJECT_PRORROGA, proyecto, to, RESPUESTA_PRORROGA_MSG);
    }

    @Async
    @Override
    public void notificarCreacionActaFinalizacion(Usuario usuario, Proyecto proyecto) {
        String to = getEmailRecipient(usuario);
        this.sendNotificacionEmail(EMAIL_SUBJECT_ACTA_FINALIZACION, proyecto, to, CREACION_ACTA_MSG);
    }

    private void sendDocumentoEmail(Proyecto proyecto, String to, String tituloDocumento, String mensaje, String fileUrl) {
        try {
            Usuario estudiate = proyecto.getIdUsuarioFk();
            String tituloProyecto = this.elementoService
                    .getElementoProyectoActivo(proyecto.getIdProyecto(), ElementoUtils.ID_ELEMENTO_TITULO).getInformacion();
            this.emailService.sendDocumentEmail(to, tituloDocumento, tituloProyecto, estudiate.getNombreCompleto(),
                    estudiate.getRegistroAcademico(), mensaje, fileUrl);
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }

    private void sendNotificacionEmail(String subject, Proyecto proyecto, String to, String mensaje) {
        try {
            Usuario estudiate = proyecto.getIdUsuarioFk();
            String tituloProyecto = this.elementoService
                    .getElementoProyectoActivo(proyecto.getIdProyecto(), ElementoUtils.ID_ELEMENTO_TITULO).getInformacion();
            this.emailService.sendNotificationEmail(subject, to, tituloProyecto, estudiate.getNombreCompleto(),
                    estudiate.getRegistroAcademico(), mensaje);
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }

    private String getEmailRecipient(Usuario usuario) {
        logger.info("---- env:"+activeProfile);
        if ("prod".equals(activeProfile)) {
            return usuario.getCorreo(); // Use predefined test email
        } else {
            return "edvinteodoro-gonzalezrafael@cunoc.edu.gt"; // Use real email for other profiles
        }
    }

}
