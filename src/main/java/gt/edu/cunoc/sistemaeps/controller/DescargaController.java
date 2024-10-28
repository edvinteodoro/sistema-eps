package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edvin
 */
@RestController
@RequestMapping("/api/descarga")
public class DescargaController {
    
    private static final Logger logger = LoggerFactory.getLogger(ActaController.class);
    private final StorageService storageService;

    public DescargaController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @GetMapping
    public ResponseEntity descargar(@RequestParam String key){
        try {
            String link=this.storageService.getFile(key);
            return ResponseEntity.ok(link);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
