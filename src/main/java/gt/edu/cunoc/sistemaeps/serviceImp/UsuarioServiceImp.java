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
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.specification.UsuarioSpecification;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.dao.DataIntegrityViolationException;
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

    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})";

    private final UsuarioRepository usuarioRepository;
    private final TituloService tituloService;
    private final RolService rolService;
    private final CarreraService carreraService;
    private final TokenConfirmacionService tokenConfirmacionService;
    private final UsuarioProyectoService usuarioProyectoService;

    public UsuarioServiceImp(UsuarioRepository usuarioRepository,
            TituloService tituloService, RolService rolService,
            CarreraService carreraService,
            TokenConfirmacionService tokenConfirmacionService,
            EmailService emailService, UsuarioProyectoService usuarioProyectoService) {
        this.usuarioRepository = usuarioRepository;
        this.tituloService = tituloService;
        this.rolService = rolService;
        this.carreraService = carreraService;
        this.tokenConfirmacionService = tokenConfirmacionService;
        this.usuarioProyectoService = usuarioProyectoService;
    }

    public Usuario save(Usuario usuario) throws Exception {
        try {
            return this.usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException ex) {
            String message = ex.getMessage();
            if (ex.getMessage().contains("usuario.CORREO_UNIQUE")) {
                message = "El correo electrónico: \"" + usuario.getCorreo() + "\" ya está registrado con otro usuario.";
            } else if (ex.getMessage().contains("usuario.COLEGIADO_UNIQUE")) {
                message = "El numero de colegiado: \"" + usuario.getNumeroColegiado() + "\" ya está registrado con otro usuario.";
            } else if (ex.getMessage().contains("usuario.REGISTRO_UNIQUE")) {
                message = "El registro academico: \"" + usuario.getRegistroAcademico() + "\" ya está registrado con otro usuario.";
            } else if (ex.getMessage().contains("usuario.DPI_UNIQUE")) {
                message = "El dpi: \"" + usuario.getDpi() + "\" ya está registrado con otro usuario.";
            }
            throw new RuntimeException(message);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Usuario getUsuario(Integer idUsuario) throws Exception {
        return this.usuarioRepository.findById(idUsuario).get();
    }

    @Override
    public Page<Usuario> getAll(String nombre, String registroAcademico,
            String numeroColegiado, String dpi, Integer idRol, Pageable pageable) throws Exception {
        Specification<Usuario> spec = UsuarioSpecification.filterBy(nombre, registroAcademico, numeroColegiado, dpi, idRol);
        return this.usuarioRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Usuario actualizarUsuario(Integer idUsuario, UsuarioDto usuarioDto) throws Exception {
        Usuario usuario = this.usuarioRepository.findById(idUsuario).get();
        if (usuario.isCuentaActiva() && !validarUsuario(usuarioDto)) {
            throw new Exception("No se puede actualizar usuario, debido que existe un usuario activo con el mismo rol");
        }
        usuario.setNombreCompleto(usuarioDto.getNombreCompleto());
        usuario.setRegistroAcademico(usuarioDto.getRegistroAcademico());
        usuario.setCorreo(usuarioDto.getCorreo());
        usuario.setDireccion(usuarioDto.getDireccion());
        usuario.setDpi(usuarioDto.getDpi());
        usuario.setTelefono(usuarioDto.getTelefono());
        usuario.setNumeroColegiado(usuarioDto.getNumeroColegiado());
        if (usuarioDto.getRol() != null) {
            Rol rol = this.rolService.getRolUsuario(idUsuario).get(0).getIdRolFk();
            if (!rol.getIdRol().equals(usuarioDto.getRol().getIdRol())) {
                this.rolService.eliminarRol(usuario);
                usuario.setRolUsuarioList(asignarRol(usuario, usuarioDto.getRol()));
            }
        }
        if (usuarioDto.getCarreras() != null && !usuarioDto.getCarreras().isEmpty()) {
            this.carreraService.eliminarCarreras(usuario);
            usuario.setCarreraUsuarioList(asignarCarrera(usuario, usuarioDto.getCarreras()));
        }
        return save(usuario);
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

    private Boolean validarUsuario(UsuarioDto usuarioDto) throws Exception {
        if (usuarioDto.getNombreCompleto().isBlank() || usuarioDto.getCorreo().isBlank()
                || usuarioDto.getDpi().isBlank() || usuarioDto.getTelefono().isBlank()
                || usuarioDto.getRol() == null) {
            throw new Exception("Debe proporcionar los campos obligatorias");
        }
        if (usuarioDto.getRol().getIdRol().equals(RolUtils.ID_ROL_COORDINADOR_EPS)) {
            try {
                Usuario coordinadorEps = this.usuarioProyectoService.getCoordinadorEpsDisponible();
                if (coordinadorEps != null && !coordinadorEps.getIdUsuario().equals(usuarioDto.getIdUsuario())) {
                    return Boolean.FALSE;
                }
            } catch (Exception e) {
            }
        }
        if (usuarioDto.getRol().getIdRol().equals(RolUtils.ID_ROL_COORDINADOR_CARRERA)) {
            if (usuarioDto.getCarreras() == null) {
                throw new Exception("El usuario coordinador de carrera debe tener una carrera asignada.");
            }
            for (CarreraDto carreraDto : usuarioDto.getCarreras()) {
                Usuario coordinador = null;
                try {
                    coordinador = this.usuarioProyectoService.getCoordinadorCarreraDisponible(carreraDto.getIdCarrera());
                } catch (Exception e) {
                }
                if (coordinador != null) {
                    if (!coordinador.getIdUsuario().equals(usuarioDto.getIdUsuario())) {
                        return Boolean.FALSE;
                    }
                }
            }
        }
        if (usuarioDto.getRol().getIdRol().equals(RolUtils.ID_ROL_SUPERVISOR)) {
            if (usuarioDto.getCarreras() == null) {
                throw new Exception("El usuario Supervisor debe tener una carrera asignada.");
            }
            for (CarreraDto carreraDto : usuarioDto.getCarreras()) {
                Usuario supervisor = null;
                try {
                    supervisor = this.usuarioProyectoService.getSupervisorDisponible(carreraDto.getIdCarrera());
                } catch (Exception e) {
                }
                if (supervisor != null) {
                    if (!supervisor.getIdUsuario().equals(usuarioDto.getIdUsuario())) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        if (usuarioDto.getRol().getIdRol().equals(RolUtils.ID_ROL_SECRETARIA)) {
            Usuario secretaria = null;
            try {
                secretaria = this.usuarioProyectoService.getSecretariaDisponible();
            } catch (Exception e) {
            }
            if (secretaria != null && !secretaria.getIdUsuario().equals(usuarioDto.getIdUsuario())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Usuario crearUsuario(UsuarioDto usuarioDto) throws Exception {
        if (!validarUsuario(usuarioDto)) {
            throw new Exception("No se puede crear usuario, debido que ya existe un usuario activo en el mismo rol");
        }
        Usuario usuario = new Usuario(usuarioDto);
        Titulo titulo = this.tituloService.getTitulo(usuarioDto.getTitulo().getIdTitulo());
        usuario.setIdTituloFk(titulo);
        if (usuarioDto.getCarreras() != null && !usuarioDto.getCarreras().isEmpty()) {
            usuario.setCarreraUsuarioList(asignarCarrera(usuario, usuarioDto.getCarreras()));
        }
        usuario.setRolUsuarioList(asignarRol(usuario, usuarioDto.getRol()));
        usuario = save(usuario);
        TokenConfirmacion token = this.tokenConfirmacionService.crearTockenConfirmacion(usuario);
        return usuario;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activarUsuario(TokenConfirmacionDto tokenConfirmacionDto) throws Exception {
        TokenConfirmacion tokenConfirmacion = this.tokenConfirmacionService
                .getTokenConfiramcion(tokenConfirmacionDto.getToken());
        if (tokenConfirmacion==null){
            throw new Exception("No se pudo encontrar los datos del usuario");
        }
        Usuario usuario = tokenConfirmacion.getIdUsuarioFk();
        if (!validarUsuario(new UsuarioDto(usuario))) {
            throw new Exception("No se puede activar usuario, debido que ya existe un usuario activo en el mismo rol");
        }
        if (!tokenConfirmacionDto.getPassword1().equals(tokenConfirmacionDto.getPassword2())) {
            throw new Exception("Contrasenas no coinciden");
        }
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(tokenConfirmacionDto.getPassword1());
        if (!matcher.find()) {
            throw new Exception("La contraseña no cumple con los requisitos.");
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
        System.out.println("dto size: " + carrerasDto.size());
        for (CarreraDto carreraDto : carrerasDto) {
            Carrera carrera = this.carreraService.getCarrera(carreraDto.getIdCarrera());
            CarreraUsuario carreraUsuario = new CarreraUsuario();
            carreraUsuario.setIdCarreraFk(carrera);
            carreraUsuario.setIdUsuarioFk(usuario);
            carreras.add(carreraUsuario);
        }
        System.out.println("carrera fun size: " + carreras.size());

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Usuario desactivarUsuario(Integer idUsuario) throws Exception {
        Usuario usuario = getUsuario(idUsuario);
        usuario.setCuentaActiva(Boolean.FALSE);
        return save(usuario);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Usuario resetPassword(Integer idUsuario) throws Exception {
        Usuario usuario = getUsuario(idUsuario);
        usuario.setCuentaActiva(Boolean.FALSE);
        TokenConfirmacion token = this.tokenConfirmacionService.crearTockenConfirmacion(usuario);
        //this.notificacionService.notificarTokenUsuario(usuario, LINK_BASE + token.getToken());
        return save(usuario);
    }

}
