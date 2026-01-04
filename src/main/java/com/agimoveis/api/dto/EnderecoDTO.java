package com.agimoveis.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EnderecoDTO {

    private String cep;
    private String logradouro;
    private String bairro;
    @JsonProperty("localidade")
    private String localidade; 
    private String numero;
    private String uf;
}
