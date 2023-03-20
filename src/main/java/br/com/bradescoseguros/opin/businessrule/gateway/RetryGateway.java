package br.com.bradescoseguros.opin.businessrule.gateway;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

import java.util.Optional;

public interface RetryGateway {
    Optional<DemoSRE> findById(final Integer id);

    String externalApiCall(final ExtraStatusCode status);

    String externalApiCallWithCircuitBreaker(ExtraStatusCode statusCode);
}
