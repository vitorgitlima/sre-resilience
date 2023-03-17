package br.com.bradescoseguros.opin.interfaceadapter.exception;

import br.com.bradescoseguros.opin.businessrule.exception.RetriableBaseException;

public class MaxRetriesExceededException extends RetriableBaseException {

    public MaxRetriesExceededException() {
        super();
    }
    public MaxRetriesExceededException(final String message) {
        super(message);
    }
}
