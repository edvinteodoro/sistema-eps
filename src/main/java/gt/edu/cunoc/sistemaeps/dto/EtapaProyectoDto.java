package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class EtapaProyectoDto {
    private Integer idEtapaProyecto;
    private Boolean editable;
    private Boolean activo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EtapaDto etapa;

    public EtapaProyectoDto(EtapaProyecto etapaProyecto) {
        this.idEtapaProyecto=etapaProyecto.getIdEtapaProyecto();
        this.editable = etapaProyecto.isEditable();
        this.activo = etapaProyecto.isActivo();
        this.fechaInicio = etapaProyecto.getFechaInicio();
        this.fechaFin = etapaProyecto.getFechaFin();
        this.etapa = new EtapaDto(etapaProyecto.getIdEtapaFk());
    }
    
    
}
