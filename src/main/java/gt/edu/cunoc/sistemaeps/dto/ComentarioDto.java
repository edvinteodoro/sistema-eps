package gt.edu.cunoc.sistemaeps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gt.edu.cunoc.sistemaeps.entity.Comentario;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class ComentarioDto {
    private Integer idComentario;
    private UsuarioDto usuario;
    private String comentario;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha;
    private RolDto rol;
    
    public ComentarioDto(Comentario comentario){
        this.idComentario = comentario.getIdComentario();
        this.usuario = new UsuarioDto(comentario.getIdUsuarioFk());
        this.comentario = comentario.getComentario();
        this.fecha=comentario.getFechaCreacion();
        this.rol = new RolDto(comentario.getIdRolFk());
    }
}
