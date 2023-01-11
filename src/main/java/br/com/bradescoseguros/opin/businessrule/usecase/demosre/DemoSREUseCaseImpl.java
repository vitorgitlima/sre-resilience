package br.com.bradescoseguros.opin.businessrule.usecase.demosre;

import br.com.bradescoseguros.opin.businessrule.exception.NotFoundException;
import br.com.bradescoseguros.opin.businessrule.gateway.DemoSREGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.businessrule.validator.DemoSREValidator;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoSREUseCaseImpl implements DemoSREUseCase {

	@Autowired
	private DemoSREGateway gateway;

	@Autowired
	private DemoSREValidator validator;

	@Autowired
	private MessageSourceService messageSourceService;

	@Override
	public DemoSRE getDemoSRE(final Integer id) {
		return gateway.findById(id).orElse(null);
	}

	@Override
	public void insertDemoSRE(final DemoSRE payload) {
		validator.execute(payload);
		gateway.insertDemoSRE(payload);
	}

	@Override
	public void updateDemoSRE(final DemoSRE payload) {
		validator.execute(payload);

		final String notFound = "demo-sre.id-not-found";

		if (gateway.findById(payload.getId()).isEmpty()) {
			throw new NotFoundException(messageSourceService.getMessage(notFound));
		}

		gateway.updateDemoSRE(payload);
	}

	@Override
	public void removeDemoSRE(final Integer id) {

		final String notFound = "demo-sre.id-not-found";

		if (gateway.findById(id).isEmpty()) {
			throw new NotFoundException(messageSourceService.getMessage(notFound));
		}
		
		gateway.removeDemoSRE(id);
	}
}
