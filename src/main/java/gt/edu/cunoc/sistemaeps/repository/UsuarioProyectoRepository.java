/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */
@Repository
public interface UsuarioProyectoRepository extends JpaRepository<UsuarioProyecto, Integer> {

    @Query("SELECT up FROM UsuarioProyecto up "
            + "WHERE up.idProyectoFk.idProyecto = :idProyecto "
            + "AND up.idRolFk.idRol = :idRol "
            + "AND up.activo = :activo")
    public UsuarioProyecto findUsuarioProyecto(Integer idProyecto, Integer idRol, Boolean activo);

    @Query("SELECT u FROM Usuario u "
            + "LEFT JOIN u.rolUsuarioList ru "
            + "WHERE ru.idRolFk.idRol = :idRol "
            + "AND u.cuentaActiva = :cuentaActiva")
    public List<Usuario> findUsuarios(Integer idRol, Boolean cuentaActiva);

    @Query("SELECT COUNT(up) FROM UsuarioProyecto up "
            + "WHERE up.idUsuarioFk.idUsuario = :idUsuario "
            + "AND up.activo = :activo")
    public Integer getCantidadProyectos(Integer idUsuario,Boolean activo);

   @Query("SELECT u FROM Usuario u "
            + "LEFT JOIN u.carreraUsuarioList cu "
            + "WHERE cu.idCarreraFk.idCarrera = :idCarrera "
            + "AND u.idUsuario = :idUsuario")
    public Optional<Usuario> findUsuarioProyecto(Integer idUsuario,Integer idCarrera);

}
