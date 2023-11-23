/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Titulo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class TituloDto {

    private Integer idTitulo;
    private String nombre;
    private String abreviatura;

    public TituloDto(Titulo titulo) {
        this.idTitulo = titulo.getIdTitulo();
        this.nombre = titulo.getNombre();
        this.abreviatura = titulo.getAbreviatura();
    }

}
