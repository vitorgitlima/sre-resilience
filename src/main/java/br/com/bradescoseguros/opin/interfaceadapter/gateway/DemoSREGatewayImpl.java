package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSREBulkheadFullException;
import br.com.bradescoseguros.opin.businessrule.gateway.DemoSREGateway;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.domain.demosre.ExtraStatusCode;
import br.com.bradescoseguros.opin.external.configuration.redis.RedisConstants;
import br.com.bradescoseguros.opin.interfaceadapter.repository.DemoSRERepository;
import io.github.resilience4j.bulkhead.BulkheadFullException;
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
public class DemoSREGatewayImpl implements DemoSREGateway {
    @Autowired
    private DemoSRERepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DemoSREGatewayBulkheadThreadPool demoSREGatewayBulkheadThreadPool;

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
        final String fullURL = "http://localhost:8081/api/sre/v1/extra/bulkhead";

        return restTemplate.exchange(fullURL, HttpMethod.GET, null, String.class).getBody();
    }

    @Override
    @Bulkhead(name = "semaphoreBulkhead")
    @Retry(name = "apiBulkhead")
    public String externalApiCallBulkheadRetry() {
        final String fullURL = "http://localhost:8081/api/sre/v1/extra/bulkhead";

        return restTemplate.exchange(fullURL, HttpMethod.GET, null, String.class).getBody();
    }

    @Override
    public String externalApiCallThreadPoolBulkhead() {
        try {
            return demoSREGatewayBulkheadThreadPool.externalApiBulkheadThreadPool().get();
        } catch (InterruptedException | ExecutionException e) {
            if(e.getCause() instanceof BulkheadFullException) {
                throw new DemoSREBulkheadFullException(e.getMessage());
            }
            throw new GatewayException(e);
        }
    }
}
