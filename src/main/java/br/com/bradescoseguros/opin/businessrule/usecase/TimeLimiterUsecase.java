package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExecutionResult;

public interface TimeLimiterUsecase {

    ExecutionResult<DemoSRE> getDemoSRE(Integer id);
    String externalApiCallWithTimeLimiter();
    String externalApiCallWithTimeLimiterAndRetry();

}
