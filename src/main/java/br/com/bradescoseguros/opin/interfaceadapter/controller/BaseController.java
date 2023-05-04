package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorCode;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.external.exception.entities.MetaData;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    public ResponseEntity<Object> generateBadRequestResponse(ExecutionResult<DemoSRE> executionResult) {
        MetaDataEnvelope response = new MetaDataEnvelope(HttpStatus.BAD_REQUEST.toString(), ErrorCode.BAD_REQUEST, executionResult.getErrorMessage());

        final MetaData meta = new MetaData(HttpStatus.BAD_REQUEST.toString(), MDC.get("TRACE_ID"));
        response.setMeta(meta);
        return ResponseEntity.badRequest().body(response);
    }

    public ResponseEntity<Object> generateNotFoundResponse() {
        return ResponseEntity.notFound().build();
    }

}
