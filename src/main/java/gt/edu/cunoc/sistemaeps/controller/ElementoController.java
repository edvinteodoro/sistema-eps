/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.service.ElementoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edvin
 */
@RestController
@RequestMapping("/api/elementos")
public class ElementoController {

    private final ElementoService elementoService;

    public ElementoController(ElementoService elementoService) {
        this.elementoService = elementoService;
    }

    @PutMapping("/{idElementoProyecto}/desactivar")
    public ResponseEntity desactivarElementoProyecto(@PathVariable Integer idElementoProyecto) {
        try {
            this.elementoService.desactivarElementoProyectoActivo(idElementoProyecto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
