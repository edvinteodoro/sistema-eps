package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Recurso;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class RecursoDto {
    private Integer idRecurso;
    private String descripcion;
    private MultipartFile file;
    private String link;
    private String icono;
    private String tipoRecurso;

    public RecursoDto(Recurso recurso) {
        this.idRecurso=recurso.getIdRecurso();
        this.descripcion=recurso.getDescripcion();
        this.link=recurso.getLink();
        this.tipoRecurso=recurso.getTipoRecurso();
        if(this.tipoRecurso.equals("LINK")){
            this.icono = "pi pi-link";
        }else if(this.tipoRecurso.equals("INFORME MENSUAL")){
            this.icono = "pi pi-book";
        }else if (this.tipoRecurso.equals("IMAGEN")){
            this.icono = "pi pi-image";
        }else{
            this.icono = "pi pi-file";
        }
    }
    
}
