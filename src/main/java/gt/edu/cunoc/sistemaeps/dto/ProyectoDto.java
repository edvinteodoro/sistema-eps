package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class ProyectoDto {

    private Integer idProyecto;
    private String semestre;
    private InstitucionDto institucion;
    private EtapaDto etapaActiva;
    private UsuarioDto usuario;
    private UsuarioDto asesor;
    private UsuarioDto contraparte;
    private CarreraDto carrera;
    private Boolean activo;
    private Boolean enEspera;

    public ProyectoDto(Proyecto proyecto) {
        this.idProyecto = proyecto.getIdProyecto();
        this.institucion = new InstitucionDto(proyecto.getIdInstitucionFk());
        this.usuario = new UsuarioDto(proyecto.getIdUsuarioFk());
        this.carrera = new CarreraDto(proyecto.getIdCarreraFk());
        this.activo = proyecto.isActivo();
        this.semestre = proyecto.getSemestre();
        if (proyecto.getEtapaProyectoList() != null) {
            Optional<EtapaDto> etapa = proyecto.getEtapaProyectoList().stream().filter(EtapaProyecto
                    -> EtapaProyecto.isActivo()).map(ep -> new EtapaDto(ep.getIdEtapaFk())).findFirst();
            if (etapa.isPresent()) {
                this.etapaActiva = etapa.get();
            }
        }
    }
}
