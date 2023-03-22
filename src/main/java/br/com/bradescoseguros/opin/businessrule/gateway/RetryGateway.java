package br.com.bradescoseguros.opin.businessrule.gateway;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

import java.util.Optional;

public interface RetryGateway {
    Optional<DemoSRE> findByIdWithRetry(final Integer id);

    String externalApiCallWithRetry(final ExtraStatusCode status);

    String externalApiCallWithRetryAndCircuitBreaker(ExtraStatusCode statusCode);
}
