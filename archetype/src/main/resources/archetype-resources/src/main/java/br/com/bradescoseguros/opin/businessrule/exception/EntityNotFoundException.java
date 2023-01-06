package br.com.bradescoseguros.opin.businessrule.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EntityNotFoundException extends RuntimeException {

    private final String identifier;
    private final String type;

    public EntityNotFoundException(final String type, final String identifier, final String message) {
        super(message);
        this.type = type;
        this.identifier = identifier;
    }

    public EntityNotFoundException(final String type, final String identifier) {
        this(type, identifier, null);
    }
}
