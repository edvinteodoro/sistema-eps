package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Rol;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class RolDto {

    private Integer idRol;
    private String titulo;
    private Boolean contieneCarrera;
    private Boolean contieneRegistro;
    private Boolean contieneColegiado;

    public RolDto(Rol rol) {
        this.idRol = rol.getIdRol();
        this.titulo = rol.getTitulo();
        this.contieneCarrera = rol.getContieneCarrera();
        this.contieneRegistro = rol.getContieneRegistro();
        this.contieneColegiado = rol.getContieneColegiado();
    }
}
