package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */
@Repository
public interface EtapaRepository extends JpaRepository<Etapa,Integer>{
    
}
