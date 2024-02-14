package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.ActaDto;
import gt.edu.cunoc.sistemaeps.service.ActaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/actas")
public class ActaController {

    private final ActaService actaService;

    public ActaController(ActaService actaService) {
        this.actaService = actaService;
    }

    @GetMapping
    public ResponseEntity getActas(Pageable pageable) {
        try {
            Page<ActaDto> actas = this.actaService.getActasAnteproyecto(pageable)
                    .map(ActaDto::new);
            return ResponseEntity.ok(actas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idActa}")
    public ResponseEntity getActa(@PathVariable Integer idActa) {
        try {
            ActaDto acta = new ActaDto(this.actaService.getActaAnteproyecto(idActa));
            return ResponseEntity.ok(acta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @PutMapping("/{idActa}")
    public ResponseEntity actualizarActa(@PathVariable Integer idActa,
            @RequestBody ActaDto actaDto) {
        try {
            ActaDto acta = new ActaDto(this.actaService.getActaAnteproyecto(idActa));
            return ResponseEntity.ok(acta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
