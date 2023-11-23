/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.ElementoProyecto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class ElementoProyectoDto {

    private Integer idElementoProyecto;
    private String informacion;
    private MultipartFile file;
    private ElementoDto elemento;
    private Boolean activo;

    public ElementoProyectoDto(ElementoProyecto elementoProyecto) {
        this.idElementoProyecto = elementoProyecto.getIdElementosProyecto();
        this.informacion = elementoProyecto.getInformacion();
        this.elemento = new ElementoDto(elementoProyecto.getIdElementoFk());
        this.activo = elementoProyecto.isActivo();
    }
}
