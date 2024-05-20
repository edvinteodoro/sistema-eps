package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.dto.ProrrogaDto;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "prorroga")
@Data
@NoArgsConstructor
public class Prorroga{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_prorroga")
    private Integer idProrroga;
    @Basic(optional = false)
    @Column(name = "link_solicitud")
    private String linkSolicitud;
    @Basic(optional = false)
    @Column(name = "link_amparo")
    private String linkAmparo;
    @Basic(optional = false)
    @Column(name = "dias_extension")
    private Integer diasExtension;
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaSolicitud;
    @Column(name = "comentario_supervisor")
    private String comentarioSupervisor;
    @Column(name = "aprobado")
    private Boolean aprobado;
    @JoinColumn(name = "id_proyecto_fk", referencedColumnName = "id_proyecto")
    @ManyToOne(optional = false)
    private Proyecto idProyectoFk;

}
