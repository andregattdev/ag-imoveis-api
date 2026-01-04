package com.agimoveis.api.model;

import java.time.LocalDateTime;

import com.agimoveis.api.model.enums.Finalidade;
import com.agimoveis.api.model.enums.TipoImovel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Imovel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;


    private Double preco;

    private Integer quartos;
    private Integer banheiros;
    private Double area;

    private String cidade;
    private String bairro;
    private String logradouro;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoImovel tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Finalidade finalidade;

    private String urlImagemPrincipal;
    
    private LocalDateTime dataCadastro = LocalDateTime.now();
    
    private boolean ativo = true; // Todo imóvel novo começa como ativo

	

}
