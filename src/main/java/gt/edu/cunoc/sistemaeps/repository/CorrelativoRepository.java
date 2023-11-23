package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Correlativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */
@Repository
public interface CorrelativoRepository extends JpaRepository<Correlativo,Integer>{
    @Query("SELECT c FROM Correlativo c "
            + "WHERE c.idCarreraFk.idCarrera = :idCarrera "
            + "AND c.idEtapaFk.idEtapa = :idEtapa")
    public Correlativo findCorrelativo(Integer idCarrera,Integer idEtapa);
    @Query("SELECT c FROM Correlativo c "
            + "WHERE c.idEtapaFk.idEtapa = :idEtapa")
    public Correlativo findCorrelativo(Integer idEtapa);
}
