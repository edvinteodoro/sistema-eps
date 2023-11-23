/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Carrera;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */

@Data
@NoArgsConstructor
public class CarreraDto {
    private Integer idCarrera;
    private String nombre;
    private String nombreCorto;

    public CarreraDto(Carrera carrera) {
        this.idCarrera=carrera.getIdCarrera();
        this.nombreCorto=carrera.getNombreCorto();
        this.nombre = carrera.getNombre();
    }
}
