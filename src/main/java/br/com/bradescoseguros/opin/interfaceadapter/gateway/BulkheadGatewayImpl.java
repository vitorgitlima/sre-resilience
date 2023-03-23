package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import br.com.bradescoseguros.opin.businessrule.gateway.BulkheadGateway;
import br.com.bradescoseguros.opin.interfaceadapter.exception.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class BulkheadGatewayImpl implements BulkheadGateway {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BulkheadThreadPoolGatewayAnotation bulkheadThreadPoolGatewayAnotation;


    @Override
    @Bulkhead(name = "semaphoreBulkhead")
    public String externalApiCallWithBulkhead() {
        log.info("Chamando externalApiCall com delay");

        return callExternalApi("http://localhost:8081/api/sre/v1/extra/delay");
    }

    @Override
    @Bulkhead(name = "semaphoreBulkhead")
    @Retry(name = "apiBulkhead")
    public String externalApiCallWithBulkheadAndRetry() {
        log.info("Chamando externalApiCall com delay");

        return callExternalApi("http://localhost:8081/api/sre/v1/extra/delay");
    }

    @Override
    public String externalApiCallWithThreadPoolBulkhead() {
        log.info("Chamando externalApiCall com delay");

        try {
            return bulkheadThreadPoolGatewayAnotation.externalApiBulkheadThreadPool().get();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
            throw new GatewayException(e.getMessage());
        } catch (ExecutionException e) {
            log.error(e.getMessage());
            if (e.getCause() instanceof io.github.resilience4j.bulkhead.BulkheadFullException) {
                throw new BulkheadFullException(e.getMessage());
            }
            throw new GatewayException(e);
        }
    }
    private String callExternalApi(String fullURL) {

        return restTemplate.exchange(fullURL, HttpMethod.GET, null, String.class).getBody();
    }

}
