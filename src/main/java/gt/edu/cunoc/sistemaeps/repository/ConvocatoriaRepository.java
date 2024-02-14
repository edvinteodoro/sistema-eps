package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Convocatoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author edvin
 */
public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Integer>{
    @Query("SELECT c FROM Convocatoria c "
            + "WHERE c.idProyectoFk.idProyecto = :idProyecto "
            + "AND c.activo = :activo "
            + "AND c.tipo = :tipo")
    public Convocatoria findConvocatoriaActiva(Integer idProyecto,Boolean activo,String tipo);
    
}
