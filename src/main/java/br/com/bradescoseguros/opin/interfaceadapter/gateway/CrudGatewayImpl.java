package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.gateway.CrudGateway;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;
import br.com.bradescoseguros.opin.external.configuration.redis.RedisConstants;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
public class CrudGatewayImpl implements CrudGateway {
    @Autowired
    private CrudRepository repository;

    @Autowired
    private RestTemplate restTemplate;



    @Override
    @Cacheable(cacheNames = RedisConstants.DERMOSRE_CACHE_NAME, unless = "#result == null")
    public Optional<DemoSRE> findById(final Integer id) {
        log.info("Objeto não encontrado no Cache. Chamando findById com id {}", id);
        return repository.findById(id);
    }

    @Override
    @CacheEvict(cacheNames = RedisConstants.DERMOSRE_CACHE_NAME)
    public void insertDemoSRE(final DemoSRE payload) {
        log.info("Chamando insert com payload {}", payload.toString());
        repository.insert(payload);
    }

    @Override
    @CacheEvict(cacheNames = RedisConstants.DERMOSRE_CACHE_NAME)
    public void updateDemoSRE(final DemoSRE payload) {
        log.info("Chamando save com payload {}", payload.toString());
        repository.save(payload);
    }

    @Override
    @CacheEvict(cacheNames = RedisConstants.DERMOSRE_CACHE_NAME)
    public void removeDemoSRE(final Integer id) {
        log.info("Chamando deleteById com id {}", id);
        repository.deleteById(id);
    }

    @Override
    public String externalApiCall(final ExtraStatusCode statusCode) {
        log.info("Chamando externalApiCall com status {}", statusCode);

        final String baseURL = "http://localhost:8081/api/sre/v1/extra/";
        final String fullURL = baseURL + statusCode.getStatusURL();

        return restTemplate.exchange(fullURL, HttpMethod.GET, null, String.class).getBody();
    }


}
