package gt.edu.cunoc.sistemaeps.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class BitacoraDto {

    private Integer idBitacora;
    private String descripcion;
    private Integer avance;
    private UsuarioDto usuario;
    private CarreraDto carrera;
    private Integer idProyecto;
    private LocalDate fechaReporte;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaReporteFormat;
    private LocalDate fecha;
    private Boolean revisionAsesor;
    private Boolean revisionSupervisor;
    private Boolean revisionContraparte;
    private Boolean contieneInforme;

    public BitacoraDto(Bitacora bitacora) {
        this.idBitacora = bitacora.getIdBitacora();
        this.descripcion = bitacora.getDescripcion();
        this.avance = bitacora.getAvance();
        this.fechaReporte = bitacora.getFechaReporte();
        this.fechaReporteFormat = bitacora.getFechaReporte();
        this.fecha = bitacora.getFecha();
        this.revisionAsesor = bitacora.isRevisionAsesor();
        this.revisionSupervisor = bitacora.isRevisionSupervisor();
        this.revisionContraparte = bitacora.isRevisionContraparte();
        this.contieneInforme = bitacora.isContieneInforme();
        this.usuario = new UsuarioDto(bitacora.getIdProyectoFk().getIdUsuarioFk());
        this.idProyecto = bitacora.getIdProyectoFk().getIdProyecto();
        this.carrera = new CarreraDto(bitacora.getIdProyectoFk().getIdCarreraFk());
    }

}
