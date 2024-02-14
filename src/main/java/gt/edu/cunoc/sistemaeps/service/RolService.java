/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.RolDto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.RolUsuario;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import java.util.List;

/**
 *
 * @author edvin
 */
public interface RolService {
    public Rol getRol(Integer idRol);
    public List<Rol> getAll();
    public Rol getLoggedUsuarioRol() throws Exception;
    public List<RolUsuario> getRolUsuario(Integer idUsuario);
    public List<Rol> getRolesUsuario(Integer idUsuario);
    public void actualizarRol(Usuario usuario, List<RolDto> rolesDto) throws Exception;
}
