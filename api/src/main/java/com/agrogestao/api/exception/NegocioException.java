package com.agrogestao.api.exception;

// "extends RuntimeException" faz com que o Java entenda que isso é um ERRO
public class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message); // Passa a mensagem para a classe pai do Java
    }
}