package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.exception.BadRequestException;
import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.CircuitBreakerGateway;
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
public class CircuitBreakerUseCaseImpl implements CircuitBreakerUseCase {

    @Autowired
    private CircuitBreakerGateway gateway;

    @Autowired
    private DemoSREValidator validator;

    @Autowired
    private MessageSourceService messageSourceService;

    private static final String NOT_FOUND = "demo-sre.id-not-found";

    @Override
    public DemoSRE getDemoSREWithCircuitBreaker(final Integer id)throws Throwable {
        log.info("Iniciando fluxo de recuperação de objeto por id com Circuit Breaker");

        DemoSRE demoSRE = gateway.findByIdWithCircuitBreaker(id).orElseThrow(() -> {
            log.warn(messageSourceService.getMessage(NOT_FOUND));

            throw new NoContentException(messageSourceService.getMessage(NOT_FOUND));
        });

        log.info("Finalizando fluxo de recuperação de objeto por id com Circuit Breaker");
        return demoSRE;
    }

    @Override
    public String externalApiCallWithCircuitBreaker(final ExtraStatusCode status) {
        log.info("Iniciando fluxo de consulta por status com Circuit Breaker");

        if (Objects.isNull(status)) {
            String error = "O status informado não é suportado pela aplicação.";
            log.warn(error);

            throw new BadRequestException(error);
        }

        log.info("Finalizando fluxo de consulta por status com Circuit Breaker");
        return gateway.externalApiCallWithCircuitBreaker(status);
    }
}
