package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Prorroga;
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
public interface ProrrogaRepository extends JpaRepository<Prorroga, Integer>, JpaSpecificationExecutor<Prorroga>{
    @Override
    public Page<Prorroga> findAll(Specification<Prorroga> spec, Pageable pageable);
}
