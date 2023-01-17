package br.com.bradescoseguros.opin.businessrule.exception;

import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class RetriableBaseException extends RuntimeException {
    static final long serialVersionUID = 8229384672887028287L;

    @Getter
    private Set<ErrorData> errors;

    public RetriableBaseException() {
        super();
    }

    public RetriableBaseException(final Set<ErrorData> errors) {
        this.errors = errors;
    }

    public RetriableBaseException(final ErrorData... errors) {
        this.errors = Arrays.stream(errors).collect(Collectors.toSet());
    }

    public RetriableBaseException(final String message) {
        super(message);
    }

    public RetriableBaseException(final String message, final Set<ErrorData> errors) {
        super(message);
        this.errors = errors;
    }

    public RetriableBaseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RetriableBaseException(final String message, final Throwable cause, final Set<ErrorData> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public RetriableBaseException(final Throwable cause) {
        super(cause);
    }

    public RetriableBaseException(final Throwable cause, final Set<ErrorData> errors) {
        super(cause);
        this.errors = errors;
    }
}
