package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */
@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Integer>,JpaSpecificationExecutor<Bitacora>{
    @Override
    public Page<Bitacora> findAll(Specification<Bitacora> spec, Pageable pageable);
}

