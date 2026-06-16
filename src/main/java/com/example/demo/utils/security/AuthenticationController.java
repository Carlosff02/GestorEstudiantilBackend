package com.example.demo.utils.security;


import com.example.demo.presentation.dtos.AuthenticationResponse;
import com.example.demo.presentation.dtos.LoginRequest;
import com.example.demo.presentation.dtos.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws IllegalAccessException {

        return ResponseEntity.ok(authenticationService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<?> autenticarUsuario(@RequestBody LoginRequest request) {

        try {
            AuthenticationResponse response = authenticationService.authenticate(request);

            if (response == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message","Usuario o contraseña incorrectos"));
            }
            return ResponseEntity.ok(response);
        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "La cuenta está bloqueada. Contacte con el administrador."));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "La cuenta está deshabilitada."));
        } catch (AccountExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message","La cuenta ha expirado."));
        } catch (CredentialsExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message","Las credenciales han expirado. Por favor, cambie su contraseña."));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales inválidas."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message","Ocurrió un error" ));
        }
    }





    @PutMapping("/cambiar-contraseña")
    public ResponseEntity cambiarContrasena(@RequestParam("userid") String userid,
                                            @RequestParam("contrasenaActual") String contrasenaActual,
                                            @RequestParam("nuevaContrasena") String nuevaContrasena){
        try {
            authenticationService.cambiarContrasena(userid, contrasenaActual, nuevaContrasena);
            return ResponseEntity.status(200).body(Map.of("message","Contraseña actualizada correctamente"));
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "La contraseña actual es incorrecta"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(Map.of("message", "Error interno del servidor"));

        }
    }

}
