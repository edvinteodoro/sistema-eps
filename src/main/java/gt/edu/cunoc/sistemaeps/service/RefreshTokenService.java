package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.entity.RefreshToken;
import java.util.Optional;

/**
 *
 * @author edvin
 */
public interface RefreshTokenService {

    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(Integer userId, String token);
    public RefreshToken createRefreshToken(Integer userId);
    public RefreshToken verifyExpiration(RefreshToken token);
    public int deleteByUserId(Integer userId);
}
