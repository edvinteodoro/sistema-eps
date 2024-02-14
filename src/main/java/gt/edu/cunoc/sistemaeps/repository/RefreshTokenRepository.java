package gt.edu.cunoc.sistemaeps.repository;

import gt.edu.cunoc.sistemaeps.entity.RefreshToken;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

/**
 *
 * @author edvin
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    public Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByIdUsuarioFk(Usuario idUsuarioFk);
}
