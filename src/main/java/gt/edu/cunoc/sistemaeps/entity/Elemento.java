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
@Table(name = "elemento")
@Data
public class Elemento{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_elemento")
    private Integer idElemento;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "template")
    private String template;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idElementoFk")
    private List<ElementoProyecto> elementoProyectoList;
    @JoinColumn(name = "id_etapa_fk", referencedColumnName = "id_etapa")
    @ManyToOne(optional = false)
    private Etapa idEtapaFk;    
}
