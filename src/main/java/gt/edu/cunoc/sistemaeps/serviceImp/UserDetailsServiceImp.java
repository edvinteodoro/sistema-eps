package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.RolUsuario;
import gt.edu.cunoc.sistemaeps.jwt.UsuarioDetails;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.UsuarioRepository;
import gt.edu.cunoc.sistemaeps.service.RolService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;

    public UserDetailsServiceImp(UsuarioRepository usuarioRepository,
            RolService rolService) {
        this.usuarioRepository = usuarioRepository;
        this.rolService = rolService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = this.usuarioRepository.findUsuarioByRegistroAcademico(username);
        if (usuario.isEmpty()) {
            usuario = this.usuarioRepository.findUsuarioByNumeroColegiado(username);
        }
        if (usuario.isEmpty()) {
            usuario = this.usuarioRepository.findUsuarioByDpi(username);
        }
        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("No se encontro el usuario: " + username);
        }
        List<RolUsuario> roles = this.rolService.getRolUsuario(usuario.get().getIdUsuario());
        return new UsuarioDetails(username, usuario.get().getPassword(), usuario.get().isCuentaActiva(), roles);
    }

}
