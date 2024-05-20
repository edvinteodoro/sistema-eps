package gt.edu.cunoc.sistemaeps.dto;

import gt.edu.cunoc.sistemaeps.entity.Persona;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class UsuarioDto {

    private Integer idUsuario;
    private String correo;
    private String nombreCompleto;
    private String telefono;
    private String numeroColegiado;
    private String registroAcademico;
    private String dpi;
    private String direccion;
    private String puesto;
    private Boolean activo;
    private TituloDto titulo;
    private List<CarreraDto> carreras;
    private List<RolDto> roles;
    private RolDto rol;

    public UsuarioDto(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.correo = usuario.getCorreo();
        this.nombreCompleto = usuario.getNombreCompleto();
        this.telefono = usuario.getTelefono();
        this.numeroColegiado = usuario.getNumeroColegiado();
        this.registroAcademico = usuario.getRegistroAcademico();
        this.dpi = usuario.getDpi();
        this.direccion = usuario.getDireccion();
        this.activo = usuario.isCuentaActiva();
        this.rol = new RolDto(usuario.getRolUsuarioList().get(0).getIdRolFk());
        this.roles = usuario.getRolUsuarioList().stream().map(rolUsuario->new RolDto(rolUsuario.getIdRolFk())).toList();
        if (usuario.getIdTituloFk() != null) {
            this.titulo = new TituloDto(usuario.getIdTituloFk());
        }
        if (usuario.getCarreraUsuarioList() != null) {
            this.carreras = usuario.getCarreraUsuarioList().stream()
                    .map(carrera -> new CarreraDto(carrera.getIdCarreraFk())).toList();
        }
    }

    public UsuarioDto(Persona persona) {
        this.idUsuario = persona.getIdPersona();
        this.correo = persona.getCorreo();
        this.nombreCompleto = persona.getNombreCompleto();
        this.telefono = persona.getTelefono();
        this.numeroColegiado = persona.getNumeroColegiado();
        this.registroAcademico = persona.getRegistroAcademico();
        this.dpi = persona.getDpi();
        this.direccion = persona.getDireccion();
        if (persona.getIdTituloFk() != null) {
            this.titulo = new TituloDto(persona.getIdTituloFk());
        }
    }

}
