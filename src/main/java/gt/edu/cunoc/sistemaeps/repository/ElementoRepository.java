/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Elemento;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author edvin
 */
public interface ElementoRepository extends JpaRepository<Elemento,Integer>{
    
}
