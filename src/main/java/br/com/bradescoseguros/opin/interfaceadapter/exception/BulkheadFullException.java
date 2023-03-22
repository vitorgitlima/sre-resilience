package br.com.bradescoseguros.opin.interfaceadapter.exception;

import br.com.bradescoseguros.opin.businessrule.exception.BaseException;

public class BulkheadFullException extends BaseException {

    public BulkheadFullException() {
        super();
    }

    public BulkheadFullException(final String message) {
        super(message);
    }
}
