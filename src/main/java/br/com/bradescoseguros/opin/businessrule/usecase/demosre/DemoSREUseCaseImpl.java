package br.com.bradescoseguros.opin.businessrule.usecase.demosre;

import br.com.bradescoseguros.opin.businessrule.exception.NotFoundException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSREBadRequestException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSRENoContentException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSRERegistryAlreadyExistsException;
import br.com.bradescoseguros.opin.businessrule.gateway.DemoSREGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.businessrule.validator.DemoSREValidator;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.domain.demosre.ExtraStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class DemoSREUseCaseImpl implements DemoSREUseCase {

    @Autowired
    private DemoSREGateway gateway;

    @Autowired
    private DemoSREValidator validator;

    @Autowired
    private MessageSourceService messageSourceService;

    private static final String NOT_FOUND = "demo-sre.id-not-found";

    @Override
    public DemoSRE getDemoSRE(final Integer id) {
        return gateway.findById(id).orElseThrow(() -> {
            log.warn(messageSourceService.getMessage(NOT_FOUND));

            throw new DemoSRENoContentException(messageSourceService.getMessage(NOT_FOUND));
        });
    }

    @Override
    public void insertDemoSRE(final DemoSRE payload) {
        validator.execute(payload);

        if (gateway.findById(payload.getId()).isPresent()) {
            String error = "O ID informado na inserção DemoSRE já existe na base de dados";
            log.warn(error);

            throw new DemoSRERegistryAlreadyExistsException(error);
        }

        gateway.insertDemoSRE(payload);
    }

    @Override
    public void updateDemoSRE(final DemoSRE payload) {
        validator.execute(payload);

        if (gateway.findById(payload.getId()).isEmpty()) {
            log.warn(messageSourceService.getMessage(NOT_FOUND));

            throw new NotFoundException(messageSourceService.getMessage(NOT_FOUND));
        }

        gateway.updateDemoSRE(payload);
    }

    @Override
    public void removeDemoSRE(final Integer id) {

        if (gateway.findById(id).isEmpty()) {
            log.warn(messageSourceService.getMessage(NOT_FOUND));

            throw new NotFoundException(messageSourceService.getMessage(NOT_FOUND));
        }

        gateway.removeDemoSRE(id);
    }

    @Override
    public String externalApiCall(final ExtraStatusCode status) {
        if (Objects.isNull(status)) {
            String error = "O status informado não é suportado pela aplicação.";
            log.warn(error);

            throw new DemoSREBadRequestException(error);
        }

        return getExternalApi(status);
    }

    private String getExternalApi(ExtraStatusCode status) {

        log.info("Status: " + status.getStatusURL());

        String statusURL = status.getStatusURL();
        String bulkheadUrl = ExtraStatusCode.BULKHEAD.getStatusURL();
        String bulkheadRetryUrl = ExtraStatusCode.BULKHEAD_RETRY.getStatusURL();
        String bulkheadThreadPool = ExtraStatusCode.BULKHEAD_THREAD_POOL.getStatusURL();

        if (statusURL.equals(bulkheadUrl)) {
            log.info("Calling Bulkhead");
            return gateway.externalApiCallBulkhead();
        }
        if (statusURL.equals(bulkheadRetryUrl)) {
            log.info("Calling BulkheadRetry");
            return gateway.externalApiCallBulkheadRetry();
        }
        if(statusURL.equals(bulkheadThreadPool)) {
            log.info("Calling ThreadPoolBulkhead");
            return gateway.externalApiCallThreadPoolBulkhead();
        }

        log.info("Calling ExternalApiCall");
        return gateway.externalApiCall(status);
    }
}
