package com.agrogestao.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Erro de Validação (@Valid) - Ex: Nome em branco
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(f -> errors.put(f.getField(), f.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // 2. Recurso Não Encontrado - Retorna 404
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ApiError> handleNotFound(EntidadeNaoEncontradaException ex, HttpServletRequest request) {
        ApiError error = new ApiError(LocalDateTime.now(), 404, "Não Encontrado", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 3. Erro de Negócio (Duplicidade) - Retorna 400 ou 409
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ApiError> handleNegocio(NegocioException ex, HttpServletRequest request) {
        ApiError error = new ApiError(LocalDateTime.now(), 400, "Regra de Negócio", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.badRequest().body(error);
    }

    // 4. Erro Genérico (Coringa) - Retorna 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex, HttpServletRequest request) {
        ApiError error = new ApiError(LocalDateTime.now(), 500, "Erro Interno", "Ocorreu um erro inesperado no servidor.", request.getRequestURI());
        return ResponseEntity.internalServerError().body(error);
    }
}