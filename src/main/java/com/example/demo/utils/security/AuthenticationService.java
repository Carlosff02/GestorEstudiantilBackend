package com.example.demo.utils.security;



import com.example.demo.persistance.entity.AuditoriaAcceso;
import com.example.demo.persistance.entity.Usuario;
import com.example.demo.persistance.repository.UsuarioRepository;
import com.example.demo.presentation.dtos.AuthenticationResponse;
import com.example.demo.presentation.dtos.LoginRequest;
import com.example.demo.presentation.dtos.RegisterRequest;
import com.example.demo.service.implementation.AuditoriaAccesoService;
import com.example.demo.service.implementation.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final EmailService emailService;
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditoriaAccesoService auditoriaAccesoService;
    private final HttpServletRequest httpServletRequest;


    public AuthenticationService(UsuarioRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 TokenService jwtService,
                                 AuthenticationManager authenticationManager,
                                 EmailService emailService, AuditoriaAccesoService auditoriaAccesoService, HttpServletRequest httpServletRequest) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.auditoriaAccesoService = auditoriaAccesoService;
        this.httpServletRequest = httpServletRequest;
    }

    public AuthenticationResponse register(RegisterRequest request) throws IllegalAccessException {


        String contrasenaHash = passwordEncoder.encode(request.password());

        //user.setRole(request.getRole());
        Optional<Usuario> usuarioBuscar = repository.findByCorreo(request.correo());
        if(usuarioBuscar.isPresent()){
            throw new IllegalAccessException("Ya existe un usuario con ese correo");
        };
        Usuario user = new Usuario(null, request.correo(),contrasenaHash,request.nombres(),request.apellidos(),
                "estudiante",true,request.perfilAccesibilidad(),request.idioma(), LocalDateTime.now(), LocalDateTime.now()
                );
        Usuario usuario =repository.save(user);

        String token = jwtService.generarToken(usuario);
        String destinatario = request.correo();
        String subject = "Usuario "+request.nombres()+" creado correctamente";
        String mensaje = """
Su usuario ha sido creado correctamente.

Usuario: %s

""".formatted(
                request.correo()
        );

        //emailService.enviarCorreoAsync(destinatario,subject,mensaje);

        return new AuthenticationResponse(user.getIdUsuario(),
                token,
                "estudiante",
                user.getNombres(),
                user.getApellidos(),
                user.getIdioma(),
                user.getPerfilAccesibilidad());
    }

    public AuthenticationResponse authenticate(LoginRequest request) {

        // 1. Buscar el usuario — puede no existir
        Usuario user = repository.findByCorreo(request.correo()).orElseThrow(()->new IllegalArgumentException("No se ha encontrado un usuario con correo "+request.correo()));


        if (user == null) {
            auditoriaAccesoService.save(
                    AuditoriaAcceso.builder()
                            .correoIntentado(request.correo())
                            .tipoEvento(AuditoriaAcceso.TipoEventoAcceso.login)
                            .resultado(AuditoriaAcceso.ResultadoAcceso.fallido)
                            .motivoFallo(AuditoriaAcceso.MotivoFalloAcceso.usuario_no_existe)
                            .detalle("El correo no está registrado en el sistema")
                            .ipOrigen(obtenerIp())
                            .build()
            );
            throw new IllegalArgumentException("Usuario no encontrado");
        }



        // 3. Intentar autenticación — capturar fallo de contraseña
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.correo(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException ex) {
            auditoriaAccesoService.save(
                    AuditoriaAcceso.builder()
                            .usuario(user)
                            .correoIntentado(request.correo())
                            .tipoEvento(AuditoriaAcceso.TipoEventoAcceso.login)
                            .resultado(AuditoriaAcceso.ResultadoAcceso.fallido)
                            .motivoFallo(AuditoriaAcceso.MotivoFalloAcceso.contrasena_incorrecta)
                            .detalle("Contraseña incorrecta")
                            .ipOrigen(obtenerIp())
                            .build()
            );
            throw ex; // relanzar para que el controlador devuelva 401
        } catch (LockedException ex) {
            auditoriaAccesoService.save(
                    AuditoriaAcceso.builder()
                            .usuario(user)
                            .correoIntentado(request.correo())
                            .tipoEvento(AuditoriaAcceso.TipoEventoAcceso.login)
                            .resultado(AuditoriaAcceso.ResultadoAcceso.fallido)
                            .motivoFallo(AuditoriaAcceso.MotivoFalloAcceso.cuenta_bloqueada)
                            .detalle("Cuenta bloqueada por demasiados intentos")
                            .ipOrigen(obtenerIp())
                            .build()
            );
            throw ex;
        }

        // 4. Login exitoso — generar token y registrar
        String token = jwtService.generarToken(user);

        auditoriaAccesoService.save(
                AuditoriaAcceso.builder()
                        .usuario(user)
                        .correoIntentado(request.correo())
                        .tipoEvento(AuditoriaAcceso.TipoEventoAcceso.login)
                        .resultado(AuditoriaAcceso.ResultadoAcceso.exitoso)
                        .motivoFallo(null)
                        .ipOrigen(obtenerIp())
                        .build()
        );

        return new AuthenticationResponse(
                user.getIdUsuario(),
                token,
                user.getRol(),
                user.getNombres(),
                user.getApellidos(),
                user.getIdioma(),
                user.getPerfilAccesibilidad()
        );
    }

    // Helper — inyecta HttpServletRequest en el constructor del servicio
    private String obtenerIp() {
        return httpServletRequest.getRemoteAddr();
    }


    public boolean cambiarContrasena(String userid, String contrasenaActual, String nuevaCotrasena){
        Usuario usuario = repository.findByCorreo(userid).orElseThrow(()->new IllegalArgumentException("Usuario no encontrado"));




        if(!passwordEncoder.matches(contrasenaActual, usuario.getContrasenaHash())){
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        usuario.setContrasenaHash(passwordEncoder.encode(nuevaCotrasena));
        repository.save(usuario);

        return true;
    }

}
