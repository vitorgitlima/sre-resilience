package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.gateway.ResidentialRangeCepGateway;
import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import br.com.bradescoseguros.opin.external.configuration.redis.RedisConstants;
import br.com.bradescoseguros.opin.interfaceadapter.repository.ResidentialRangeCepRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResidentialRangeCepGatewayImpl implements ResidentialRangeCepGateway {

    private final ResidentialRangeCepRepository residentialRangeCepRepository;

    @Override
    @Cacheable(cacheNames = RedisConstants.RESIDENTIAL_RANGE_CEP_CACHE_NAME)
    public List<ResidentialRangeCep> findListElegibleProductsByCep(String zipCode) {
        log.info("Find products with zipCode [" + zipCode + "].");

        return residentialRangeCepRepository
                .findByPostalCodeStartLessThanEqualAndPostalCodeEndGreaterThanEqual(zipCode, zipCode);
    }
}