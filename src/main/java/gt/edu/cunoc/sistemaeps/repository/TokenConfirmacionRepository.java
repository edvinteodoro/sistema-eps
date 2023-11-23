package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.TokenConfirmacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author edvin
 */
public interface TokenConfirmacionRepository extends JpaRepository<TokenConfirmacion,Integer>{
    @Query("SELECT t FROM TokenConfirmacion t "
            + "WHERE t.token = :token")
    public TokenConfirmacion findTokenConfirmacion(String token);
}
