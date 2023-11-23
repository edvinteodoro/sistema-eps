package gt.edu.cunoc.sistemaeps.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "rol")
@Data
public class Rol {

    @Id
    @Basic(optional = false)
    @Column(name = "id_rol")
    private Integer idRol;
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @Column(name = "contiene_carrera")
    private Boolean contieneCarrera;
    @Basic(optional = false)
    @Column(name = "contiene_registro")
    private Boolean contieneRegistro;
    @Basic(optional = false)
    @Column(name = "contiene_colegiado")
    private Boolean contieneColegiado;
    @ManyToMany(mappedBy = "rolList")
    private List<Permisos> permisosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRolFk")
    private List<CambioUsuario> cambioUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRolFk")
    private List<UsuarioProyecto> usuarioProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRolFk")
    private List<RolUsuario> rolUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRolFk")
    private List<ComentarioBitacora> comentarioBitacoraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRolFk")
    private List<Comentario> comentarioList;
}
