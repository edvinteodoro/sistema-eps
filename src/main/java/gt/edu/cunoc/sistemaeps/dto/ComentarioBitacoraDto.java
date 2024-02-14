package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.ComentarioBitacora;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioBitacoraDto {

    private Integer idComentarioBitacora;
    private String comentario;
    private LocalDate fecha;
    private UsuarioDto usuario;
    private RolDto rol;

    public ComentarioBitacoraDto(ComentarioBitacora comentarioBitacora) {
        this.idComentarioBitacora = comentarioBitacora.getIdComentarioBitacora();
        this.comentario = comentarioBitacora.getComentario();
        this.fecha = comentarioBitacora.getFecha();
        this.usuario = new UsuarioDto(comentarioBitacora.getIdUsuarioFk());
        this.rol = new RolDto(comentarioBitacora.getIdRolFk());
    }
}
