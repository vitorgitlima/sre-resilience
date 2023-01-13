package br.com.bradescoseguros.opin.businessrule.exception.demosre;

import br.com.bradescoseguros.opin.businessrule.exception.RetriableBaseException;

public class DemoSRENoContentException extends RetriableBaseException {
    public DemoSRENoContentException(final String message) {
        super(message);
    }
}
