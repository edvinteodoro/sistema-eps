package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.dto.InstitucionDto;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "institucion")
@Data
@NoArgsConstructor
public class Institucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_institucion")
    private Integer idInstitucion;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "coordenadas")
    private String coordenadas;
    @Column(name = "direccion")
    private String direccion;
    @JoinColumn(name = "id_municipio_fk", referencedColumnName = "id_municipio")
    @ManyToOne
    private Municipio idMunicipioFk;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idInstitucionFk")
    private Proyecto proyecto;
    
    public Institucion(InstitucionDto institucionDto){
        this.nombre=institucionDto.getNombre();
        this.coordenadas=institucionDto.getCoordenadas();
        this.direccion=institucionDto.getDireccion();
    }
    
    public void actualizar(InstitucionDto institucionDto){
        this.nombre=institucionDto.getNombre();
        this.coordenadas=institucionDto.getCoordenadas();
        this.direccion=institucionDto.getDireccion();
    }
}
