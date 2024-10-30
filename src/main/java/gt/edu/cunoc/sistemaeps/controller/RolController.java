package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.RolDto;
import gt.edu.cunoc.sistemaeps.service.RolService;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edvin
 */
@RestController
@RequestMapping("/api/roles")
public class RolController {

    private static final Logger logger = LoggerFactory.getLogger(RolController.class);
    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public ResponseEntity getRoles() {
        try {
            List<RolDto> roles = this.rolService.getAll().stream()
                    .map(RolDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping("/{idRol}")
    public ResponseEntity getRol(@PathVariable Integer idRol) {
        try {
            RolDto rol = new RolDto(this.rolService.getRol(idRol));
            return ResponseEntity.ok(rol);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.ok(e.getMessage());
        }
    }
}
