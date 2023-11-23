/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento,Integer>{
    @Query("SELECT m.idDepartamentoFk FROM Municipio m "
            + "WHERE m.idMunicipio = :idMunicipio")
    public Departamento findDepartamento(Integer idMunicipio);
}
