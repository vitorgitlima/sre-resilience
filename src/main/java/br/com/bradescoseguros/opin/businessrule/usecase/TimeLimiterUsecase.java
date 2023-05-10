package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;

public interface TimeLimiterUsecase {

    DemoSRE getDemoSRE(Integer id) throws Throwable;
    String externalApiCallWithTimeLimiter();
    String externalApiCallWithTimeLimiterAndRetry();

}
