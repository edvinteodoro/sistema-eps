package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.CorrelativoEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */
@Repository
public interface CorrelativoEstudianteRepository extends JpaRepository<CorrelativoEstudiante, Integer> {

    @Query("SELECT ce FROM CorrelativoEstudiante ce "
            + "WHERE ce.idEstudianteFk.idUsuario = :idUsuario "
            + "AND ce.idCorrelativoFk.idCorrelativo = :idCorrelativo "
            + "AND ce.anulado = :anulado")
    public CorrelativoEstudiante findCorrelativoEstudianteActivo(Integer idUsuario,
            Integer idCorrelativo, Boolean anulado);
}
