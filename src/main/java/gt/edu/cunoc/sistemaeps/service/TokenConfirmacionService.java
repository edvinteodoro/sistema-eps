package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.entity.TokenConfirmacion;
import gt.edu.cunoc.sistemaeps.entity.Usuario;

/**
 *
 * @author edvin
 */
public interface TokenConfirmacionService {
    public TokenConfirmacion crearTockenConfirmacion(Usuario usuario) throws Exception;
    public TokenConfirmacion getTokenConfiramcion(String token) throws Exception;
    public void delete(TokenConfirmacion token);
}
