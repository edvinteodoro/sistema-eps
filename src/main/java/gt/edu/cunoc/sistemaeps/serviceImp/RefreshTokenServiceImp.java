package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.RefreshToken;
import gt.edu.cunoc.sistemaeps.exception.TokenRefreshException;
import gt.edu.cunoc.sistemaeps.repository.RefreshTokenRepository;
import gt.edu.cunoc.sistemaeps.repository.UsuarioRepository;
import gt.edu.cunoc.sistemaeps.service.RefreshTokenService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author edvin
 */
@Service
public class RefreshTokenServiceImp implements RefreshTokenService {

    private final Long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;

    public RefreshTokenServiceImp(@Value("${spring.jwt.refresh_expiration}") Long refreshTokenDurationMs,
            RefreshTokenRepository refreshTokenRepository, UsuarioRepository usuarioRepository) {
        this.refreshTokenDurationMs = refreshTokenDurationMs;
        this.refreshTokenRepository = refreshTokenRepository;
        this.usuarioRepository = usuarioRepository;

    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Integer userId, String token) {
        RefreshToken oldToken=findByToken(token).get();
        if(oldToken!=null){ 
            refreshTokenRepository.delete(oldToken);
        }
        return createRefreshToken(userId);
    }

    @Override
    public RefreshToken createRefreshToken(Integer userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setIdUsuarioFk(this.usuarioRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    @Override
    public int deleteByUserId(Integer userId) {
        return refreshTokenRepository.deleteByIdUsuarioFk(this.usuarioRepository.findById(userId).get());
    }
}
