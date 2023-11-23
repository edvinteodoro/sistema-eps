package gt.edu.cunoc.sistemaeps.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "permisos")
@Data
public class Permisos{
    @Id
    @Basic(optional = false)
    @Column(name = "id_permisos")
    private Integer idPermisos;
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "estado")
    private String estado;
    @JoinTable(name = "rol_permisos", joinColumns = {
        @JoinColumn(name = "id_permiso_fk", referencedColumnName = "id_permisos")}, inverseJoinColumns = {
        @JoinColumn(name = "id_rol_fk", referencedColumnName = "id_rol")})
    @ManyToMany
    private List<Rol> rolList;    
}
