package br.com.bradescoseguros.opin.businessrule.gateway;

import br.com.bradescoseguros.opin.domain.DemoSRE;

import java.util.Optional;

public interface TimeLimiterGateway {

    Optional<DemoSRE> findByIdWithTimeLimiter(Integer id);
    String externalApiCallWithTimeLimiter();
    String externalApiCallWithTimeLimiterAndRetry();

}
