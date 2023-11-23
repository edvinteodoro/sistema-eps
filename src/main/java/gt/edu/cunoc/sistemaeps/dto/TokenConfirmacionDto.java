package gt.edu.cunoc.sistemaeps.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author edvin
 */
@Data
@NoArgsConstructor
public class TokenConfirmacionDto {
    private String token;
    private String password1;
    private String password2;
}
