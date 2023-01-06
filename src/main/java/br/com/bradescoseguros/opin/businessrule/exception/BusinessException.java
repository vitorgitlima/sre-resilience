package br.com.bradescoseguros.opin.businessrule.exception;

import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class BusinessException extends BaseException {

    static final long serialVersionUID = -4034896190745716968L;

    public BusinessException() {
        super();
    }

    public BusinessException(final Set<ErrorData> errors) {
        super(errors);
    }

    public BusinessException(final String message, final Set<ErrorData> errors) {
        super(message, errors);
    }

    public BusinessException(final String message) {
        super(message);
    }

    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BusinessException(final Throwable cause) {
        super(cause);
    }

}
