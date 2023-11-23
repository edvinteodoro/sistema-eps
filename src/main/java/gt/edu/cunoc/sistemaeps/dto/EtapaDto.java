package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Etapa;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class EtapaDto {
    private Integer idEtapa;
    private String nombre;

    public EtapaDto(Etapa etapa) {
        this.idEtapa = etapa.getIdEtapa();
        this.nombre = etapa.getNombre();
    }
    
    
}
