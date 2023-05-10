package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.TimeLimiterGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ErrorEnum;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TimeLimiterUsecaseImpl implements TimeLimiterUsecase {

    @Autowired
    private TimeLimiterGateway timeLimiterGateway;

    @Autowired
    private MessageSourceService messageSourceService;

    private static final String NOT_FOUND = "demo-sre.id-not-found";


    @Override
    public ExecutionResult<DemoSRE> getDemoSRE(Integer id) {
        log.info("Iniciando fluxo de recuperação de objeto por id com Time Limiter");

        Optional<DemoSRE> demoSREOptional = timeLimiterGateway.findByIdWithTimeLimiter(id);
        if(demoSREOptional.isEmpty()) {
            log.warn(messageSourceService.getMessage(NOT_FOUND));
            return ExecutionResult.<DemoSRE>builder().errorType(ErrorEnum.NOT_FOUND).build();
        }


        log.info("Finalizando fluxo de recuperação de objeto por id com Time Limiter");
        return ExecutionResult.<DemoSRE>builder().object(demoSREOptional.get()).build();
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
