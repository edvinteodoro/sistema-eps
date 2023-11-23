package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Elemento;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class ElementoDto {

    private Integer idElemento;
    private String nombre;
    private String nombreArchivo;
    private String tipo;

    public ElementoDto(Elemento elemento) {
        this.idElemento = elemento.getIdElemento();
        this.nombre = elemento.getNombre();
        this.nombreArchivo=elemento.getNombreArchivo();
        this.tipo=elemento.getTipo();
    }

}
