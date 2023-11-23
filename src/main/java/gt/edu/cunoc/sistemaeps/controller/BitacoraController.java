package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.BitacoraDto;
import gt.edu.cunoc.sistemaeps.dto.RecursoDto;
import gt.edu.cunoc.sistemaeps.service.BitacoraService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/bitacoras")
public class BitacoraController {

    private final BitacoraService bitacoraService;

    public BitacoraController(BitacoraService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    @GetMapping
    public ResponseEntity getBitacoras(Pageable pageable) {
        try {
            Page<BitacoraDto> bitacoras = this.bitacoraService.getBitacoras(pageable)
                    .map(BitacoraDto::new);
            return ResponseEntity.ok(bitacoras);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idBitacora}")
    public ResponseEntity getBitacora(@PathVariable Integer idBitacora) {
        try {
            BitacoraDto bitacora = new BitacoraDto(this.bitacoraService.getBitacora(idBitacora));
            return ResponseEntity.ok(bitacora);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idBitacora}/recursos")
    public ResponseEntity getRecursosBitacora(@PathVariable Integer idBitacora) {
        try {
            List<RecursoDto> recursos = this.bitacoraService.getRecursosBitacora(idBitacora)
                    .stream().map(RecursoDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(recursos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
