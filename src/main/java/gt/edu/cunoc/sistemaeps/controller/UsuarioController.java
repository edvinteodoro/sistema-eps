package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.CarreraDto;
import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import gt.edu.cunoc.sistemaeps.service.CarreraService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edvin
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;
    private final CarreraService carreraService;
    private final UsuarioProyectoService usuarioProyectoService;

    public UsuarioController(UsuarioService usuarioService,
            CarreraService carreraService, UsuarioProyectoService usuarioProyectoService) {
        this.usuarioService = usuarioService;
        this.carreraService = carreraService;
        this.usuarioProyectoService = usuarioProyectoService;
    }

    @GetMapping
    public ResponseEntity getUsuarios(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String registroAcademico,
            @RequestParam(required = false) String colegiado,
            @RequestParam(required = false) String dpi,
            @RequestParam(required = false) Integer idRol,
            Pageable pageable) {
        try {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "idUsuario"));
            Page<UsuarioDto> usuarios = this.usuarioService.getAll(nombre, registroAcademico, colegiado, dpi, idRol, pageable)
                    .map(UsuarioDto::new);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity getUsuario(@PathVariable Integer idUsuario) {
        try {
            UsuarioDto usuario = new UsuarioDto(this.usuarioService.getUsuario(idUsuario));
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity actualizarUsuario(@PathVariable Integer idUsuario,
            @RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto usuario = new UsuarioDto(this.usuarioService.actualizarUsuario(idUsuario, usuarioDto));
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idUsuario}/reset-password")
    public ResponseEntity resetPassword(@PathVariable Integer idUsuario) {
        try {
            UsuarioDto usuario = new UsuarioDto(this.usuarioService.resetPassword(idUsuario));
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idUsuario}/desactivar")
    public ResponseEntity desactivarUsuario(@PathVariable Integer idUsuario) {
        try {
            UsuarioDto usuario = new UsuarioDto(this.usuarioService.desactivarUsuario(idUsuario));
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity crearUsuario(@RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto usuario = new UsuarioDto(this.usuarioService.crearUsuario(usuarioDto));
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/coordinador-eps")
    public ResponseEntity getCoordinadorEps() {
        try {

            UsuarioDto usuario = new UsuarioDto(this.usuarioProyectoService.getCoordinadorEpsDisponible());
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/actual/carreras")
    public ResponseEntity getCarrerasUsuario(Principal principal) {
        try {
            List<CarreraDto> carreras = this.carreraService.getCarrerasUsuario(principal.getName()).stream()
                    .map(carreraUsuario -> new CarreraDto(carreraUsuario.getIdCarreraFk())).collect(Collectors.toList());
            return ResponseEntity.ok(carreras);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
