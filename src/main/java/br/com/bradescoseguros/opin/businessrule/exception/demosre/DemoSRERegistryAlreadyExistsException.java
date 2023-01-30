package br.com.bradescoseguros.opin.businessrule.exception.demosre;

import br.com.bradescoseguros.opin.businessrule.exception.BaseException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;

import java.util.Set;

public class DemoSRERegistryAlreadyExistsException extends BaseException {

    public DemoSRERegistryAlreadyExistsException() {
        super();
    }

    public DemoSRERegistryAlreadyExistsException(final String message) {
        super(message);
    }

    public DemoSRERegistryAlreadyExistsException(final Set<ErrorData> errors) {
        super(errors);
    }
}
