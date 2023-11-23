/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Municipio;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class MunicipioDto {
    private Integer idMunicipio;
    private String nombre;
    private DepartamentoDto departamento;

    public MunicipioDto(Municipio municipio) {
        this.idMunicipio=municipio.getIdMunicipio();
        this.nombre=municipio.getNombre();
        this.departamento=new DepartamentoDto(municipio.getIdDepartamentoFk());
    }
}
