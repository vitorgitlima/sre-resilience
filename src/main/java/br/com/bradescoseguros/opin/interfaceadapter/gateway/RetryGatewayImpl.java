package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.gateway.RetryGateway;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;
import br.com.bradescoseguros.opin.external.configuration.redis.RedisConstants;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
public class RetryGatewayImpl implements RetryGateway {
    @Autowired
    private CrudRepository repository;

    @Autowired
    private RestTemplate restTemplate;



    @Override
    @Retry(name = "cosmoRetry")
    @Cacheable(cacheNames = RedisConstants.DERMOSRE_CACHE_NAME, unless = "#result == null")
    public Optional<DemoSRE> findById(final Integer id) {
        return repository.findById(id);
    }

    @Override
    @Retry(name = "apiRetry")
    public String externalApiCall(final ExtraStatusCode statusCode) {
        return getExternalApiCall(statusCode);
    }

    @Override
    @Retry(name = "apiRetry")
    @CircuitBreaker(name = "apiCircuitBreaker")
    public String externalApiCallWithCircuitBreaker(final ExtraStatusCode statusCode) {
        return getExternalApiCall(statusCode);
    }

    private String getExternalApiCall(ExtraStatusCode statusCode) {
        final String baseURL = "http://localhost:8081/api/sre/v1/extra/";
        final String fullURL = baseURL + statusCode.getStatusURL();

        return restTemplate.exchange(fullURL, HttpMethod.GET, null, String.class).getBody();
    }
}
