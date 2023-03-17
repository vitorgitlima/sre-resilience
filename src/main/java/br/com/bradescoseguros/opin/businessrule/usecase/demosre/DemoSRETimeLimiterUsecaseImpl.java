package br.com.bradescoseguros.opin.businessrule.usecase.demosre;

import br.com.bradescoseguros.opin.businessrule.gateway.DemoSRETimeLimiterGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoSRETimeLimiterUsecaseImpl implements DemoSRETimeLimiterUsecase {

    @Autowired
    private DemoSRETimeLimiterGateway demoSRETimeLimiterGateway;


    @Override
    public String externalApiCall() {
        return demoSRETimeLimiterGateway.externalApiCallTimeLimiter();
    }

    @Override
    public String externalApiCallWithRetry() {
        return demoSRETimeLimiterGateway.externalApiCallTimeLimiterWithRetry();
    }
}
