package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.RolDto;
import gt.edu.cunoc.sistemaeps.service.RolService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edvin
 */
@RestController
@RequestMapping("/api/roles")
public class RolController {
    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }
    
    @GetMapping
    public ResponseEntity getRoles(){
        try {
            List<RolDto> roles=this.rolService.getAll().stream()
                    .map(RolDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }
}
