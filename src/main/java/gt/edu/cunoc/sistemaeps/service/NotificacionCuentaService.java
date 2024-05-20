package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.entity.Usuario;

/**
 *
 * @author edvin
 */
public interface NotificacionCuentaService {
    public void notificarTokenUsuario(Usuario usuario,String link); 
}
