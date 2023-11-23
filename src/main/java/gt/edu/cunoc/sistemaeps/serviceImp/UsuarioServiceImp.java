package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.CarreraDto;
import gt.edu.cunoc.sistemaeps.dto.RolDto;
import gt.edu.cunoc.sistemaeps.dto.TokenConfirmacionDto;
import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import gt.edu.cunoc.sistemaeps.entity.Carrera;
import gt.edu.cunoc.sistemaeps.entity.CarreraUsuario;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.RolUsuario;
import gt.edu.cunoc.sistemaeps.entity.Titulo;
import gt.edu.cunoc.sistemaeps.entity.TokenConfirmacion;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.UsuarioRepository;
import gt.edu.cunoc.sistemaeps.service.CarreraService;
import gt.edu.cunoc.sistemaeps.service.EmailService;
import gt.edu.cunoc.sistemaeps.service.RolService;
import gt.edu.cunoc.sistemaeps.service.TituloService;
import gt.edu.cunoc.sistemaeps.service.TokenConfirmacionService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.specification.UsuarioSpecification;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author edvin
 */
@Service
public class UsuarioServiceImp implements UsuarioService {

    private final String LINK_BASE = " http://localhost:4200/#/auth/confirm?token=";

    private final UsuarioRepository usuarioRepository;
    private final TituloService tituloService;
    private final RolService rolService;
    private final CarreraService carreraService;
    private final TokenConfirmacionService tokenConfirmacionService;
    private final EmailService emailService;

    public UsuarioServiceImp(UsuarioRepository usuarioRepository,
            TituloService tituloService, RolService rolService,
            CarreraService carreraService,
            TokenConfirmacionService tokenConfirmacionService,
            EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.tituloService = tituloService;
        this.rolService = rolService;
        this.carreraService = carreraService;
        this.tokenConfirmacionService = tokenConfirmacionService;
        this.emailService = emailService;
    }

    public Usuario save(Usuario usuario) throws Exception {
        return this.usuarioRepository.save(usuario);
    }

    @Override
    public Usuario getUsuario(Integer idUsuario) throws Exception {
        return this.usuarioRepository.findById(idUsuario).get();
    }

    @Override
    public Page<Usuario> getAll(String nombre, String registroAcademico,
            String numeroColegiado, Pageable pageable) throws Exception {
        Specification<Usuario> spec = UsuarioSpecification.filterBy(nombre, registroAcademico, numeroColegiado);
        return this.usuarioRepository.findAll(spec, pageable);
    }

    @Override
    public Usuario getUsuario(String username) throws Exception {
        Optional<Usuario> usuario = this.usuarioRepository.findUsuarioByRegistroAcademico(username);
        if (usuario.isEmpty()) {
            usuario = this.usuarioRepository.findUsuarioByNumeroColegiado(username);
        }
        if (usuario.isEmpty()) {
            usuario = this.usuarioRepository.findUsuarioByDpi(username);
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario crearUsuario(UsuarioDto usuarioDto) throws Exception {
        Usuario usuario = new Usuario(usuarioDto);
        Titulo titulo = this.tituloService.getTitulo(usuarioDto.getTitulo().getIdTitulo());
        usuario.setIdTituloFk(titulo);
        usuario.setCarreraUsuarioList(asignarCarrera(usuario, usuarioDto.getCarreras()));
        usuario.setRolUsuarioList(asignarRol(usuario, usuarioDto.getRol()));
        usuario = save(usuario);
        TokenConfirmacion token = this.tokenConfirmacionService.crearTockenConfirmacion(usuario);
        this.emailService.sendConfirmationEmail("edvinteodoro-gonzalezrafael@cunoc.edu.gt", LINK_BASE + token.getToken());
        return usuario;
    }

    @Override
    @Transactional
    public void activarUsuario(TokenConfirmacionDto tokenConfirmacionDto) throws Exception {
        TokenConfirmacion tokenConfirmacion = this.tokenConfirmacionService
                .getTokenConfiramcion(tokenConfirmacionDto.getToken());
        Usuario usuario = tokenConfirmacion.getIdUsuarioFk();
        if (!tokenConfirmacionDto.getPassword1().equals(tokenConfirmacionDto.getPassword2())) {
            throw new Exception("Contrasenas no coinciden");
        }
        usuario.setPassword(getEncodedPassword(tokenConfirmacionDto.getPassword1()));
        usuario.setCuentaActiva(Boolean.TRUE);
        this.tokenConfirmacionService.delete(tokenConfirmacion);
        save(usuario);
    }

    private String getEncodedPassword(String pass) {
        return new BCryptPasswordEncoder().encode(pass);
    }

    private List<CarreraUsuario> asignarCarrera(Usuario usuario, List<CarreraDto> carrerasDto) throws Exception {
        List<CarreraUsuario> carreras = new ArrayList<>();
        for (CarreraDto carreraDto : carrerasDto) {
            Carrera carrera = this.carreraService.getCarrera(carreraDto.getIdCarrera());
            CarreraUsuario carreraUsuario = new CarreraUsuario();
            carreraUsuario.setIdCarreraFk(carrera);
            carreraUsuario.setIdUsuarioFk(usuario);
            carreras.add(carreraUsuario);
        }
        return carreras;
    }

    private List<RolUsuario> asignarRol(Usuario usuario, RolDto rolDto) {
        List<RolUsuario> rolUsuarios = new ArrayList<>();
        Rol rol = this.rolService.getRol(rolDto.getIdRol());
        RolUsuario rolUsuario = new RolUsuario();
        rolUsuario.setIdRolFk(rol);
        rolUsuario.setIdUsuarioFk(usuario);
        rolUsuarios.add(rolUsuario);
        return rolUsuarios;

    }

    @Override
    public Usuario getLoggedUsuario() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String username = authentication.getName();
            Usuario usuario = getUsuario(username);
            return usuario;
        } else {
            throw new Exception("El usuario no esta autenticado");
        }
    }

}
