package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.net.URL;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 *
 * @author edvin
 */
@Service
public class EmailServiceImp implements EmailService {

    private final String ACTIVAR_CUENTA_TEMPLATE = "activar-cuenta";
    private final String NOTIFICACION_TEMPLATE = "notificacion";
    private final String DOCUMENTO_GENERADO_TEMPLATE = "documento-generado";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailServiceImp(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }
    
    
    @Override
    //@Async
    public void sendDocumentEmail(String to, String tituloDocumento,String tituloProyecto,String estudiante, String registro, String carrera, String fileUrl){
        try {
            Context context = new Context();
            context.setVariable("titulo_documento", tituloDocumento);
            context.setVariable("titulo_proyecto", tituloProyecto);
            context.setVariable("nombre_estudiante", estudiante);
            context.setVariable("registro_academico", registro);
            context.setVariable("carrera", carrera);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(tituloDocumento);

            String htmlContent = templateEngine.process(DOCUMENTO_GENERADO_TEMPLATE, context);
            helper.setText(htmlContent, true);

            // Attach file from URL
            Resource resource = new UrlResource(new URL(fileUrl));
            helper.addAttachment(resource.getFilename(), resource);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e);
        } catch (Exception e) {
            System.out.println("Error attaching file: " + e);
        }
    }

    @Override
    //@Async
    public void sendConfirmationEmail(String to, String link) {
        try {
            Context context = new Context();
            context.setVariable("link", link);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Activar Cuenta");
            String htmlContent = templateEngine.process(ACTIVAR_CUENTA_TEMPLATE, context);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("error email: " + e);
        }
    }

    public void sendNotificacion(String subject, String message, String fileUrl, String... emails) {
        for (String email : emails) {
            sendNotificacion(email, subject, message, fileUrl);
        }
    }

    //@Async
    @Override
    public void sendNotificationEmail(String subject,String to, String tituloProyecto, String estudiante, String registroAcademico, String mensaje) {
        try {
            Context context = new Context();
            context.setVariable("titulo_proyecto", tituloProyecto);
            context.setVariable("nombre_estudiante", estudiante);
            context.setVariable("registro_academico", registroAcademico);
            context.setVariable("mensaje", mensaje);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            String htmlContent = templateEngine.process(NOTIFICACION_TEMPLATE, context);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e);
        } catch (Exception e) {
            System.out.println("Error attaching file: " + e);
        }
    }
}
