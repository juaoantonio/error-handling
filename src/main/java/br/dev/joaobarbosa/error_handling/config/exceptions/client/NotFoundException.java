package br.dev.joaobarbosa.error_handling.config.exceptions.client;

import org.springframework.http.HttpStatus;

public final class NotFoundException extends BaseClientException {
    public NotFoundException() {
        super("Não foi possível encontrar este recurso no sistema.", HttpStatus.NOT_FOUND, "Verifique se os parâmetros enviados na consulta estão certos.", "NotFoundException");
    }

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "Verifique se os parâmetros enviados na consulta estão certos.", "NotFoundException");
    }

    public NotFoundException(String message, String action) {
        super(message, HttpStatus.NOT_FOUND, action, "NotFoundException");
    }
}