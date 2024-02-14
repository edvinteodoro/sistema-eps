/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.RolUsuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author edvin
 */
public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Integer> {

    @Query("SELECT ru FROM RolUsuario ru "
            + "WHERE ru.idUsuarioFk.idUsuario = :idUsuario")
    public List<RolUsuario> findRolUsuario(Integer idUsuario);

    /*@Query("SELECT ru FORM RolUsuario ru "
            + "WHERE ru.idUsuarioFk.idUsuario = :idUsuario "
            + "AND ru.idRolFk.idRol = :idRol")
    public RolUsuario findRolUsuario(Integer idUsuario, Integer idRol);*/
}
