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
    public DemoSRE getDemoSREWithRetry(final Integer id) {
        log.info("Calling findById with id {}", id);

        return gateway.findByIdWithRetry(id).orElseThrow(() -> {
            log.warn(messageSourceService.getMessage(NOT_FOUND));

            throw new NoContentException(messageSourceService.getMessage(NOT_FOUND));
        });
    }

    @Override
    public String externalApiCallWithRetry(final ExtraStatusCode status) {
        validaStatus(status);

        log.info("Calling ExternalApiCall");
        return gateway.externalApiCallWithRetry(status);
    }


    @Override
    public String externalApiCallWithRetryAndCircuitBreaker(final ExtraStatusCode status) {
        validaStatus(status);

        log.info("Calling ExternalApiCall With Circuit Breaker");
        return gateway.externalApiCallWithRetryAndCircuitBreaker(status);
    }

    private static void validaStatus(ExtraStatusCode status) {
        if (Objects.isNull(status)) {
            String error = "O status informado não é suportado pela aplicação.";
            log.warn(error);

            throw new BadRequestException(error);
        }
    }
}
