package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.DepartamentoDto;
import gt.edu.cunoc.sistemaeps.dto.MunicipioDto;
import gt.edu.cunoc.sistemaeps.service.DepartamentoService;
import java.util.List;
import java.util.stream.Collectors;
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
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public ResponseEntity getDepartamentos() {
        try {
            List<DepartamentoDto> departamentos = this.departamentoService.getDepartamentos().stream()
                    .map(DepartamentoDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(departamentos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @GetMapping("/{idDepartamento}/municipios")
    public ResponseEntity getMunicipios(@PathVariable Integer idDepartamento) {
        try {
            List<MunicipioDto> municipios = this.departamentoService.getMunicipios(idDepartamento).stream()
                    .map(MunicipioDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(municipios);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
