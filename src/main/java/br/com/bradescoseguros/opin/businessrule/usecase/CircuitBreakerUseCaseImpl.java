package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.gateway.CircuitBreakerGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.businessrule.validator.DemoSREValidator;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ErrorEnum;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public ExecutionResult<DemoSRE> getDemoSREWithCircuitBreaker(final Integer id) {
        log.info("Iniciando fluxo de recuperação de objeto por id com Circuit Breaker");

        Optional<DemoSRE> demoSREOptional = gateway.findByIdWithCircuitBreaker(id);
        if(!demoSREOptional.isPresent()) {
            log.warn(messageSourceService.getMessage(NOT_FOUND));
            return ExecutionResult.<DemoSRE>builder().errorType(ErrorEnum.NOT_FOUND).build();
        }

        log.info("Finalizando fluxo de recuperação de objeto por id com Circuit Breaker");
        return ExecutionResult.<DemoSRE>builder().object(demoSREOptional.get()).build();
    }

    @Override
    public String externalApiCallWithCircuitBreaker(final ExtraStatusCode status) {
        log.info("Iniciando fluxo de consulta por status com Circuit Breaker");

        String value = gateway.externalApiCallWithCircuitBreaker(status);

        log.info("Finalizando fluxo de consulta por status com Circuit Breaker");
        return value;
    }
}
