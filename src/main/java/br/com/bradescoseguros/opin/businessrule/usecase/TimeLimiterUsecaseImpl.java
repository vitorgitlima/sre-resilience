package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.gateway.TimeLimiterGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeLimiterUsecaseImpl implements TimeLimiterUsecase {

    @Autowired
    private TimeLimiterGateway timeLimiterGateway;


    @Override
    public String externalApiCall() {
        return timeLimiterGateway.externalApiCallTimeLimiter();
    }

    @Override
    public String externalApiCallWithRetry() {
        return timeLimiterGateway.externalApiCallTimeLimiterWithRetry();
    }
}
