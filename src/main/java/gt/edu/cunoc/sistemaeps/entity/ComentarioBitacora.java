package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.dto.ComentarioBitacoraDto;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "comentario_bitacora")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioBitacora{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_comentario_bitacora")
    private Integer idComentarioBitacora;
    @Basic(optional = false)
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @JoinColumn(name = "id_bitacora_fk", referencedColumnName = "id_bitacora")
    @ManyToOne(optional = false)
    private Bitacora idBitacoraFk;
    @JoinColumn(name = "id_rol_fk", referencedColumnName = "id_rol")
    @ManyToOne(optional = false)
    private Rol idRolFk;
    @JoinColumn(name = "id_usuario_fk", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioFk;
    
    public ComentarioBitacora(ComentarioBitacoraDto comentarioBitacoraDto){
        this.comentario = comentarioBitacoraDto.getComentario();
        this.fecha = DateUtils.getCurrentDate();
    }
}
