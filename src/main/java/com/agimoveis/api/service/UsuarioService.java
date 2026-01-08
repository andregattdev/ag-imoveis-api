package com.agimoveis.api.service;

import com.agimoveis.api.model.Usuario;
import com.agimoveis.api.repository.UsuarioRepository;
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

    /**
     * Método para criar um novo usuário
     * 
     * @param usuario
     * @return Retorna um usuario criado no banco de dados
     */
    public Usuario criar(Usuario usuario) {
        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }

        // CRIPTOGRAFIA AQUI:
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return repository.save(usuario);
    }

    /**
     * Método para listar todos os usuários
     * 
     * @return Retorna uma lista com todos os usuários
     */
    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    /**
     * Método para buscar um usuário pelo ID
     * 
     * @param id
     * @return Retorna o usuário encontrado ou lança uma exceção se não encontrado
     */
    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    // Atualizar
    public Usuario atualizar(Long id, Usuario dadosNovos) {
        Usuario usuarioExistente = buscarPorId(id);

        usuarioExistente.setNome(dadosNovos.getNome());
        usuarioExistente.setEmail(dadosNovos.getEmail());
        usuarioExistente.setRole(dadosNovos.getRole());

        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isEmpty()) {
            usuarioExistente.setSenha(dadosNovos.getSenha());
        }

        return repository.save(usuarioExistente);
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com este e-mail."));
    }

    /**
     * Método para deletar um usuário pelo ID
     * 
     * @param id
     * @return Retorna usuario deletado
     */
    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        repository.delete(usuario);
    }
}