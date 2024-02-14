/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.TituloDto;
import gt.edu.cunoc.sistemaeps.service.TituloService;
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
@RequestMapping("/api/titulos")
public class TituloController {

    private final TituloService tituloService;

    public TituloController(TituloService tituloService) {
        this.tituloService = tituloService;
    }

    @GetMapping
    public ResponseEntity getTitulos() {
        try {
            List<TituloDto> titulos = this.tituloService.getTitulos().stream()
                    .map(TituloDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(titulos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @GetMapping("/{idTitulo}")
    public ResponseEntity getTitulo(@PathVariable Integer idTitulo) {
        try {
            TituloDto titulo = new TituloDto(this.tituloService.getTitulo(idTitulo));
            return ResponseEntity.ok(titulo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
