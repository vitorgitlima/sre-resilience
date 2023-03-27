package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;

public interface TimeLimiterUsecase {

    DemoSRE getDemoSRE();
    String externalApiCallWithTimeLimiter();
    String externalApiCallWithTimeLimiterAndRetry();

}
