package br.com.bradescoseguros.opin.businessrule.exception;

import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class GatewayException extends BaseException {

    static final long serialVersionUID = -1033896140745516769L;

    public GatewayException() {
        super();
    }

    public GatewayException(final String message, final Set<ErrorData> errors) {
        super(message, errors);
    }

    public GatewayException(final Set<ErrorData> errors) {
        super(errors);
    }

    public GatewayException(final ErrorData... errors) {
        super(errors);
    }

    public GatewayException(final String message) {
        super(message);
    }

    public GatewayException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public GatewayException(final Throwable cause) {
        super(cause);
    }

}
