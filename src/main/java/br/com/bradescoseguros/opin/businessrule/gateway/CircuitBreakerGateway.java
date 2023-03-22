package br.com.bradescoseguros.opin.businessrule.gateway;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

import java.util.Optional;

public interface CircuitBreakerGateway {
    Optional<DemoSRE> findByIdWithCircuitBreaker(final Integer id);
    String externalApiCallWithCircuitBreaker(final ExtraStatusCode status);

}
