package com.agimoveis.api.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public record TratadorDeErrosDTO(String campo, String mensagem) {
    public TratadorDeErrosDTO(org.springframework.validation.FieldError erro) {
        this(erro.getField(), erro.getDefaultMessage());
    }

    // No arquivo TratadorDeErros.java, adicione este método:
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }
}