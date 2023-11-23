package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
public class Usuario{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Basic(optional = false)
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    @Column(name = "registro_academico")
    private String registroAcademico;
    @Column(name = "numero_colegiado")
    private String numeroColegiado;
    @Basic(optional = false)
    @Column(name = "dpi")
    private String dpi;
    @Basic(optional = false)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "genero")
    private String genero;
    @Basic(optional = false)
    @Column(name = "cuenta_activa")
    private boolean cuentaActiva;
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "idUsuarioFk",fetch = FetchType.LAZY)
    private List<UsuarioProyecto> usuarioProyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioFk")
    private List<Proyecto> proyectoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioFk",fetch = FetchType.LAZY)
    private List<RolUsuario> rolUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioFk")
    private List<ComentarioBitacora> comentarioBitacoraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioFk")
    private List<CarreraUsuario> carreraUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioFk")
    private List<TokenConfirmacion> tokenConfirmacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioFk")
    private List<Comentario> comentarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEstudianteFk")
    private List<CorrelativoEstudiante> correlativoEstudianteList;
    @JoinColumn(name = "id_titulo_fk", referencedColumnName = "id_titulo")
    @ManyToOne(optional = false)
    private Titulo idTituloFk;

    public Usuario(UsuarioDto usuarioDto) {
        this.correo=usuarioDto.getCorreo();
        this.nombreCompleto=usuarioDto.getNombreCompleto();
        this.registroAcademico=usuarioDto.getRegistroAcademico();
        this.dpi=usuarioDto.getDpi();
        this.direccion=usuarioDto.getDireccion();
        this.telefono = usuarioDto.getTelefono();
        this.cuentaActiva=false;
    }
}
