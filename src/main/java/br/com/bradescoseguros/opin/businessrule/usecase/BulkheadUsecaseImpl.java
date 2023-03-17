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
        return "";
    }

    @Override
    public String externalApiCallWithSemaphoreBulkheadAndRetry() {
        return "";
    }

    @Override
    public String externalApiCallWithThreadPoolBulkhead() {
        return "";
    }
}
