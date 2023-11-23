/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Persona;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */
@Repository
public interface PersonaRepository extends JpaRepository<Persona,Integer>{
    @Query("SELECT p FROM Persona p "
            + "WHERE p.idProyectoFk.idProyecto = :idProyecto "
            + "AND p.rol = :rol") 
    public Persona findPersona(Integer idProyecto, String rol);
    
    @Query("SELECT p FROM Persona p "
            + "WHERE p.idProyectoFk.idProyecto = :idProyecto "
            + "AND p.rol = :rol") 
    public List<Persona> findPersonas(Integer idProyecto, String rol);
}
