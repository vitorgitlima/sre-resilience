package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;

public interface CrudUseCase {
    DemoSRE getDemoSRE(final Integer id) throws Throwable;

    void insertDemoSRE(final DemoSRE payload);

    void updateDemoSRE(final DemoSRE payload);

    void removeDemoSRE(final Integer id);

    String externalApiCall(final ExtraStatusCode status);
}
