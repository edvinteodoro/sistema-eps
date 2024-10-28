package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.CarreraDto;
import gt.edu.cunoc.sistemaeps.service.CarreraService;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edvin
 */
@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    private static final Logger logger = LoggerFactory.getLogger(ActaController.class);
    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public ResponseEntity getCarreras() {
        try {
            List<CarreraDto> carreras = this.carreraService.getCarreras().stream()
                    .map(CarreraDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(carreras);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
