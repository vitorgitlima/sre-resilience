package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.usecase.CircuitBreakerUseCase;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/sre/v1/circuitbreaker")
public class CircuitBreakerController {

    @Autowired
    private CircuitBreakerUseCase circuitBreakerUseCase;

    @Operation(summary = "Realiza uma chamada ao banco de dados com Retry.",
            description = "Realiza uma chamada ao banco de dados com o padrão de projeto Retry. O Retry será executado apenas em cenário de erros")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 423, message = "O circuito esta aberto e novas solicitações estão suspensas.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/db")
    public ResponseEntity<DemoSRE> getDbWithCircuitBreaker() {

        log.info("Fluxo Circuit Breaker DB");

        return ResponseEntity.ok(this.circuitBreakerUseCase.getDemoSREWithCircuitBreaker(1));
    }

    @Operation(summary = "Realiza uma chamada externa de API com Time Limiter.",
            description = "Realiza uma chamada externa de API com o padrão de projeto Time Limiter.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 423, message = "O circuito esta aberto e novas solicitações estão suspensas.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 503, message = "O limite de retentativas foi excedido.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/api")
    public ResponseEntity<String> getApiWithCircuitBreaker() {

        log.info("Fluxo Circuit Breaker API");

        return ResponseEntity.ok(this.circuitBreakerUseCase.externalApiCallWithCircuitBreaker(ExtraStatusCode.INTERNAL_SERVER_ERROR));
    }
}
