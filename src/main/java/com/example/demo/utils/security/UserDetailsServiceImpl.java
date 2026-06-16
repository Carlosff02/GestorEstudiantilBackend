package com.example.demo.utils.security;



import com.example.demo.persistance.entity.Usuario;
import com.example.demo.persistance.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {

            Optional<Usuario> usuario = usuarioRepository.findByCorreo(username);
            if(usuario.isPresent()){
                return usuario.get();
            }
            throw new RuntimeException("Usuario no encontrado");


                    } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
