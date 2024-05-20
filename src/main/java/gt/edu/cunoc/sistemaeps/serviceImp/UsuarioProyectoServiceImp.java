/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Carrera;
import gt.edu.cunoc.sistemaeps.entity.CarreraUsuario;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.RolUsuario;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import gt.edu.cunoc.sistemaeps.repository.UsuarioProyectoRepository;
import gt.edu.cunoc.sistemaeps.service.CarreraService;
import gt.edu.cunoc.sistemaeps.service.RolService;
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
    private final RolService rolService;
    private final CarreraService carreraService;

    public UsuarioProyectoServiceImp(UsuarioProyectoRepository usuarioProyectoRepository, RolService rolService,
            CarreraService carreraService) {
        this.usuarioProyectoRepository = usuarioProyectoRepository;
        this.rolService = rolService;
        this.carreraService = carreraService;
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
            if (cantidad > temp || tempUsuario == null) {
                cantidad = temp;
                tempUsuario = usuario;
            }
        }
        return tempUsuario;
    }

    public Usuario getUsuarioDisponible(Integer idRol, Integer idCarrera) throws Exception {//supervisor //sistemas //pedro 1
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
                if (cantidad > temp || tempUsuario == null) {
                    cantidad = temp;
                    tempUsuario = usuario;
                }
            }
        }
        if (tempUsuario == null) {
            throw new Exception("No hay usuarios disponible para asignar proyecto");
        }
        return tempUsuario;
    }

    private Boolean validarRol(List<RolUsuario> rolesUsuario, Rol rol) {
        return rolesUsuario.stream().anyMatch(usuario -> usuario.getIdRolFk().equals(rol));
    }

    private Boolean validarCarrera(List<CarreraUsuario> carrerasUsuario, Carrera carrera) {
        return carrerasUsuario.stream().anyMatch(carreraUsuario -> carreraUsuario.getIdCarreraFk().equals(carrera));
    }

    /*@Override
    public UsuarioProyecto actualizarSupervisorProyecto(Proyecto proyecto, Usuario supervisor) throws Exception {
        Usuario supervisor = this.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        Rol rolSupervisor = this.rolService.getRol(RolUtils.ID_ROL_SUPERVISOR);
        if (!validarRol(this.rolService.getRolUsuario(supervisor.getIdUsuario()), rolSupervisor)) {
            throw new Exception("El Usuario asignado no es de tipo Supervisor");
        }
        if (!validarCarrera(this.carreraService.getCarrerasUsuario(supervisor.getIdUsuario()), proyecto.getIdCarreraFk())) {
            throw new Exception("El Supervisor puede ser asignado debido a que no corresponde a la carrera del proyecto");
        }
        supervisorProyecto.setActivo(Boolean.FALSE);
        return crearUsuarioProyecto(supervisor, proyecto, rolSupervisor);
    }*/

    @Override
    public UsuarioProyecto actualizarAsesorProyecto(Proyecto proyecto, Usuario asesor) throws Exception {
        UsuarioProyecto asesorProyecto = this.getAsesorProyecto(proyecto.getIdProyecto());
        Rol rolAsesor = this.rolService.getRol(RolUtils.ID_ROL_ASESOR);
        /*if (!validarRol(this.rolService.getRolUsuario(asesor.getIdUsuario()), rolAsesor)) {
            throw new Exception("El Usuario asignado no es de tipo Asesor");
        }
        if (!validarCarrera(this.carreraService.getCarrerasUsuario(asesor.getIdUsuario()), proyecto.getIdCarreraFk())) {
            throw new Exception("El Supervisor puede ser asignado debido a que no corresponde a la carrera del proyecto");
        }*/
        asesorProyecto.setActivo(Boolean.FALSE);
        return crearUsuarioProyecto(asesor, proyecto, rolAsesor);
    }

    @Override
    public UsuarioProyecto actualizarContraparteProyecto(Proyecto proyecto, Usuario contraparte) throws Exception {
        UsuarioProyecto contraparteProyecto = this.getContraparteProyecto(proyecto.getIdProyecto());
        Rol rolContraparte = this.rolService.getRol(RolUtils.ID_ROL_CONTRAPARTE);
        /*if (!validarRol(this.rolService.getRolUsuario(contraparte.getIdUsuario()), rolContraparte)) {
            throw new Exception("El Usuario asignado no es de tipo Asesor");
        }*/
        contraparteProyecto.setActivo(Boolean.FALSE);
        return crearUsuarioProyecto(contraparte, proyecto, rolContraparte);
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
    public UsuarioProyecto getUsuarioProyecto(Integer idProyecto, Integer idUsuario) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioAsignadoProyecto(idProyecto, idUsuario, Boolean.TRUE);
    }

    /*
    @Override
    public UsuarioProyecto getSecretariaProyecto(Integer idProyecto) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioProyecto(idProyecto, RolUtils.ID_ROL_SECRETARIA, Boolean.TRUE);
    }
    */

    /*@Override
    public UsuarioProyecto getSupervisorProyecto(Integer idProyecto) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioProyecto(idProyecto, RolUtils.ID_ROL_SUPERVISOR, Boolean.TRUE);
    }*/

    @Override
    public UsuarioProyecto getAsesorProyecto(Integer idProyecto) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioProyecto(idProyecto, RolUtils.ID_ROL_ASESOR, Boolean.TRUE);
    }

    @Override
    public UsuarioProyecto getContraparteProyecto(Integer idProyecto) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioProyecto(idProyecto, RolUtils.ID_ROL_CONTRAPARTE, Boolean.TRUE);
    }

    /*
    @Override
    public UsuarioProyecto getCoordinadoCarreraProyecto(Integer idProyecto) throws Exception {
        return this.usuarioProyectoRepository.findUsuarioProyecto(idProyecto, RolUtils.ID_ROL_COORDINADOR_CARRERA, Boolean.TRUE);
    }
*/
}
