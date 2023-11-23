package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.CarreraUsuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edvin
 */
@Repository
public interface CarreraUsuarioRepository extends JpaRepository<CarreraUsuario, Integer> {

    @Query("SELECT cu FROM CarreraUsuario cu "
            + "WHERE cu.idUsuarioFk.registroAcademico = :registroAcademico")
    public List<CarreraUsuario> findCarrerasUsuario(String registroAcademico);
}
