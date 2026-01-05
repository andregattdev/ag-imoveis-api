package com.agimoveis.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura erros de validação (@NotBlank, @Valid, etc)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            erros.put(fieldName, errorMessage);
        });

        ErroResposta erro = new ErroResposta(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos campos",
                LocalDateTime.now(),
                erros
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    // Captura quando o ficheiro de imagem é maior que o permitido
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErroResposta> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        ErroResposta erro = new ErroResposta(
                HttpStatus.BAD_REQUEST.value(),
                "O ficheiro é demasiado grande! O limite é 5MB.",
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    // Captura qualquer outro erro genérico (RuntimeException)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResposta> handleRuntimeExceptions(RuntimeException ex) {
        ErroResposta erro = new ErroResposta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}