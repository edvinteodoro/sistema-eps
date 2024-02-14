/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.RolDto;
import gt.edu.cunoc.sistemaeps.repository.RolUsuarioRepository;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.RolUsuario;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.RolRepository;
import gt.edu.cunoc.sistemaeps.service.RolService;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class RolServiceImp implements RolService {

    private final RolRepository rolRepository;
    private final RolUsuarioRepository rolUsuarioRepository;

    public RolServiceImp(RolRepository rolRepository,
            RolUsuarioRepository rolUsuarioRepository) {
        this.rolRepository = rolRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    @Override
    public Rol getRol(Integer idRol) {
        return this.rolRepository.findById(idRol).get();
    }

    public Rol getRol(String titulo) throws Exception {
        return this.rolRepository.findRolBytitulo(titulo);
    }

    @Override
    public Rol getLoggedUsuarioRol() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (!authorities.isEmpty()) {
                GrantedAuthority authority = authorities.iterator().next();
                String role = authority.getAuthority();
                return getRol(role);
            } else {
                throw new Exception("El usuario no tiene rol");
            }
        } else {
            throw new Exception("El usuario no esta logeado");
        }
    }

    @Override
    public void actualizarRol(Usuario usuario, List<RolDto> rolesDto) throws Exception {
        /*List<Rol> roles = getRolesUsuario(usuario.getIdUsuario());
        for (RolDto rolDto : rolesDto) {
            Rol rol = getRol(rolDto.getIdRol());
            if (!roles.contains(rol)) {
                RolUsuario rolUsuario = new RolUsuario();
                rolUsuario.setIdRolFk(rol);
                rolUsuario.setIdUsuarioFk(usuario);
                this.rolUsuarioRepository.save(rolUsuario);
            }
        }
        List<Integer> idRoles = rolesDto.stream().map(rolDto -> rolDto.getIdRol()).toList();
        for (Rol rol : roles) {
            if(!idRoles.contains(rol.getIdRol())){
                RolUsuario rolUsuario= this.rolUsuarioRepository.findRolUsuario(usuario.getIdUsuario(), rol.getIdRol());
                this.rolUsuarioRepository.delete(rolUsuario);
            }
        }*/
    } 

    @Override
    public List<RolUsuario> getRolUsuario(Integer idUsuario) {
        return this.rolUsuarioRepository.findRolUsuario(idUsuario);
    }

    @Override
    public List<Rol> getAll() {
        return this.rolRepository.findAll();
    }

    @Override
    public List<Rol> getRolesUsuario(Integer idUsuario) {
        return this.rolRepository.findRolesUsuario(idUsuario);
    }

}
