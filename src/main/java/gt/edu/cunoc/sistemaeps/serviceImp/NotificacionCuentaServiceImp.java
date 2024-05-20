package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.service.EmailService;
import gt.edu.cunoc.sistemaeps.service.NotificacionCuentaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class NotificacionCuentaServiceImp implements NotificacionCuentaService{

    @Value("${spring.profiles.active}")
    private String activeProfile;
    
    private final EmailService emailService;
    
    public NotificacionCuentaServiceImp(EmailService emailService){
        this.emailService = emailService;
    }
    
    @Async
    @Override
    public void notificarTokenUsuario(Usuario usuario, String link) {
        String to = getEmailRecipient(usuario);
        this.emailService.sendConfirmationEmail(to, link);
    }
    
    private String getEmailRecipient(Usuario usuario) {
        if ("DEV".equals(activeProfile)) {
            return "edvinteodoro-gonzalezrafael@cunoc.edu.gt"; // Use predefined test email
        } else {
            return usuario.getCorreo(); // Use real email for other profiles
        }
    }
    
}
