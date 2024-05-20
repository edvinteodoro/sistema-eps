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
    private Boolean contieneTitulo;
    private Boolean contieneMultiplesCarreras;

    public RolDto(Rol rol) {
        this.idRol = rol.getIdRol();
        this.titulo = rol.getTitulo();
        this.contieneCarrera = rol.getContieneCarrera();
        this.contieneRegistro = rol.getContieneRegistro();
        this.contieneColegiado = rol.getContieneColegiado();
        this.contieneTitulo=rol.getContieneTitulo();
        this.contieneMultiplesCarreras = rol.getContieneMultiplesCarreras();
    }
}
