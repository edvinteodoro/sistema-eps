/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.ElementoProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */

@Data
@NoArgsConstructor
public class ProyectoDto {
    private Integer idProyecto;
    private String tipoProyecto;
    private InstitucionDto institucion;
    private UsuarioDto usuario;
    private UsuarioDto asesor;
    private UsuarioDto contraparte;
    private CarreraDto carrera;
    private Boolean activo;
    
    public ProyectoDto(Proyecto proyecto){
        this.idProyecto=proyecto.getIdProyecto();
        this.institucion=new InstitucionDto(proyecto.getIdInstitucionFk());
        this.usuario=new UsuarioDto(proyecto.getIdUsuarioFk());
        this.carrera=new CarreraDto(proyecto.getIdCarreraFk());
        this.activo = proyecto.isActivo();
    }
}
