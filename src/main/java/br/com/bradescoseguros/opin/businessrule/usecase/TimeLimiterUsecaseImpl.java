package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.gateway.TimeLimiterGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TimeLimiterUsecaseImpl implements TimeLimiterUsecase {

    @Autowired
    private TimeLimiterGateway timeLimiterGateway;


    @Override
    public String externalApiCallWithTimeLimiter() {
        log.info("Iniciando fluxo de consulta por status com Time Limiter");

        String value = timeLimiterGateway.externalApiCallWithTimeLimiter();

        log.info("Finalizando fluxo de consulta por status com Time Limiter");
        return value;
    }

    @Override
    public String externalApiCallWithTimeLimiterAndRetry() {
        log.info("Iniciando fluxo de consulta por status com Time Limiter e Retry");

        String value = timeLimiterGateway.externalApiCallWithTimeLimiterAndRetry();

        log.info("Finalizando fluxo de consulta por status com Time Limiter e Retry");
        return value;
    }
}
