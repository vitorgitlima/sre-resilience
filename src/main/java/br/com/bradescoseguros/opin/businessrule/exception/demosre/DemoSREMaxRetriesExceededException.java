package br.com.bradescoseguros.opin.businessrule.exception.demosre;

import br.com.bradescoseguros.opin.businessrule.exception.RetriableBaseException;

public class DemoSREMaxRetriesExceededException extends RetriableBaseException {

    public DemoSREMaxRetriesExceededException() {
        super();
    }
    public DemoSREMaxRetriesExceededException(final String message) {
        super(message);
    }
}
