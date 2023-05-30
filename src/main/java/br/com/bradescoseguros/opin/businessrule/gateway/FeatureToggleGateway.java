package br.com.bradescoseguros.opin.businessrule.gateway;

import br.com.bradescoseguros.opin.domain.DemoSRE;

import java.util.Optional;

public interface FeatureToggleGateway {

    Optional<DemoSRE> findByIdWithFeatureToggle(Integer id);
}
