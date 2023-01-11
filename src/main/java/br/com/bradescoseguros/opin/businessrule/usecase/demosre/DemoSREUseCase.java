package br.com.bradescoseguros.opin.businessrule.usecase.demosre;

import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;

public interface DemoSREUseCase {
	DemoSRE getDemoSRE(final Integer id);

	void insertDemoSRE(final DemoSRE payload);

	void updateDemoSRE(final DemoSRE payload);

	void removeDemoSRE(final Integer id);
}
