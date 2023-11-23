/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.LoginRequest;
import gt.edu.cunoc.sistemaeps.dto.LoginResponse;
import gt.edu.cunoc.sistemaeps.dto.TokenConfirmacionDto;
import gt.edu.cunoc.sistemaeps.service.JwtService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author edvin
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthenticationController(AuthenticationManager authenticationManager,
            JwtService jwtService, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok(new LoginResponse(jwtService.generateToken(loginRequest.getUsername(),
                        authentication.getAuthorities())));
            } else {
                System.out.println("invalido");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credenciales invalidas");
            }
        } catch (AuthenticationException e) {
            System.out.println("error: " + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas");
        }
    }

    @PostMapping("/activar-cuenta")
    public ResponseEntity activarCuenta(@RequestBody TokenConfirmacionDto tokenConfirmacionDto) {
        try {
            this.usuarioService.activarUsuario(tokenConfirmacionDto);
            return ResponseEntity.ok("Cuenta activada");
        } catch (Exception e) {
            System.out.println("error: "+e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
