package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExecutionResult;

public interface FeatureToggleUseCase {

    ExecutionResult<DemoSRE> getDemoSREWithToggleEnabled(final Integer id);

}
