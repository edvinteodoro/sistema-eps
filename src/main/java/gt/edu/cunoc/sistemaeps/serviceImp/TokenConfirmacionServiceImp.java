package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.TokenConfirmacion;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.TokenConfirmacionRepository;
import gt.edu.cunoc.sistemaeps.service.TokenConfirmacionService;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class TokenConfirmacionServiceImp implements TokenConfirmacionService {

    private final TokenConfirmacionRepository tokenConfirmacionRepository;

    public TokenConfirmacionServiceImp(TokenConfirmacionRepository tokenConfirmacionRepository) {
        this.tokenConfirmacionRepository = tokenConfirmacionRepository;
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
