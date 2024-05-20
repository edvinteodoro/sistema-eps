package gt.edu.cunoc.sistemaeps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gt.edu.cunoc.sistemaeps.entity.Prorroga;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class ProrrogaDto {
    private Integer idProrroga;
    private String linkSolicitud;
    private String linkAmparo;
    private Integer diasExtension;
    private String comentarioSupervisor;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaSolicitud;
    private Boolean aprobado;
    private ProyectoDto proyecto;
    
    private MultipartFile solicitudFile;
    private MultipartFile amparoFile;

    public ProrrogaDto(Prorroga prorroga) {
        this.idProrroga = prorroga.getIdProrroga();
        this.linkSolicitud = prorroga.getLinkSolicitud();
        this.linkAmparo = prorroga.getLinkAmparo();
        this.diasExtension = prorroga.getDiasExtension();
        this.fechaSolicitud = prorroga.getFechaSolicitud();
        this.comentarioSupervisor = prorroga.getComentarioSupervisor();
        this.aprobado = prorroga.getAprobado();
        this.proyecto = new ProyectoDto(prorroga.getIdProyectoFk());
    }
    
    
}
