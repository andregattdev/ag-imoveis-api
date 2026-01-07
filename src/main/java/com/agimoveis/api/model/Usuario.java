package com.agimoveis.api.model;

import com.agimoveis.api.model.enums.TipoUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "usuarios") // Define o nome da tabela no MySQL
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(unique = true, nullable = false) // Impede e-mails duplicados no banco
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    @Enumerated(EnumType.STRING) // Salva como "ADMIN", "CORRETOR" no banco
    @Column(nullable = false)
    private TipoUsuario role;

    // Construtor padrão (necessário para o JPA)
    public Usuario() {}

    // Construtor auxiliar para facilitar testes
    public Usuario(String nome, String email, String senha, TipoUsuario role) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
    }
}