package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.exception.BadRequestException;
import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.RetryGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.businessrule.validator.DemoSREValidator;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class RetryUseCaseImpl implements RetryUseCase {

    @Autowired
    private RetryGateway gateway;

    @Autowired
    private DemoSREValidator validator;

    @Autowired
    private MessageSourceService messageSourceService;

    private static final String NOT_FOUND = "demo-sre.id-not-found";

    @Override
    public DemoSRE getDemoSREWithRetry(final Integer id) throws Throwable {
        log.info("Iniciando fluxo de recuperação de objeto por id com Retry");

        DemoSRE demoSRE = gateway.findByIdWithRetry(id).orElseThrow(() -> {
            log.warn(messageSourceService.getMessage(NOT_FOUND));

            throw new NoContentException(messageSourceService.getMessage(NOT_FOUND));
        });

        log.info("Finalizando fluxo de recuperação de objeto por id com Retry");
        return demoSRE;
    }

    @Override
    public String externalApiCallWithRetry(final ExtraStatusCode status) {
        log.info("Iniciando fluxo de consulta por status com Retry");

        validaStatus(status);

        String value = gateway.externalApiCallWithRetry(status);

        log.info("Finalizando fluxo de consulta por status com Retry");
        return value;
    }


    @Override
    public String externalApiCallWithRetryAndCircuitBreaker(final ExtraStatusCode status) {
        log.info("Iniciando fluxo de consulta por status com Retry e Circuit Breaker");

        validaStatus(status);

        String value = gateway.externalApiCallWithRetryAndCircuitBreaker(status);

        log.info("Finalizando fluxo de consulta por status com Retry e Circuit Breaker");
        return value;
    }

    private static void validaStatus(ExtraStatusCode status) {
        if (Objects.isNull(status)) {
            String error = "O status informado não é suportado pela aplicação.";
            log.warn(error);

            throw new BadRequestException(error);
        }
    }
}
