package br.com.bradescoseguros.opin.businessrule.exception.demosre;

import br.com.bradescoseguros.opin.businessrule.exception.BaseException;

public class DemoSREBulkheadFullException extends BaseException {

    public DemoSREBulkheadFullException() {
        super();
    }

    public DemoSREBulkheadFullException(final String message) {
        super(message);
    }
}
