package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

public interface RetryUseCase {
    DemoSRE getDemoSREWithRetry(final Integer id) throws Throwable;
    String externalApiCallWithRetry(final ExtraStatusCode status);
    String externalApiCallWithRetryAndCircuitBreaker(ExtraStatusCode status);
}
