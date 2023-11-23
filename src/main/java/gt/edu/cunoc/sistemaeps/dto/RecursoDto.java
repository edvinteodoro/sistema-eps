package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Recurso;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class RecursoDto {
    private Integer idRecurso;
    private String descripcion;
    private String link;
    private String tipoRecurso;

    public RecursoDto(Recurso recurso) {
        this.idRecurso=recurso.getIdRecurso();
        this.descripcion=recurso.getDescripcion();
        this.link=recurso.getLink();
        this.tipoRecurso=recurso.getTipoRecurso();
    }
    
}
