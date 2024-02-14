package gt.edu.cunoc.sistemaeps.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private Integer idUsuario;
    private List<String> roles;
    
    public JwtResponse(Integer idUsuario,String accessToken, String refreshToken,List<String> roles){
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
        this.roles=roles;
        this.idUsuario=idUsuario;
    }
}
