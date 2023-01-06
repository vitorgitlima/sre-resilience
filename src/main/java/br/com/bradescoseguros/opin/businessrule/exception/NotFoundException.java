package br.com.bradescoseguros.opin.businessrule.exception;


import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;

import java.util.Set;

public class NotFoundException extends BaseException {

    public NotFoundException(final Set<ErrorData> errors) {
        super(errors);
    }

    public NotFoundException(final String message) {
        super(message);
    }

}
