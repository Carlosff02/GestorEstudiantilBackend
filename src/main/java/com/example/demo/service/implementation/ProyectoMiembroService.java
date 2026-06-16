package com.example.demo.service.implementation;

import com.example.demo.persistance.entity.ProyectoMiembro;
import com.example.demo.persistance.entity.Proyecto;
import com.example.demo.persistance.entity.Usuario;
import com.example.demo.persistance.repository.ProyectoMiembroRepository;
import com.example.demo.persistance.repository.ProyectoRepository;
import com.example.demo.persistance.repository.UsuarioRepository;
import com.example.demo.presentation.dtos.GuardarProyectoMiembroRequest;
import com.example.demo.presentation.dtos.ProyectoInvitacionResponse;
import com.example.demo.presentation.dtos.ProyectoMiembroResponse;
import com.example.demo.presentation.dtos.ProyectoResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoMiembroService {

    private final ProyectoMiembroRepository repository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private  final EmailService emailService;

    public List<ProyectoMiembroResponse> obtenerPorProyecto(Long proyectoId) {
        return repository.findByProyectoIdProyectoAndAceptadoTrue(proyectoId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProyectoMiembroResponse guardar(GuardarProyectoMiembroRequest request) {
        Proyecto proyecto = proyectoRepository.findById(request.getIdProyecto())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ProyectoMiembro miembro = new ProyectoMiembro();
        miembro.setRol(request.getRol());
        miembro.setFechaAsignacion(LocalDate.now());
        miembro.setProyecto(proyecto);
        miembro.setUsuario(usuario);
        miembro.setAceptado(false);

        ProyectoMiembro guardado = repository.save(miembro);
        String destinatario = miembro.getUsuario().getCorreo();
        String subject = "El proyecto  "+guardado.getProyecto().getNombre()+" ha sido asignado a su usuario";
        String mensaje = """
Ingrese a la plataforma para aceptarlo.

Usuario: %s

""".formatted(
                miembro.getUsuario().getNombres()
        );
        // emailService.enviarCorreoAsync(destinatario,subject,mensaje);
        return convertirAResponse(guardado);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private ProyectoMiembroResponse convertirAResponse(ProyectoMiembro miembro) {
        return new ProyectoMiembroResponse(
                miembro.getIdMiembro(),
                miembro.getRol(),
                miembro.getFechaAsignacion(),
                miembro.getProyecto().getIdProyecto(),
                miembro.getUsuario().getIdUsuario(),
                miembro.getUsuario().getNombres(),
                miembro.getUsuario().getApellidos()
        );
    }

    public List<ProyectoInvitacionResponse> obtenerInvitaciones(Long usuarioId) {
        return repository.findInvitacionesPendientesByUsuarioId(usuarioId);
    }

    public ProyectoMiembroResponse aceptar(Long id, Long idUsuario) {
        ProyectoMiembro miembro = repository.findByProyectoIdProyectoAndUsuarioIdUsuario(id, idUsuario).orElseThrow(()->new IllegalArgumentException("Proyecto miembro no encontrado"));
        miembro.setAceptado(true);
        repository.save(miembro);
        return new ProyectoMiembroResponse(
                miembro.getIdMiembro(),
                miembro.getRol(),
                miembro.getFechaAsignacion(),
                miembro.getProyecto().getIdProyecto(),
                miembro.getUsuario().getIdUsuario(),
                miembro.getUsuario().getNombres(),
                miembro.getUsuario().getApellidos()
        );
    }
}
