package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.usecase.RetryUseCase;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ErrorEnum;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/sre/v1/retry")
public class RetryController implements BaseController {

    @Autowired
    private RetryUseCase retryUseCase;

    @Operation(summary = "Realiza uma chamada ao banco de dados com Retry.",
            description = "Realiza uma chamada ao banco de dados com o padrão de projeto Retry. O Retry será executado apenas em cenário de erros")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 503, message = "O limite de retentativas foi excedido.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/db")
    public ResponseEntity<Object> getDbWithRetry() {

        log.info("Fluxo Retry DB");

        ExecutionResult<DemoSRE> result = this.retryUseCase.getDemoSREWithRetry(1);

        if (result.getErrorType() == ErrorEnum.NOT_FOUND) {
            return generateNotFoundResponse();
        }

        return ResponseEntity.ok(result.getObject());
    }

    @Operation(summary = "Realiza uma chamada externa de API com Time Limiter.",
            description = "Realiza uma chamada externa de API com o padrão de projeto Time Limiter.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 503, message = "O limite de retentativas foi excedido.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/api/{status}")
    public ResponseEntity<String> getApiWithRetry(@PathVariable final String status) {

        log.info("Fluxo Retry API");

        return ResponseEntity.ok(this.retryUseCase.externalApiCallWithRetry(ExtraStatusCode.fromString(status)));
    }

    @Operation(summary = "Realiza uma chamada externa de API com Time Limiter e Circuit Breaker.",
            description = "Realiza uma chamada externa de API com o padrão de projeto Time Limiter e Circuit Breaker. O Circuit Breaker irá registrar os erros obtidos nas retentativas também")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 423, message = "O circuito esta aberto e novas solicitações estão suspensas.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 503, message = "O limite de retentativas foi excedido ou o .", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/circuitbreaker")
    public ResponseEntity<String> getApiWithRetryAndCircuitBreaker() {

        log.info("Fluxo Retry with Circuit Breaker");

        return ResponseEntity.ok(this.retryUseCase.externalApiCallWithRetryAndCircuitBreaker(ExtraStatusCode.INTERNAL_SERVER_ERROR));
    }
}
