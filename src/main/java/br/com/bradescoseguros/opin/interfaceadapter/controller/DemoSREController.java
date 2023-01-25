package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.usecase.demosre.DemoSREUseCase;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.domain.demosre.ExtraStatusCode;
import br.com.bradescoseguros.opin.interfaceadapter.controller.dto.demosre.DemoSREDTO;
import br.com.bradescoseguros.opin.interfaceadapter.mapper.DemoSREMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/sre/v1")
public class DemoSREController {

    @Autowired
    private DemoSREUseCase demoSREUseCase;

    @Operation(summary = "Obtêm o registro de SRE.",
            description = "Obtêm o registro de SRE identificado através do ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações de SRE para o ID informado."),
            @ApiResponse(responseCode = "204", description = "O recurso solicitado não existe, não foi localizado ou foi deletado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "A requisição foi malformada, omitindo atributos obrigatórios, seja no payload ou através de atributos na URL.", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "404", description = "O recurso solicitado não existe ou não foi implementado.", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "405", description = "O consumidor tentou acessar o recurso com um método não suportado.", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no gateway da API ou no microsserviço.", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Error.class))}),
    })
    @GetMapping(value = "/getDemoSRE/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemoSRE> getDemoSRE(@PathVariable final Integer id) {
        return ResponseEntity.ok(this.demoSREUseCase.getDemoSRE(id));
    }

    @PostMapping("/insertDemoSRE")
    public ResponseEntity<DemoSRE> insertDemoSRE(@RequestBody final DemoSREDTO payload) {
        DemoSRE demoSRE = DemoSREMapper.INSTANCE.mapDemoSREFrom(payload);
        this.demoSREUseCase.insertDemoSRE(demoSRE);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateDemoSRE")
    public ResponseEntity<DemoSRE> updateDemoSRE(@RequestBody final DemoSREDTO payload) {
        DemoSRE demoSRE = DemoSREMapper.INSTANCE.mapDemoSREFrom(payload);
        this.demoSREUseCase.updateDemoSRE(demoSRE);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/removeDemoSRE/{id}")
    public ResponseEntity<DemoSRE> removeDemoSRE(@PathVariable final Integer id) {
        this.demoSREUseCase.removeDemoSRE(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/externalApiCall/{status}")
    public ResponseEntity<String> externalApiCall(@PathVariable final String status) {
        return ResponseEntity.ok(this.demoSREUseCase.externalApiCall(ExtraStatusCode.fromString(status)));
    }

}
