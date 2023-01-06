package br.com.bradescoseguros.opin.businessrule.exception;


import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class BaseException extends RuntimeException {

    static final long serialVersionUID = -9034816190742716963L;

    @Getter
    private Set<ErrorData> errors;

    public BaseException() {
        super();
    }

    public BaseException(final Set<ErrorData> errors) {
        this.errors = errors;
    }

    public BaseException(final ErrorData... errors) {
        this.errors = Arrays.stream(errors).collect(Collectors.toSet());
    }

    public BaseException(final String message) {
        super(message);
    }

    public BaseException(final String message, final Set<ErrorData> errors) {
        super(message);
        this.errors = errors;
    }

    public BaseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BaseException(final String message, final Throwable cause, final Set<ErrorData> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public BaseException(final Throwable cause) {
        super(cause);
    }

    public BaseException(final Throwable cause, final Set<ErrorData> errors) {
        super(cause);
        this.errors = errors;
    }

}
