package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Recurso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */
@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Integer>{
    @Query("SELECT r FROM Recurso r "
            + "WHERE r.idBitacoraFk.idBitacora = :idBitacora")
    public List<Recurso> findRecursos(Integer idBitacora);
}
