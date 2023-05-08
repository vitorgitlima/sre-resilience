package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

public interface CrudUseCase {
    ExecutionResult<DemoSRE> getDemoSRE(final Integer id);

    ExecutionResult<DemoSRE> insertDemoSRE(final DemoSRE payload);

    ExecutionResult<DemoSRE> updateDemoSRE(final DemoSRE payload);

    ExecutionResult<DemoSRE> removeDemoSRE(final Integer id);

    ExecutionResult<String> externalApiCall(final ExtraStatusCode status);
}
