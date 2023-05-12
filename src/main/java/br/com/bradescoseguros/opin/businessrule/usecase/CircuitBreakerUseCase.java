package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

public interface CircuitBreakerUseCase {
    ExecutionResult<DemoSRE> getDemoSREWithCircuitBreaker(final Integer id);
    String externalApiCallWithCircuitBreaker(final ExtraStatusCode status);
}
