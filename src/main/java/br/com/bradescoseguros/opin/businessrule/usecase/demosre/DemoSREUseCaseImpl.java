package br.com.bradescoseguros.opin.businessrule.usecase.demosre;

import br.com.bradescoseguros.opin.businessrule.exception.NotFoundException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSRENoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.DemoSREGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.businessrule.validator.DemoSREValidator;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import io.github.resilience4j.retry.annotation.Retry;
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

    private static final String NOT_FOUND = "demo-sre.id-not-found";

    @Retry(name = "CosmoRetry")
    @Override
    public DemoSRE getDemoSRE(final Integer id) {
        return gateway.findById(id).orElseThrow(() -> new DemoSRENoContentException(messageSourceService.getMessage(NOT_FOUND)));
    }

    @Override
    public void insertDemoSRE(final DemoSRE payload) {
        validator.execute(payload);
        gateway.insertDemoSRE(payload);
    }

    @Override
    public void updateDemoSRE(final DemoSRE payload) {
        validator.execute(payload);

        if (gateway.findById(payload.getId()).isEmpty()) {
            throw new NotFoundException(messageSourceService.getMessage(NOT_FOUND));
        }

        gateway.updateDemoSRE(payload);
    }

    @Override
    public void removeDemoSRE(final Integer id) {

        if (gateway.findById(id).isEmpty()) {
            throw new NotFoundException(messageSourceService.getMessage(NOT_FOUND));
        }

        gateway.removeDemoSRE(id);
    }
}
