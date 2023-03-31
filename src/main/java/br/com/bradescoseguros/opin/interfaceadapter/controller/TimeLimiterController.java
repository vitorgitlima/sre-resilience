package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.usecase.TimeLimiterUsecase;
import br.com.bradescoseguros.opin.domain.DemoSRE;
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
@RequestMapping("/api/sre/v1/timelimiter")
public class TimeLimiterController {

    @Autowired
    private TimeLimiterUsecase timeLimiterUsecase;


    @Operation(summary = "Realiza uma chamada para o banco de dados com Time Limiter.",
            description = "Realiza uma chamada para o banco de dados com o padrão de projeto Time Limiter.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno na execução da requisição.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/db")
    public ResponseEntity<DemoSRE> getDbWithTimelimiter() {

        log.info("Fluxo DB Time Limiter");

        return ResponseEntity.ok(this.timeLimiterUsecase.getDemoSRE(1));
    }

    @Operation(summary = "Realiza uma chamada externa de API com Time Limiter.",
            description = "Realiza uma chamada externa de API com o padrão de projeto Time Limiter.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/api")
    public ResponseEntity<String> getWithTimeLimiter() {

        log.info("Fluxo API Time Limiter");

        return ResponseEntity.ok(this.timeLimiterUsecase.externalApiCallWithTimeLimiter());
    }

    @Operation(summary = "Realiza uma chamada externa de API com Time Limiter e Retry.",
            description = "Realiza uma chamada externa de API com o padrão de projeto Time Limiter funcionando em conjunto com o padrão de Retry.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 503, message = "Ocorreu um excesso de retentativas de chamada para o serviço externo e a resposta não foi obtida.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/retry")
    public ResponseEntity<String> getWithTimeLimiterAndRetry() {

        log.info("Fluxo Time Limiter com Retry");

        return ResponseEntity.ok(this.timeLimiterUsecase.externalApiCallWithTimeLimiterAndRetry());
    }

}
