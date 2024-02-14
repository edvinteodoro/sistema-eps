/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;

/**
 *
 * @author edvin
 */
public interface UsuarioProyectoService {
    public Usuario getSecretariaDisponible() throws Exception;
    public Usuario getCoordinadorEpsDisponible() throws Exception;
    public Usuario getSupervisorDisponible(Integer idCarrera) throws Exception;
    public Usuario getCoordinadorCarreraDisponible(Integer idCarrera) throws Exception;
    public UsuarioProyecto crearUsuarioProyecto(Usuario usuario, Proyecto proyecto, Rol rol) throws Exception;
    public UsuarioProyecto getSecretariaProyecto(Integer idProyecto) throws Exception;
    public UsuarioProyecto getSupervisorProyecto(Integer idProyecto) throws Exception;
    public UsuarioProyecto getCoordinadoCarreraProyecto(Integer idProyecto) throws Exception;
    public UsuarioProyecto getAsesorProyecto(Integer idProyecto) throws Exception;
    public UsuarioProyecto getContraparteProyecto(Integer idProyecto) throws Exception;
    public UsuarioProyecto actualizarSupervisorProyecto(Proyecto proyecto,Usuario supervisor) throws Exception;
    public UsuarioProyecto actualizarAsesorProyecto(Proyecto proyecto, Usuario asesor) throws Exception;
    public UsuarioProyecto actualizarContraparteProyecto(Proyecto proyecto, Usuario contraparte) throws Exception;
}
