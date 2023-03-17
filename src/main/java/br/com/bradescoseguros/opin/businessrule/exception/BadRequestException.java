package br.com.bradescoseguros.opin.businessrule.exception;

import br.com.bradescoseguros.opin.businessrule.exception.BaseException;

public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(message);
    }

}
