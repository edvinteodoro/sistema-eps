package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.Comentario;
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
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    @Query("SELECT c FROM Comentario c "
            + "WHERE c.etapaProyectoFk.idProyectoFk.idProyecto = :idProyecto "
            + "ORDER BY c.idComentario DESC")
    public Page<Comentario> findComentariosProyecto(Integer idProyecto, Pageable pageable);
}
