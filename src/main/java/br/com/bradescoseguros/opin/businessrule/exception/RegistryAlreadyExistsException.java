package br.com.bradescoseguros.opin.businessrule.exception;

import br.com.bradescoseguros.opin.businessrule.exception.BaseException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;

import java.util.Set;

public class RegistryAlreadyExistsException extends BaseException {

    public RegistryAlreadyExistsException() {
        super();
    }

    public RegistryAlreadyExistsException(final String message) {
        super(message);
    }

    public RegistryAlreadyExistsException(final Set<ErrorData> errors) {
        super(errors);
    }
}
