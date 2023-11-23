/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Usuario;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>,JpaSpecificationExecutor<Usuario> {

    @Query("SELECT u FROM Usuario u "
            + "WHERE u.registroAcademico = :registroAcademico")
    public Optional<Usuario> findUsuarioByRegistroAcademico(String registroAcademico);

    @Query("SELECT u FROM Usuario u "
            + "WHERE u.numeroColegiado = :numeroColegiado")
    public Optional<Usuario> findUsuarioByNumeroColegiado(String numeroColegiado);
    
    
    @Query("SELECT u FROM Usuario u "
            + "WHERE u.dpi = :dpi")
    public Optional<Usuario> findUsuarioByDpi(String dpi);
    
    @Override
    public Page<Usuario> findAll(Specification<Usuario> spec, Pageable pageable);

}
