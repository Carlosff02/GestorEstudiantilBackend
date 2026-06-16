package com.example.demo.service.implementation;

import com.example.demo.persistance.entity.AuditoriaAcceso;
import com.example.demo.persistance.repository.AuditoriaAccesoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditoriaAccesoService {
    private final AuditoriaAccesoRepository repository;

    public AuditoriaAcceso save(AuditoriaAcceso auditoriaAcceso){
        return repository.save(auditoriaAcceso);
    }
}
