package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Departamento;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class DepartamentoDto {

    private Integer idDepartamento;
    private String nombre;

    public DepartamentoDto(Departamento departamento) {
        this.idDepartamento = departamento.getIdDepartamento();
        this.nombre = departamento.getNombre();
    }

}
