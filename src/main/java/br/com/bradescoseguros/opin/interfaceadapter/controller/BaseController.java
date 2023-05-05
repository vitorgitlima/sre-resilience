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
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return generateErrorResponseWithStatusCode(badRequest, ErrorCode.BAD_REQUEST, executionResult);
    }

    public ResponseEntity<Object> generateNotFoundResponse() {
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Object> generateConflictResponse(ExecutionResult<DemoSRE> executionResult) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        return generateErrorResponseWithStatusCode(conflict, ErrorCode.CONFLICT, executionResult);
    }

    private ResponseEntity<Object> generateErrorResponseWithStatusCode(HttpStatus conflict, ErrorCode conflict1, ExecutionResult<DemoSRE> executionResult) {
        MetaDataEnvelope response = new MetaDataEnvelope(conflict.toString(), conflict1, executionResult.getErrorMessage());

        final MetaData meta = new MetaData(conflict.toString(), MDC.get("TRACE_ID"));
        response.setMeta(meta);
        return ResponseEntity.status(conflict).body(response);
    }
}
