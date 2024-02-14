package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.ComentarioBitacora;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */

@Repository
public interface ComentarioBitacoraRepository extends JpaRepository<ComentarioBitacora, Integer>{
    
    @Query("SELECT cb FROM ComentarioBitacora cb "
            + "WHERE cb.idBitacoraFk.idBitacora = :idBitacora "
            + "ORDER BY cb.idComentarioBitacora DESC")
    public Page<ComentarioBitacora> findComentariosBitacora(Integer idBitacora, Pageable pageable);
}
