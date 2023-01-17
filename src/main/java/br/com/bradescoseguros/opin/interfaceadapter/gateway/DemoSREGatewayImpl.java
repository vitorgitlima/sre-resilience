package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSREMaxRetriesExceededException;
import br.com.bradescoseguros.opin.businessrule.gateway.DemoSREGateway;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.external.configuration.redis.RedisConstants;
import br.com.bradescoseguros.opin.interfaceadapter.repository.DemoSRERepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
public class DemoSREGatewayImpl implements DemoSREGateway {
    @Autowired
    private DemoSRERepository repository;

    @Override
    @Retry(name = "CosmoRetry", fallbackMethod = "fallbackCosmoRetry")
    @Cacheable(cacheNames = RedisConstants.DERMOSRE_CACHE_NAME)
    public Optional<DemoSRE> findById(final Integer id) {
        return repository.findById(id);
    }

    @Override
    public void insertDemoSRE(final DemoSRE payload) {
        repository.insert(payload);
    }

    @Override
    public void updateDemoSRE(final DemoSRE payload) {
        repository.save(payload);
    }

    @Override
    public void removeDemoSRE(final Integer id) {
        repository.deleteById(id);
    }

    @Override
    @Retry(name = "ApiRetry")
    public String externalApiCall() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.exchange("", HttpMethod.POST, null, String.class);

        return null;
    }

    private Optional<DemoSRE> fallbackCosmoRetry(final Integer id, DataAccessResourceFailureException e) {
        throw new DemoSREMaxRetriesExceededException();
    }
}
