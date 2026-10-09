package tech.gomesdev87.meubolso.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeExetionErro.class)
    public ResponseEntity<Map<String, String>> handleRuntimeExceptionErro(
            RuntimeExetionErro ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    // Erros de validação do DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new java.util.LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "error", "Erro de validação",
                "messages", errors
        ));
    }

    // Email ou senha incorretos
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(
            BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "status", 401,
                "error", "Não autorizado",
                "message", "Email ou senha inválidos."
        ));
    }

    // JSON inválido ou corpo malformado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidBody(
            HttpMessageNotReadableException ex) {

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "error", "Requisição inválida",
                "message", "Verifique o JSON enviado."
        ));
    }

    // Violação de restrições do banco
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(
            DataIntegrityViolationException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "status", 409,
                "error", "Conflito de dados",
                "message", "Os dados enviados violam uma restrição do banco."
        ));
    }

    // Erros inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "status", 500,
                        "error", "Erro interno",
                        "message", "Ocorreu um erro inesperado."
                ));
    }
}

