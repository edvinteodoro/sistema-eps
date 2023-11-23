/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.DepartamentoDto;
import gt.edu.cunoc.sistemaeps.service.DepartamentoService;
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
@RequestMapping("/api/municipios")
public class MunicipioController {
    
    private final DepartamentoService departamentoService;

    public MunicipioController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping("/{idMunicipio}/departamento")
    public ResponseEntity getDepartamento(@PathVariable Integer idMunicipio) {
        try {
            DepartamentoDto departamento = new DepartamentoDto(this.departamentoService.getDepartamento(idMunicipio));
            return ResponseEntity.ok(departamento);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
