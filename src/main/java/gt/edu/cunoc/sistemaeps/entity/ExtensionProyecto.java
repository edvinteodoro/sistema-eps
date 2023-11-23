package gt.edu.cunoc.sistemaeps.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "extension_proyecto")
@Data
public class ExtensionProyecto{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_extension_proyecto")
    private Integer idExtensionProyecto;
    @Basic(optional = false)
    @Column(name = "link_solicitud")
    private String linkSolicitud;
    @Column(name = "aprobado")
    private Boolean aprobado;
    @JoinColumn(name = "id_proyecto_fk", referencedColumnName = "id_proyecto")
    @ManyToOne(optional = false)
    private Proyecto idProyectoFk;    
}
