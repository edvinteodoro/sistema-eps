package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.BitacoraDto;
import gt.edu.cunoc.sistemaeps.dto.ComentarioBitacoraDto;
import gt.edu.cunoc.sistemaeps.dto.RecursoDto;
import gt.edu.cunoc.sistemaeps.service.BitacoraService;
import gt.edu.cunoc.sistemaeps.service.ComentarioBitacoraService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/api/bitacoras")
public class BitacoraController {

    private final BitacoraService bitacoraService;
    private final ComentarioBitacoraService comentarioBitacoraService;

    public BitacoraController(BitacoraService bitacoraService,
            ComentarioBitacoraService comentarioBitacoraService) {
        this.bitacoraService = bitacoraService;
        this.comentarioBitacoraService = comentarioBitacoraService;
    }

    @GetMapping
    public ResponseEntity getBitacoras(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String registroAcademico, Pageable pageable) {
        try {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "idBitacora"));
            Page<BitacoraDto> bitacoras = this.bitacoraService.getBitacoras(nombre,registroAcademico,pageable)
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

    @PutMapping("/{idBitacora}")
    public ResponseEntity actualizarBitacora(@PathVariable Integer idBitacora,
            @RequestBody BitacoraDto bitacoraDto) {
        try {
            BitacoraDto bitacora = new BitacoraDto(this.bitacoraService.actualizarBitacora(idBitacora,
                    bitacoraDto));
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

    @PostMapping("/{idBitacora}/recursos")
    public ResponseEntity crearRecursoBitacora(@PathVariable Integer idBitacora,
            @ModelAttribute RecursoDto recursoDto) {
        try {
            RecursoDto recurso = new RecursoDto(this.bitacoraService.crearRecursoBitacora(idBitacora, recursoDto));
            return ResponseEntity.ok(recurso);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{idBitacora}/recursos/{idRecurso}")
    public ResponseEntity eliminarRecursoBitacora(@PathVariable Integer idBitacora,
            @PathVariable Integer idRecurso) {
        try {
            BitacoraDto bitacora = new BitacoraDto(
                    this.bitacoraService.eliminarRecurso(idRecurso));
            return ResponseEntity.ok(bitacora);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idBitacora}/comentarios")
    public ResponseEntity getComentarios(@PathVariable Integer idBitacora,
            Pageable pageable) {
        try {

            Page<ComentarioBitacoraDto> comentarios = this.comentarioBitacoraService.getComentariosBitacora(idBitacora, pageable)
                    .map(ComentarioBitacoraDto::new);
            return ResponseEntity.ok(comentarios);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idBitacora}/comentarios")
    public ResponseEntity agregarComentario(@PathVariable Integer idBitacora,
            @RequestBody ComentarioBitacoraDto comentarioBitacoraDto) {
        try {
            ComentarioBitacoraDto comentario = new ComentarioBitacoraDto(
                    this.comentarioBitacoraService.crearComentario(idBitacora, comentarioBitacoraDto));
            return ResponseEntity.ok(comentario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idBitacora}/revisar")
    public ResponseEntity revisarBitacora(@PathVariable Integer idBitacora) {
        try {
            BitacoraDto bitacora = new BitacoraDto(this.bitacoraService.revisarBitacora(idBitacora));
            return ResponseEntity.ok(bitacora);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
