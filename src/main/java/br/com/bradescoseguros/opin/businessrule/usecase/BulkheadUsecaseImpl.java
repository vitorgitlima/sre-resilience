package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.gateway.BulkheadGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BulkheadUsecaseImpl implements BulkheadUsecase {

    @Autowired
    private BulkheadGateway bulkheadGateway;

    @Override
    public String externalApiCallWithSemaphoreBulkhead() {
        log.info("Iniciando fluxo de chamada externa com Bulkhead");

        String value = bulkheadGateway.externalApiCallWithBulkhead();

        log.info("Finalizando fluxo de chamada externa com Bulkhead");
        return value;
    }

    @Override
    public String externalApiCallWithSemaphoreBulkheadAndRetry() {
        log.info("Iniciando fluxo de chamada externa com Bulkhead e Retry");

        String value = bulkheadGateway.externalApiCallWithBulkheadAndRetry();

        log.info("Finalizando fluxo de chamada externa com Bulkhead e Retry");
        return value;
    }

    @Override
    public String externalApiCallWithThreadPoolBulkhead() {
        log.info("Iniciando fluxo de chamada externa com Thread Pool Bulkhead");

        String value = bulkheadGateway.externalApiCallWithThreadPoolBulkhead();

        log.info("Finalizando fluxo de chamada externa com Thread Pool Bulkhead");
        return value;
    }
}
