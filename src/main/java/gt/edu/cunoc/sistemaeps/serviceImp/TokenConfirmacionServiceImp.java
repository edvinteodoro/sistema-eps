package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.TokenConfirmacion;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.TokenConfirmacionRepository;
import gt.edu.cunoc.sistemaeps.service.EmailService;
import gt.edu.cunoc.sistemaeps.service.NotificacionCuentaService;
import gt.edu.cunoc.sistemaeps.service.TokenConfirmacionService;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class TokenConfirmacionServiceImp implements TokenConfirmacionService {
    
    private final String LINK_BASE = " http://localhost:4200/auth/confirm?token=";

    private final TokenConfirmacionRepository tokenConfirmacionRepository;
    private final NotificacionCuentaService notificacionCuentaService;

    public TokenConfirmacionServiceImp(TokenConfirmacionRepository tokenConfirmacionRepository,
            NotificacionCuentaService notificacionCuentaService) {
        this.tokenConfirmacionRepository = tokenConfirmacionRepository;
        this.notificacionCuentaService = notificacionCuentaService;
    }

    public TokenConfirmacion save(TokenConfirmacion token) {
        return this.tokenConfirmacionRepository.save(token);
    }
    
    public void delete(TokenConfirmacion token) {
        this.tokenConfirmacionRepository.delete(token); 
    }
    
    public TokenConfirmacion getTokenConfirmacion(String token) {
        return this.tokenConfirmacionRepository.findTokenConfirmacion(token);
    }

    @Override
    public TokenConfirmacion crearTockenConfirmacion(Usuario usuario) throws Exception {
        TokenConfirmacion token = new TokenConfirmacion();
        token.setToken(generateToken());
        token.setIdUsuarioFk(usuario);
        this.notificacionCuentaService.notificarTokenUsuario(usuario, LINK_BASE+ token.getToken());
        return save(token);
    }

    @Override
    public TokenConfirmacion getTokenConfiramcion(String token) throws Exception {
        TokenConfirmacion tokenConfirmacion = getTokenConfirmacion(token);
        if(token==null){
            throw new Exception("Token Invalido");
        }
        //validar fecha
        return tokenConfirmacion;
    }

    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
