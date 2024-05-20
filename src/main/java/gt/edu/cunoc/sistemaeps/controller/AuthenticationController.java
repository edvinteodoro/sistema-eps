/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.JwtResponse;
import gt.edu.cunoc.sistemaeps.dto.LoginRequest;
import gt.edu.cunoc.sistemaeps.dto.RefreshTokenRequest;
import gt.edu.cunoc.sistemaeps.dto.TokenConfirmacionDto;
import gt.edu.cunoc.sistemaeps.entity.RefreshToken;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.exception.TokenRefreshException;
import gt.edu.cunoc.sistemaeps.service.JwtService;
import gt.edu.cunoc.sistemaeps.service.RefreshTokenService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edvin
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(AuthenticationManager authenticationManager,
            JwtService jwtService, UsuarioService usuarioService,
            RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                Usuario usuario = this.usuarioService.getUsuario(loginRequest.getUsername());
                RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(usuario.getIdUsuario());
                JwtResponse response = new JwtResponse(usuario.getIdUsuario(),jwtService.generateToken(loginRequest.getUsername(),
                        authentication.getAuthorities()), refreshToken.getToken(),
                        authentication.getAuthorities().stream().map(authority -> authority.getAuthority()).toList());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credenciales invalidas");
            }
        } catch (AuthenticationException e) {
            System.out.println("error: " + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas");
        } catch (Exception ex) {
            System.out.println("error: " + ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el sistema");
        }
    }

    @PostMapping("refresh-token")
    public ResponseEntity refreshToken(@RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getIdUsuarioFk)
                .map(usuario -> {
                    Collection<? extends GrantedAuthority> authorities = usuario.getRolUsuarioList().stream()
                            .map(rolUsuario -> new SimpleGrantedAuthority(rolUsuario.getIdRolFk().getTitulo()))
                            .collect(Collectors.toList());
                    RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(usuario.getIdUsuario(), requestRefreshToken);
                    JwtResponse response = new JwtResponse(usuario.getIdUsuario(),jwtService
                            .generateToken(usuario.getUserName(), authorities), refreshToken.getToken(),
                            authorities.stream().map(authority -> authority.getAuthority()).toList());
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                "Refresh token is not in database!"));
    }

    @PostMapping("/activar-cuenta")
    public ResponseEntity activarCuenta(@RequestBody TokenConfirmacionDto tokenConfirmacionDto) {
        try {
            this.usuarioService.activarUsuario(tokenConfirmacionDto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error: " + e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
