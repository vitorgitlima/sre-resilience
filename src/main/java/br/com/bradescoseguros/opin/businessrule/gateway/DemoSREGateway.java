package br.com.bradescoseguros.opin.businessrule.gateway;

import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.domain.demosre.ExtraStatusCode;

import java.util.Optional;

public interface DemoSREGateway {
    Optional<DemoSRE> findById(final Integer id);

    void insertDemoSRE(final DemoSRE payload);

    void updateDemoSRE(final DemoSRE payload);

    void removeDemoSRE(final Integer id);

    String externalApiCall(final ExtraStatusCode status);

    String externalApiCallBulkhead();

    String externalApiCallBulkheadRetry();

    String externalApiCallThreadPoolBulkhead();

}
