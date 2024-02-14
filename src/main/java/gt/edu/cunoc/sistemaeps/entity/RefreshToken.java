package gt.edu.cunoc.sistemaeps.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Entity
@Table(name = "refresh_token")
@Data
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_refresh_token")
    private Integer idRefreshToken;
    @OneToOne
    @JoinColumn(name = "id_usuario_fk", referencedColumnName = "id_usuario")
    private Usuario idUsuarioFk;
    @Column(name="token",nullable = false, unique = true)
    private String token;
    @Column(name="expiry_date",nullable = false)
    private Instant expiryDate;
}
