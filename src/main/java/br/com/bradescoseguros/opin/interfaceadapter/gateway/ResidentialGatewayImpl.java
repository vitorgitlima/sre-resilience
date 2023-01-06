package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.gateway.ResidentialGateway;
import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import br.com.bradescoseguros.opin.external.configuration.redis.RedisConstants;
import br.com.bradescoseguros.opin.interfaceadapter.repository.ResidentialRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResidentialGatewayImpl implements ResidentialGateway {

    private final ResidentialRepository residentialRepository;

    @Override
    @Cacheable(cacheNames = RedisConstants.RESIDENTIAL_CACHE_NAME)
    public Page<ResidentialDomain> findByProductCodes(@NonNull Pageable pageable, @NonNull List<String> codes) {
        log.info("Searching for residential products.");
        return residentialRepository.findByProductCodeIn(pageable, codes);
    }

}