package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.gateway.FeatureToggleGateway;
import br.com.bradescoseguros.opin.businessrule.validator.DemoSREValidator;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ErrorEnum;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import com.azure.spring.cloud.feature.management.FeatureManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class FeatureToggleUseCaseImpl implements FeatureToggleUseCase {

    @Autowired
    private FeatureToggleGateway gateway;

    @Autowired
    private FeatureManager featureManager;

    @Autowired
    private DemoSREValidator validator;

    @Override
    public ExecutionResult<DemoSRE> getDemoSREWithToggleEnabled(final Integer id) {
        if (featureManager.isEnabledAsync("feature-a").block()) {
            Optional<DemoSRE> demoSREOptional = gateway.findByIdWithFeatureToggle(id);
            if (!demoSREOptional.isPresent()) {
                log.info("Feature habilitada e não encontrou objeto com id requerido.");
                return ExecutionResult.<DemoSRE>builder().errorType(ErrorEnum.NOT_FOUND).build();
            } else {
                log.info("Feature habilitada e encontrou objeto com id requerido.");
                return ExecutionResult.<DemoSRE>builder().object(demoSREOptional.get()).build();
            }
        }
        log.info("Feature desabilitada.");
        return ExecutionResult.<DemoSRE>builder().errorType(ErrorEnum.NOT_FOUND).build();
    }
}
