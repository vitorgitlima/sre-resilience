package br.com.bradescoseguros.opin.domain.demosre;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ExtraStatusCode {
    OK("ok"),
    LOCKED("nok423"),
    TOO_EARLY("nok425"),
    INTERNAL_SERVER_ERROR("nok500"),
    GATEWAY_TIMEOUT("nok504");

    @Getter
    private final String statusURL;

    ExtraStatusCode(String status) {
        this.statusURL = status;
    }

    public static ExtraStatusCode fromString(String statusStringValue) {
        return Arrays.stream(ExtraStatusCode.values())
                .filter(f -> f.getStatusURL().equalsIgnoreCase(statusStringValue))
                .findFirst()
                .orElse(null);
    }
}
