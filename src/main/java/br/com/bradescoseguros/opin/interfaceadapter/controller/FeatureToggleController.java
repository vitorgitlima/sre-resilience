package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.usecase.FeatureToggleUseCase;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ErrorEnum;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("api/sre/v1/featuretoggle")
public class FeatureToggleController {

    @Autowired
    private FeatureToggleUseCase featureToggleUseCase;


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/enabled")
    public ResponseEntity<Object> getIdEnabled() {
        ExecutionResult<DemoSRE> result = this.featureToggleUseCase.getDemoSREWithToggleEnabled(2);

        if (result.getErrorType() == ErrorEnum.NOT_FOUND) {
            log.info("Not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.getObject());
    }
}