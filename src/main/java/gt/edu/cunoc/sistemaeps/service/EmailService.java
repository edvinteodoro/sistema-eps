package gt.edu.cunoc.sistemaeps.service;

/**
 *
 * @author edvin
 */
public interface EmailService {
    public void sendConfirmationEmail(String to, String link);
    public void sendNotificationEmail(String subject,String to, String tituloProyecto, String estudiante, String registroAcademico, String mensaje);
    public void sendDocumentEmail(String to, String tituloDocumento,String tituloProyecto,String estudiante, String registro, String mensaje, String fileUrl);
}
