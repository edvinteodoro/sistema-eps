package gt.edu.cunoc.sistemaeps.service;

/**
 *
 * @author edvin
 */
public interface EmailService {
    public void sendConfirmationEmail(String to, String link);
}
