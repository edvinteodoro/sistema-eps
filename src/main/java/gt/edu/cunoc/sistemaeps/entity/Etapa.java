package gt.edu.cunoc.sistemaeps.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "etapa")
@Data
public class Etapa{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_etapa")
    private Integer idEtapa;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "nota_minima_aprobacion")
    private Double notaMinimaAprobacion;
    @JoinColumn(name = "id_rol_fk", referencedColumnName = "id_rol")
    @ManyToOne
    private Rol idRolFk;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEtapaFk")
    private List<EtapaProyecto> etapaProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEtapaFk")
    private List<Elemento> elementoList;    
}
