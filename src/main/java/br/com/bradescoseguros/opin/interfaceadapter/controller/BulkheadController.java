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
            //@ApiResponse(code = 408, message = "O tempo de espera da resposta para a chamada externa foi excedido.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/semaphorebulkhead")
    public ResponseEntity<String> getWithSemaphoreBulkhead() {

        log.info("Fluxo Semaphore Bulkhead");

        return ResponseEntity.ok(this.bulkheadUsecase.externalApiCallWithSemaphoreBulkhead());
    }

}
