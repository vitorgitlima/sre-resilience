package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.usecase.demosre.DemoSREUseCase;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.domain.demosre.ExtraStatusCode;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import br.com.bradescoseguros.opin.interfaceadapter.controller.dto.demosre.DemoSREDTO;
import br.com.bradescoseguros.opin.interfaceadapter.mapper.DemoSREMapper;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/sre/v1")
public class DemoSREController {

    @Autowired
    private DemoSREUseCase demoSREUseCase;

    @Operation(summary = "Obtêm o registro de SRE.",
            description = "Obtêm o registro de SRE identificado através do ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Informações de SRE para o ID informado.", response = DemoSRE.class),
            @ApiResponse(code = 204, message = "O recurso solicitado não existe, não foi localizado ou foi deletado."),
            @ApiResponse(code = 400, message = "A requisição foi malformada, omitindo atributos obrigatórios, seja no payload ou através de atributos na URL.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 404, message = "O recurso solicitado não existe ou não foi implementado.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 405, message = "O usuário tentou acessar o recurso com um método não suportado.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 423, message = "O servidor não está recebendo chamadas temporariamente.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/getDemoSRE/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemoSRE> getDemoSRE(@PathVariable final Integer id) {
        log.info("ID={}", id);

        return ResponseEntity.ok(this.demoSREUseCase.getDemoSRE(id));
    }

    @Operation(summary = "Insere um novo registro de SRE.",
            description = "Insere um novo registro de SRE.")
    @ApiResponses(value = {
            @ApiResponse(code = 203, message = "O registrado foi criado com sucesso."),
            @ApiResponse(code = 400, message = "A requisição foi malformada, omitindo atributos obrigatórios, seja no payload ou através de atributos na URL.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 409, message = "O registro já foi previamente inserido na base de dados.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 423, message = "O servidor não está recebendo chamadas temporariamente.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
    })
    @PostMapping(value = "/insertDemoSRE")
    public ResponseEntity<DemoSRE> insertDemoSRE(@RequestBody final DemoSREDTO payload) {
        DemoSRE demoSRE = DemoSREMapper.INSTANCE.mapDemoSREFrom(payload);

        log.info("Payload={}", payload.toString());

        this.demoSREUseCase.insertDemoSRE(demoSRE);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualiza um registro existente de SRE.",
            description = "Atualiza um registro existente de SRE substituindo as informações.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "O registrado foi atualizado com sucesso."),
            @ApiResponse(code = 400, message = "A requisição foi malformada, omitindo atributos obrigatórios, seja no payload ou através de atributos na URL.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 404, message = "A registro solcitado não foi encontrado para atualização.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 423, message = "O servidor não está recebendo chamadas temporariamente.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
    })
    @PutMapping("/updateDemoSRE")
    public ResponseEntity<DemoSRE> updateDemoSRE(@RequestBody final DemoSREDTO payload) {
        DemoSRE demoSRE = DemoSREMapper.INSTANCE.mapDemoSREFrom(payload);

        log.info("Payload={}", payload.toString());

        this.demoSREUseCase.updateDemoSRE(demoSRE);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove um registro de SRE.",
            description = "Insere um registro de SRE.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "O registrado foi removido com sucesso."),
            @ApiResponse(code = 400, message = "A requisição foi malformada, omitindo atributos obrigatórios, seja no payload ou através de atributos na URL.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 404, message = "A registro solcitado não foi encontrado para remoção.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 423, message = "O servidor não está recebendo chamadas temporariamente.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
    })
    @DeleteMapping("/removeDemoSRE/{id}")
    public ResponseEntity<DemoSRE> removeDemoSRE(@PathVariable final Integer id) {
        log.info("ID={}", id);

        this.demoSREUseCase.removeDemoSRE(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Realiza uma chamada externa de API.",
            description = "Realiza uma chamada externa de API retornando diversos tipos de resultados conforme informado no parâmetro STATUS.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A API foi executada com sucesso."),
            @ApiResponse(code = 400, message = "A requisição foi malformada, omitindo atributos obrigatórios, seja no payload ou através de atributos na URL.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 404, message = "A registro solcitado não foi encontrado para remoção.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 423, message = "O servidor não está recebendo chamadas temporariamente.", response = MetaDataEnvelope.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro no gateway da API ou no microsserviço.", response = MetaDataEnvelope.class),
    })
    @GetMapping(value = "/externalApiCall/{status}")
    public ResponseEntity<String> externalApiCall(@PathVariable final String status) {

        ExtraStatusCode extraStatusCode = ExtraStatusCode.fromString(status);

        log.info("Status={}", extraStatusCode.getStatusURL());

        return ResponseEntity.ok(this.demoSREUseCase.externalApiCall(extraStatusCode));
    }

    @GetMapping(value = "/externalApiCallBulkHead")
    public ResponseEntity<String> externalApiCallBulkhead() {

        return ResponseEntity.ok(this.demoSREUseCase.externalApiCallBulkhead());
    }

    @GetMapping(value = "/externalApiCallBulkHeadRetry")
    public ResponseEntity<String> externalApiCallBulkheadRetry() {

        return ResponseEntity.ok(this.demoSREUseCase.externalApiCallBulkheadRetry());
    }
}
