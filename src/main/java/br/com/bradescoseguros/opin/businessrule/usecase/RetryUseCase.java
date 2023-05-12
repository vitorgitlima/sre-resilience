package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

public interface RetryUseCase {
    ExecutionResult<DemoSRE> getDemoSREWithRetry(final Integer id);
    String externalApiCallWithRetry(final ExtraStatusCode status);
    String externalApiCallWithRetryAndCircuitBreaker(ExtraStatusCode status);
}
