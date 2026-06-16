package com.example.demo.persistance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import java.time.OffsetDateTime;

@Entity
@Table(name = "auditoria_accesos")
@Immutable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuario")
public class AuditoriaAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long idAuditoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;


    @Column(name = "correo_intentado", length = 150)
    private String correoIntentado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento", nullable = false, columnDefinition = "tipo_evento_acceso")
    private TipoEventoAcceso tipoEvento;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado", nullable = false, columnDefinition = "resultado_acceso")
    private ResultadoAcceso resultado;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_fallo", columnDefinition = "motivo_fallo_acceso")
    private MotivoFalloAcceso motivoFallo;

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;

    @Column(name = "ip_origen", nullable = false, length = 45)
    private String ipOrigen;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "intentos_fallidos_consecutivos", nullable = false)
    @Builder.Default
    private Integer intentosFallidosConsecutivos = 0;

    @CreationTimestamp
    @Column(name = "fecha", nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime fecha;

    public enum TipoEventoAcceso {
        login, logout, login_2fa,
        cambio_contrasena, recuperacion_contrasena,
        bloqueo_cuenta, desbloqueo_cuenta,
        token_expirado, sesion_expirada
    }

    public enum ResultadoAcceso {
        exitoso, fallido
    }

    public enum MotivoFalloAcceso {
        contrasena_incorrecta, usuario_no_existe,
        cuenta_inactiva, cuenta_bloqueada,
        token_invalido, token_expirado,
        sesion_invalida, demasiados_intentos,
        ip_bloqueada, error_interno
    }
}

