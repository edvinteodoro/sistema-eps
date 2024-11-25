package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.ProrrogaDto;
import gt.edu.cunoc.sistemaeps.service.ProrrogaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/api/prorrogas")
public class ProrrogaController {

    private static final Logger logger = LoggerFactory.getLogger(ProrrogaController.class);
    private final ProrrogaService prorrogaService;

    public ProrrogaController(ProrrogaService prorrogaService) {
        this.prorrogaService = prorrogaService;
    }

    @GetMapping
    public ResponseEntity getProrrogas(Pageable pageable) {
        try {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "idProrroga"));
            Page<ProrrogaDto> prorrogas = this.prorrogaService.getProrrogas(pageable)
                    .map(ProrrogaDto::new);
            System.out.println("prorrogas: "+prorrogas);
            return ResponseEntity.ok(prorrogas);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProrroga}")
    public ResponseEntity getProrroga(@PathVariable Integer idProrroga) {
        try {
            ProrrogaDto prorroga = new ProrrogaDto(this.prorrogaService.getProrroga(idProrroga));
            return ResponseEntity.ok(prorroga);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idProrroga}")
    public ResponseEntity actualizarProrroga(@PathVariable Integer idProrroga,
            @ModelAttribute ProrrogaDto prorrogaDto) {
        try {
            ProrrogaDto prorroga = new ProrrogaDto(this.prorrogaService.actualizarProrroga(idProrroga, prorrogaDto));
            return ResponseEntity.ok(prorroga);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idProrroga}/responder")
    public ResponseEntity responderProrroga(@PathVariable Integer idProrroga,
            @RequestBody ProrrogaDto prorrogaDto) {
        try {
            ProrrogaDto prorroga = new ProrrogaDto(this.prorrogaService.responderProrroga(idProrroga, prorrogaDto));
            return ResponseEntity.ok(prorroga);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
