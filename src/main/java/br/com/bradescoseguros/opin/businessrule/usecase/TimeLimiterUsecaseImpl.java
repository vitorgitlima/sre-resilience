package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.TimeLimiterGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TimeLimiterUsecaseImpl implements TimeLimiterUsecase {

    @Autowired
    private TimeLimiterGateway timeLimiterGateway;

    @Autowired
    private MessageSourceService messageSourceService;

    private static final String NOT_FOUND = "demo-sre.id-not-found";


    @Override
    public DemoSRE getDemoSRE(Integer id) throws Throwable{
        log.info("Iniciando fluxo de recuperação de objeto por id com Time Limiter");

        DemoSRE demoSRE = timeLimiterGateway.findByIdWithTimeLimiter(id).orElseThrow(() -> {
            log.warn(messageSourceService.getMessage(NOT_FOUND));

            throw new NoContentException(messageSourceService.getMessage(NOT_FOUND));
        });

        log.info("Finalizando fluxo de recuperação de objeto por id com Time Limiter");
        return demoSRE;
    }

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
