/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Institucion;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class InstitucionDto {

    private Integer idInstitucion;
    private String nombre;
    private String coordenadas;
    private String direccion;
    private MunicipioDto municipio;

    public InstitucionDto(Institucion institucion) {
        this.idInstitucion = institucion.getIdInstitucion();
        this.nombre = institucion.getNombre();
        this.coordenadas = institucion.getCoordenadas();
        this.direccion = institucion.getDireccion();
        this.municipio = new MunicipioDto(institucion.getIdMunicipioFk());
    }
}
