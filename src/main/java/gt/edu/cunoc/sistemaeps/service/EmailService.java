package gt.edu.cunoc.sistemaeps.service;

/**
 *
 * @author edvin
 */
public interface EmailService {
    public void sendConfirmationEmail(String to, String link);
    public void sendNotificationEmail(String to, String subject, String message, String fileUrl);
    public void sendDocumentEmail(String to, String subject,String estudiante, String registro, String carrera, String fileUrl);
}
