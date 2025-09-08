package br.dev.joaobarbosa.error_handling.config.exceptions.server;

import br.dev.joaobarbosa.error_handling.config.exceptions.BaseHttpException;
import org.springframework.http.HttpStatus;

public abstract class BaseServerException extends BaseHttpException {
    protected BaseServerException(String message, HttpStatus status, String action, String name) {
        super(message, status, action, name);
        if (!status.is5xxServerError()) {
            throw new IllegalArgumentException("ServerErrorException deve usar um status 5xx");
        }
    }
}
