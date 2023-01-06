package br.com.bradescoseguros.opin.businessrule.usecase.demosre;

import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import org.springframework.stereotype.Service;

@Service
public class DemoSREUseCaseImpl implements IDemoSREUseCase {
	public DemoSRE getDemoSRE() {
		return new DemoSRE();
	}
}
