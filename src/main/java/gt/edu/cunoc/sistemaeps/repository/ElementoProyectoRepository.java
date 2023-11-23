/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.ElementoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author edvin
 */
public interface ElementoProyectoRepository extends JpaRepository<ElementoProyecto,Integer>{
    @Query("SELECT ep FROM ElementoProyecto ep "
            + "WHERE ep.idEtapaProyectoFk.idProyectoFk.idProyecto = :idProyecto "
            + "AND ep.idElementoFk.idElemento = :idElemento "
            + "AND ep.activo = :activo")
    public ElementoProyecto findElementoProyecto(Integer idProyecto,Integer idElemento,boolean activo);
}
