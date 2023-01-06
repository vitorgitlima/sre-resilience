package br.com.bradescoseguros.opin.businessrule.exception;

import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorCode;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Set;

@ToString
public class ValidationException extends RuntimeException {
    private final Set<ErrorData> errors;

    public ValidationException(final Set<ErrorData> errors) {
        this(null, null, errors);
    }

    public ValidationException(final String message, final Set<ErrorData> errors) {
        this(message, null, errors);
    }

    public ValidationException(final Throwable cause, final Set<ErrorData> errors) {
        this(null, cause, errors);
    }

    public ValidationException(final String message, final Throwable cause, final Set<ErrorData> errors) {
        super(message, cause);
        Assert.notNull(errors, "Errors List must not be null");
        errors.forEach(error -> error.setTitle(ErrorCode.INVALID_PARAMETER + " " + error.getTitle()));
        this.errors = errors;
    }

    public Set<ErrorData> getErrors() {
        return this.errors;
    }
}

