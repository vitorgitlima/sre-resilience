package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.usecase.BulkheadUsecase;
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
@RequestMapping("/api/sre/v1")
public class BulkheadController {

    @Autowired
    private BulkheadUsecase bulkheadUsecase;

    @Operation(summary = "Realiza uma chamada externa de API com Bulkhead do tipo Semáforo.",
            description = "Realiza uma chamada externa de API com o padrão de projeto Bulkhead.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 503, message = "O bulkhead esta cheio e não aceita nova requisições.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/semaphorebulkhead")
    public ResponseEntity<String> getWithSemaphoreBulkhead() {

        log.info("Fluxo Semaphore Bulkhead");

        return ResponseEntity.ok(this.bulkheadUsecase.externalApiCallWithSemaphoreBulkhead());
    }


    @Operation(summary = "Realiza uma chamada externa de API com Bulkhead do tipo Semáforo e Retry.",
            description = "Realiza uma chamada externa de API com o padrão de projeto Bulkhead e Retry, onde o Retry irá realizar retentativas de execução antes de retornar um erro.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 503, message = "A quantidade de retentativas foi excedida.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/semaphorebulkhead/retry")
    public ResponseEntity<String> getWithSemaphoreBulkheadAndRetry() {

        log.info("Fluxo Semaphore Bulkhead e Retry");

        return ResponseEntity.ok(this.bulkheadUsecase.externalApiCallWithSemaphoreBulkheadAndRetry());
    }


    @Operation(summary = "Realiza uma chamada externa de API com Bulkhead do tipo Thread Pool.",
            description = "Realiza uma chamada externa de API com o padrão de projeto Bulkhead.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
        @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
        @ApiResponse(code = 503, message = "O bulkhead esta cheio e não aceita nova requisições.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/threadpoolbulkhead")
    public ResponseEntity<String> getWithThreadPoolBulkhead() {

        log.info("Fluxo Thread Pool Bulkhead");

        return ResponseEntity.ok(this.bulkheadUsecase.externalApiCallWithThreadPoolBulkhead());
    }


}
