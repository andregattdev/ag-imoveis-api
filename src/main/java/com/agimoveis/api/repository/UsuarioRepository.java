package com.agimoveis.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agimoveis.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método essencial para o Login e para validar e-mails duplicados
    Optional<Usuario> findByEmail(String email);

}
