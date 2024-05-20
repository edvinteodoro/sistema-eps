package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Acta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author edvin
 */
public interface ActaRepository extends JpaRepository<Acta, Integer>,JpaSpecificationExecutor<Acta>{
    
    @Query("SELECT a FROM Acta a "
            + "WHERE a.idProyectoFk.idProyecto = :idProyecto "
            + "AND a.tipo = :tipo "
            + "AND a.activo = :activo")
    public Acta findActaActiva(Integer idProyecto,String tipo,Boolean activo);
    
    @Override
    public Page<Acta> findAll(Specification<Acta> spec, Pageable pageable);
}
