package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorCode;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.external.exception.entities.MetaData;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface BaseController {

    default ResponseEntity<Object> generateBadRequestResponse(ExecutionResult<?> executionResult) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return generateErrorResponseWithStatusCode(badRequest, ErrorCode.BAD_REQUEST, executionResult);
    }

    default ResponseEntity<Object> generateNotFoundResponse() {
        return ResponseEntity.notFound().build();
    }

    default ResponseEntity<Object> generateConflictResponse(ExecutionResult<?> executionResult) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        return generateErrorResponseWithStatusCode(conflict, ErrorCode.CONFLICT, executionResult);
    }

    default ResponseEntity<Object> generateErrorResponseWithStatusCode(HttpStatus httpStatus, ErrorCode errorCode, ExecutionResult<?> executionResult) {
        MetaDataEnvelope response = new MetaDataEnvelope(httpStatus.toString(), errorCode, executionResult.getErrorMessage());

        final MetaData meta = new MetaData(httpStatus.toString(), MDC.get("TRACE_ID"));
        response.setMeta(meta);
        return ResponseEntity.status(httpStatus).body(response);
    }
}
