package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.dto.RecursoDto;
import gt.edu.cunoc.sistemaeps.util.DateUtils;
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
@Table(name = "recurso")
@Data
@NoArgsConstructor
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_recurso")
    private Integer idRecurso;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "link")
    private String link;
    @Basic(optional = false)
    @Column(name = "tipo_recurso")
    private String tipoRecurso;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @JoinColumn(name = "id_bitacora_fk", referencedColumnName = "id_bitacora")
    @ManyToOne(optional = false)
    private Bitacora idBitacoraFk;
    
    public Recurso(RecursoDto recursoDto){
        this.fecha=DateUtils.getCurrentDate();
        this.tipoRecurso = recursoDto.getTipoRecurso();
    }
}
