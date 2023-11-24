/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import gt.edu.cunoc.sistemaeps.repository.UsuarioProyectoRepository;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class UsuarioProyectoServiceImp implements UsuarioProyectoService {

    private final UsuarioProyectoRepository usuarioProyectoRepository;

    public UsuarioProyectoServiceImp(UsuarioProyectoRepository usuarioProyectoRepository) {
        this.usuarioProyectoRepository = usuarioProyectoRepository;
    }

    public UsuarioProyecto saveUsuarioProyecto(UsuarioProyecto usuarioProyecto) {
        return this.usuarioProyectoRepository.save(usuarioProyecto);
    }

    public Usuario getUsuarioDisponible(Integer idRol) throws Exception {
        List<Usuario> usuarios = this.usuarioProyectoRepository
                .findUsuarios(idRol, Boolean.TRUE);
        if (usuarios.isEmpty()) {
            throw new Exception("No hay usuarios disponibles");
        }
        Integer cantidad = 0;
        Usuario tempUsuario = null;
        for (Usuario usuario : usuarios) {
            Integer temp = this.usuarioProyectoRepository.getCantidadProyectos(usuario.getIdUsuario(), Boolean.TRUE);
            if (cantidad <= temp) {
                cantidad = temp;
                tempUsuario = usuario;
            }
        }
        return tempUsuario;
    }

    public Usuario getUsuarioDisponible(Integer idRol, Integer idCarrera) throws Exception {
        List<Usuario> usuarios = this.usuarioProyectoRepository
                .findUsuarios(idRol, Boolean.TRUE);
        if (usuarios.isEmpty()) {
            throw new Exception("No hay usuarios disponibles");
        }
        Integer cantidad = 0;
        Usuario tempUsuario = null;
        for (Usuario usuario : usuarios) {
            if (this.usuarioProyectoRepository.findUsuarioProyecto(usuario.getIdUsuario(), idCarrera).isPresent()) {
                Integer temp = this.usuarioProyectoRepository.getCantidadProyectos(usuario.getIdUsuario(), Boolean.TRUE);
                if (cantidad <= temp) {
                    cantidad = temp;
                    tempUsuario = usuario;
                }
            }
        }
        if(tempUsuario==null){
            throw new Exception("No hay usuarios disponible para asignar proyecto");
        }
        System.out.println("usuario: "+tempUsuario.getNombreCompleto());
        return tempUsuario;
    }

    @Override
    public Usuario getSecretariaDisponible() throws Exception {
        return getUsuarioDisponible(RolUtils.ID_ROL_SECRETARIA);
    }

    @Override
    public Usuario getCoordinadorEpsDisponible() throws Exception {
        return getUsuarioDisponible(RolUtils.ID_ROL_COORDINADOR_EPS);
    }

    @Override
    public Usuario getSupervisorDisponible(Integer idCarrera) throws Exception {
        return getUsuarioDisponible(RolUtils.ID_ROL_SUPERVISOR, idCarrera);
    }

    @Override
    public Usuario getCoordinadorCarreraDisponible(Integer idCarrera) throws Exception {
        return getUsuarioDisponible(RolUtils.ID_ROL_COORDINADOR_CARRERA, idCarrera);
    }

    @Override
    public UsuarioProyecto crearUsuarioProyecto(Usuario usuario, Proyecto proyecto, Rol rol) throws Exception {
        UsuarioProyecto usuarioProyecto = new UsuarioProyecto();
        usuarioProyecto.setIdRolFk(rol);
        usuarioProyecto.setIdProyectoFk(proyecto);
        usuarioProyecto.setIdUsuarioFk(usuario);
        return saveUsuarioProyecto(usuarioProyecto);
    }

    @Override
    public UsuarioProyecto getSecretariaProyecto(Integer idProyecto) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioProyecto(idProyecto, RolUtils.ID_ROL_SECRETARIA, Boolean.TRUE);
    }

    @Override
    public UsuarioProyecto getSupervisorProyecto(Integer idProyecto) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioProyecto(idProyecto, RolUtils.ID_ROL_SUPERVISOR, Boolean.TRUE);
    }

    @Override
    public UsuarioProyecto getAsesorProyecto(Integer idProyecto) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioProyecto(idProyecto, RolUtils.ID_ROL_ASESOR, Boolean.TRUE);
    }

    @Override
    public UsuarioProyecto getContraparteProyecto(Integer idProyecto) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioProyecto(idProyecto, RolUtils.ID_ROL_CONTRAPARTE, Boolean.TRUE);
    }

    @Override
    public UsuarioProyecto getCoordinadoCarreraProyecto(Integer idProyecto) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioProyecto(idProyecto, RolUtils.ID_ROL_COORDINADOR_CARRERA, Boolean.TRUE);
    }

}
