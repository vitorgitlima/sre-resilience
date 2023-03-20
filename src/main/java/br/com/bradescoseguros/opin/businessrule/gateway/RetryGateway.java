package br.com.bradescoseguros.opin.businessrule.gateway;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.Optional;

public interface RetryGateway {
    Optional<DemoSRE> findById(final Integer id);

    String externalApiCall(final ExtraStatusCode status);

    String externalApiCallWithCircuitBreaker(ExtraStatusCode statusCode);
}
