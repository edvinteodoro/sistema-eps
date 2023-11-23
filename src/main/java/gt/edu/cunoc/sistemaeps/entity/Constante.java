package gt.edu.cunoc.sistemaeps.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "constante")
@Data
public class Constante{
    @Id
    @Basic(optional = false)
    @Column(name = "id_constante")
    private Integer idConstante;
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "valor")
    private String valor;    
}
