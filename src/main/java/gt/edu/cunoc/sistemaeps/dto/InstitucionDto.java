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
    private String direccion;
    private String coordenadaProyecto;
    private String direccionProyecto;
    private MunicipioDto municipio;
    private MunicipioDto municipioProyecto;

    public InstitucionDto(Institucion institucion) {
        this.idInstitucion = institucion.getIdInstitucion();
        this.nombre = institucion.getNombre();
        this.direccion = institucion.getDireccion();
        this.coordenadaProyecto = institucion.getCoordenadaProyecto();
        this.direccionProyecto = institucion.getDireccionProyecto();
        this.municipio = new MunicipioDto(institucion.getIdMunicipioFk());
        if (institucion.getIdMunicipioProyectoFk() != null) {
            this.municipioProyecto = new MunicipioDto(institucion.getIdMunicipioProyectoFk());
        }
    }
}
