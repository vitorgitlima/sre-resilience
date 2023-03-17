package br.com.bradescoseguros.opin.businessrule.gateway;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

import java.util.Optional;

public interface CrudGateway {
    Optional<DemoSRE> findById(final Integer id);

    void insertDemoSRE(final DemoSRE payload);

    void updateDemoSRE(final DemoSRE payload);

    void removeDemoSRE(final Integer id);

    String externalApiCall(final ExtraStatusCode status);



}
