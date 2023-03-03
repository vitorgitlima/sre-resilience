package br.com.bradescoseguros.opin.domain.demosre;

import lombok.Getter;

import java.util.Arrays;

public enum ExtraStatusCode {
    OK("ok"),
    NOT_FOUND("nok404"),
    LOCKED("nok423"),
    TOO_EARLY("nok425"),
    INTERNAL_SERVER_ERROR("nok500"),
    GATEWAY_TIMEOUT("nok504"),
    BULKHEAD("bulkhead"),
    BULKHEAD_RETRY("bulkheadRetry"),
    BULKHEAD_THREAD_POOL("bulkheadThreadPool");


    @Getter
    private final String statusURL;

    ExtraStatusCode(String status) {
        this.statusURL = status;
    }

    public static ExtraStatusCode fromString(String statusStringValue) {
        return Arrays.stream(ExtraStatusCode.values())
                .filter(f -> f.getStatusURL().equalsIgnoreCase(statusStringValue))
                .findFirst()
                .orElse(ExtraStatusCode.NOT_FOUND);
    }
}
