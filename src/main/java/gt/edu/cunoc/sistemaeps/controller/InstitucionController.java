package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.InstitucionDto;
import gt.edu.cunoc.sistemaeps.service.InstitucionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edvin
 */
@RestController
@RequestMapping("/api/instituciones")
public class InstitucionController {

    private static final Logger logger = LoggerFactory.getLogger(InstitucionController.class);
    private final InstitucionService institucionService;

    public InstitucionController(InstitucionService institucionService) {
        this.institucionService = institucionService;
    }

    @PutMapping("/{idInstitucion}")
    public ResponseEntity actualizarInstitucion(@PathVariable Integer idInstitucion,
            @RequestBody InstitucionDto institucionDto) {
        try {
            InstitucionDto institucion = new InstitucionDto(
                    this.institucionService.actualizarInstitucion(idInstitucion, institucionDto));
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
