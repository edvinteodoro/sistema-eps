/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author edvin
 */
public interface JwtService {
    public String generateToken(String userName,Collection<? extends GrantedAuthority> authorities);
    public String extractUsername(String token);
    public Boolean validateToken(String token, UserDetails userDetails);
    public Collection<? extends GrantedAuthority> extractRoles(String token);
}
