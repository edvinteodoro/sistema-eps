package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.ActaDto;
import gt.edu.cunoc.sistemaeps.service.ActaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edvin
 */
@RestController
@RequestMapping("/api/actas")
public class ActaController {
    private static final Logger logger = LoggerFactory.getLogger(ActaController.class);
    private final ActaService actaService;

    public ActaController(ActaService actaService) {
        this.actaService = actaService;
    }

    @GetMapping
    public ResponseEntity getActas(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String registroAcademico,
            Pageable pageable) {
        try {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "idActa"));
            Page<ActaDto> actas = this.actaService.getActas(nombre, registroAcademico, pageable)
                    .map(ActaDto::new);
            return ResponseEntity.ok(actas);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idActa}")
    public ResponseEntity getActa(@PathVariable Integer idActa) {
        try {
            ActaDto acta = new ActaDto(this.actaService.getActa(idActa));
            return ResponseEntity.ok(acta);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /*
    @PutMapping("/{idActa}")
    public ResponseEntity actualizarActa(@PathVariable Integer idActa,
            @RequestBody ActaDto actaDto) {
        try {
            ActaDto acta = new ActaDto(this.actaService.getActaAnteproyecto(idActa));
            return ResponseEntity.ok(acta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }*/
}
