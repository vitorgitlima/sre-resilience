package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import br.com.bradescoseguros.opin.interfaceadapter.exception.BulkheadFullException;
import br.com.bradescoseguros.opin.businessrule.gateway.CrudGateway;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;
import br.com.bradescoseguros.opin.external.configuration.redis.RedisConstants;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class CrudGatewayImpl implements CrudGateway {
    @Autowired
    private CrudRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BulkheadThreadPoolGatewayAnotation bulkheadThreadPoolGatewayAnotation;




    @Override
    @Retry(name = "cosmoRetry")
    @CircuitBreaker(name = "cosmoCircuitBreaker")
    @Cacheable(cacheNames = RedisConstants.DERMOSRE_CACHE_NAME, unless = "#result == null")
    public Optional<DemoSRE> findById(final Integer id) {
        return repository.findById(id);
    }

    @Override
    @CacheEvict(cacheNames = RedisConstants.DERMOSRE_CACHE_NAME)
    public void insertDemoSRE(final DemoSRE payload) {
        repository.insert(payload);
    }

    @Override
    @CacheEvict(cacheNames = RedisConstants.DERMOSRE_CACHE_NAME)
    public void updateDemoSRE(final DemoSRE payload) {
        repository.save(payload);
    }

    @Override
    @CacheEvict(cacheNames = RedisConstants.DERMOSRE_CACHE_NAME)
    public void removeDemoSRE(final Integer id) {
        repository.deleteById(id);
    }

    @Override
    @Retry(name = "apiRetry")
    @CircuitBreaker(name = "apiCircuitBreaker")
    public String externalApiCall(final ExtraStatusCode statusCode) {
        final String baseURL = "http://localhost:8081/api/sre/v1/extra/";
        final String fullURL = baseURL + statusCode.getStatusURL();

        return restTemplate.exchange(fullURL, HttpMethod.GET, null, String.class).getBody();
    }

    @Override
    @Bulkhead(name = "semaphoreBulkhead")
    public String externalApiCallBulkhead() {

        return callExternalApi("http://localhost:8081/api/sre/v1/extra/delay");
    }

    @Override
    @Bulkhead(name = "semaphoreBulkhead")
    @Retry(name = "apiBulkhead")
    public String externalApiCallBulkheadRetry() {

        return callExternalApi("http://localhost:8081/api/sre/v1/extra/delay");
    }

    @Override
    public String externalApiCallThreadPoolBulkhead() {
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
