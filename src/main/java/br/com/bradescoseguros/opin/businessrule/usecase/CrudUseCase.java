package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

public interface CrudUseCase {
    ExecutionResult<DemoSRE> getDemoSRE(final Integer id);

    void insertDemoSRE(final DemoSRE payload);

    void updateDemoSRE(final DemoSRE payload);

    void removeDemoSRE(final Integer id);

    String externalApiCall(final ExtraStatusCode status);
}
