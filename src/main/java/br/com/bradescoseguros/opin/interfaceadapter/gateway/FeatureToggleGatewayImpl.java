package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.gateway.FeatureToggleGateway;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Slf4j
@Component
public class FeatureToggleGatewayImpl implements FeatureToggleGateway {

    @Autowired
    private CrudRepository repository;

    @Override
    public Optional<DemoSRE> findByIdWithFeatureToggle(Integer id) {
            log.info("Objeto não encontrado no Cache. Chamando findById com id {}", id);
            return repository.findById(id);
    }
}
