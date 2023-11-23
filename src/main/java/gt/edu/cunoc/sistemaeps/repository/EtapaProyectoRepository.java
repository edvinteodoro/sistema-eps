package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author edvin
 */
public interface EtapaProyectoRepository extends JpaRepository<EtapaProyecto, Integer> {

    @Query("SELECT ep FROM EtapaProyecto ep "
            + "WHERE ep.idProyectoFk.idProyecto = :idProyecto "
            + "AND ep.idEtapaFk.idEtapa = :idEtapa")
    public EtapaProyecto findEtapaProyecto(Integer idProyecto, Integer idEtapa);

    @Query("SELECT ep FROM EtapaProyecto ep "
            + "WHERE ep.idProyectoFk.idProyecto = :idProyecto "
            + "AND ep.activo = :activo")
    public EtapaProyecto findEtapaProyecto(Integer idProyecto, Boolean activo);
}
