/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.TokenConfirmacionDto;
import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author edvin
 */
public interface UsuarioService {

    public Page<Usuario> getAll(String nombre, String registroAcademico,
            String colegiado, String dpi, Integer idRol, Pageable pageable) throws Exception;

    public Usuario crearUsuario(UsuarioDto usuarioDto) throws Exception;

    public Usuario getUsuario(String username) throws Exception;

    public Usuario getUsuario(Integer idUsuario) throws Exception;

    public Usuario getLoggedUsuario() throws Exception;

    public void activarUsuario(TokenConfirmacionDto tokenConfirmacionDto) throws Exception;

    public Usuario actualizarUsuario(Integer idUsuario, UsuarioDto usuarioDto) throws Exception;
}
