package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.exception.BadRequestException;
import br.com.bradescoseguros.opin.businessrule.exception.NotFoundException;
import br.com.bradescoseguros.opin.businessrule.exception.RegistryAlreadyExistsException;
import br.com.bradescoseguros.opin.businessrule.gateway.CrudGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.businessrule.validator.DemoSREValidator;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ErrorEnum;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class CrudUseCaseImpl implements CrudUseCase {

    @Autowired
    private CrudGateway gateway;

    @Autowired
    private DemoSREValidator validator;

    @Autowired
    private MessageSourceService messageSourceService;

    private static final String NOT_FOUND = "demo-sre.id-not-found";

    @Override
    public ExecutionResult<DemoSRE> getDemoSRE(final Integer id) {
        log.info("Iniciando fluxo de recuperação de objeto por id");

        if(id < 0 ) {
            log.warn("Id menor que 0");
            return ExecutionResult.<DemoSRE>builder().errorType(ErrorEnum.VALIDATION).errorMessage("ID inválido").build();
        }

        Optional<DemoSRE> demoSREOptional = gateway.findById(id);

        log.info("Finalizando fluxo de recuperação de objeto por id");

        if(demoSREOptional.isEmpty()) {
            log.warn(messageSourceService.getMessage(NOT_FOUND));
            return ExecutionResult.<DemoSRE>builder().build();
        }

        return ExecutionResult.<DemoSRE>builder().object(demoSREOptional.get()).build();
    }

    @Override
    public void insertDemoSRE(final DemoSRE payload) {
        log.info("Iniciando fluxo de inserção de objeto");

        validator.execute(payload);

        if (gateway.findById(payload.getId()).isPresent()) {
            String error = "O ID informado na inserção DemoSRE já existe na base de dados";
            log.warn(error);

            throw new RegistryAlreadyExistsException(error);
        }

        gateway.insertDemoSRE(payload);

        log.info("Finalizando fluxo de inserção de objeto");
    }

    @Override
    public void updateDemoSRE(final DemoSRE payload) {
        log.info("Iniciando fluxo de atualização de objeto");

        validator.execute(payload);

        if (gateway.findById(payload.getId()).isEmpty()) {
            log.warn(messageSourceService.getMessage(NOT_FOUND));

            throw new NotFoundException(messageSourceService.getMessage(NOT_FOUND));
        }

        gateway.updateDemoSRE(payload);

        log.info("Finalizando fluxo de atualização de objeto");
    }

    @Override
    public void removeDemoSRE(final Integer id) {
        log.info("Iniciando fluxo de remoção de objeto");

        if (gateway.findById(id).isEmpty()) {
            log.warn(messageSourceService.getMessage(NOT_FOUND));

            throw new NotFoundException(messageSourceService.getMessage(NOT_FOUND));
        }

        gateway.removeDemoSRE(id);

        log.info("Finalizando fluxo de remoção de objeto");
    }

    @Override
    public String externalApiCall(final ExtraStatusCode status) {
        log.info("Iniciando fluxo de consulta por status");

        if (Objects.isNull(status)) {
            String error = "O status informado não é suportado pela aplicação.";
            log.warn(error);

            throw new BadRequestException(error);
        }

        String externalApi = getExternalApi(status);

        log.info("Finalizando fluxo de consulta por status");

        return externalApi;
    }

    private String getExternalApi(ExtraStatusCode status) {

        log.info("Chamando ExternalApiCall");
        return gateway.externalApiCall(status);
    }
}
