package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import java.util.List;
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
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer>, JpaSpecificationExecutor<Proyecto> {
    @Override
    public Page<Proyecto> findAll(Specification<Proyecto> spec, Pageable pageable);
    
    @Query("SELECT p FROM Proyecto p "
            + "WHERE p.idUsuarioFk.idUsuario = :idUsuario "
            + "AND p.activo = :activo")
    public List<Proyecto> findProyectoActivos(Integer idUsuario, Boolean activo);
}
