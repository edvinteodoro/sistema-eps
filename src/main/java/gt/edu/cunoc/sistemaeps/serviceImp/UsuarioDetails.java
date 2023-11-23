/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.jwt;

import gt.edu.cunoc.sistemaeps.entity.RolUsuario;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author edvin
 */
public class UsuarioDetails implements UserDetails {

    private final String name;
    private final String password;
    private final Boolean activo;
    private final List<GrantedAuthority> authorities;

    public UsuarioDetails(String username,String password,
            Boolean activo,List<RolUsuario> roles) {
        this.name = username;
        this.activo = activo;
        this.password = password;
        authorities = roles.stream().map(rolUsuario->rolUsuario.getIdRolFk().getTitulo())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password; 
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }

}
