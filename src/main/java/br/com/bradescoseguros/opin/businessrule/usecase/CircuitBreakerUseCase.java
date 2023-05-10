package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

public interface CircuitBreakerUseCase {
    DemoSRE getDemoSREWithCircuitBreaker(final Integer id) throws Throwable;
    String externalApiCallWithCircuitBreaker(final ExtraStatusCode status);
}
