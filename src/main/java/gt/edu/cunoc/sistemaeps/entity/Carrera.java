package gt.edu.cunoc.sistemaeps.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "carrera")
@Data
public class Carrera{

    @Id
    @Basic(optional = false)
    @Column(name = "id_carrera")
    private Integer idCarrera;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "nombre_corto")
    private String nombreCorto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCarreraFk")
    private List<Proyecto> proyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCarreraFk")
    private List<CarreraUsuario> carreraUsuarioList;
    @OneToMany(mappedBy = "idCarreraFk")
    private List<Correlativo> correlativoList;    
}
