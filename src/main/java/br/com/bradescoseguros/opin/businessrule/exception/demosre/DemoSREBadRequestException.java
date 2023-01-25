package br.com.bradescoseguros.opin.businessrule.exception.demosre;

import br.com.bradescoseguros.opin.businessrule.exception.BaseException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;

import java.util.Set;

public class DemoSREBadRequestException extends BaseException {

    public DemoSREBadRequestException() {
        super();
    }

    public DemoSREBadRequestException(final String message) {
        super(message);
    }

    public DemoSREBadRequestException(final Set<ErrorData> errors) {
        super(errors);
    }
}
