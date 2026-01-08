package com.agimoveis.api.service;

import com.agimoveis.api.model.Usuario;
import com.agimoveis.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException; // Importante
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario criar(Usuario usuario) {
        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            
            throw new RuntimeException("E-mail já cadastrado!"); 
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return repository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                // Mudamos para EntityNotFoundException para o Tratador capturar 404
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));
    }

    public Usuario atualizar(Long id, Usuario dadosNovos) {
        Usuario usuarioExistente = buscarPorId(id);

        usuarioExistente.setNome(dadosNovos.getNome());
        usuarioExistente.setEmail(dadosNovos.getEmail());
        usuarioExistente.setRole(dadosNovos.getRole());

        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isEmpty()) {
           
            usuarioExistente.setSenha(passwordEncoder.encode(dadosNovos.getSenha()));
        }

        return repository.save(usuarioExistente);
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com este e-mail."));
    }

    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        repository.delete(usuario);
    }
}