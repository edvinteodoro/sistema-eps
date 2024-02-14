package gt.edu.cunoc.sistemaeps.entity;

import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
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
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_persona")
    private Integer idPersona;
    @Basic(optional = false)
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    @Basic(optional = false)
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "numero_colegiado")
    private String numeroColegiado;
    @Column(name = "registro_academico")
    private String registroAcademico;
    @Column(name = "dpi")
    private String dpi;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "puesto")
    private String puesto;
    @Basic(optional = false)
    @Column(name = "rol")
    private String rol;
    @JoinColumn(name = "id_proyecto_fk", referencedColumnName = "id_proyecto")
    @ManyToOne(optional = false)
    private Proyecto idProyectoFk;
    @JoinColumn(name = "id_titulo_fk", referencedColumnName = "id_titulo")
    @ManyToOne
    private Titulo idTituloFk;

    public Persona(UsuarioDto usuarioDto) {
        this.correo = usuarioDto.getCorreo();
        this.nombreCompleto = usuarioDto.getNombreCompleto();
        this.telefono = usuarioDto.getTelefono();
        this.numeroColegiado = usuarioDto.getNumeroColegiado();
        this.registroAcademico = usuarioDto.getRegistroAcademico();
        this.dpi = usuarioDto.getDpi();
        this.direccion = usuarioDto.getDireccion();
        this.puesto = usuarioDto.getPuesto();
    }
    
    public void updatePersona(UsuarioDto usuarioDto){
        this.correo = usuarioDto.getCorreo();
        this.nombreCompleto = usuarioDto.getNombreCompleto();
        this.telefono = usuarioDto.getTelefono();
        this.numeroColegiado = usuarioDto.getNumeroColegiado();
        this.registroAcademico = usuarioDto.getRegistroAcademico();
        this.dpi = usuarioDto.getDpi();
        this.direccion = usuarioDto.getDireccion();
        this.puesto = usuarioDto.getPuesto();
        //this.rol = usuarioDto.getRol().getTitulo();
    }
}
