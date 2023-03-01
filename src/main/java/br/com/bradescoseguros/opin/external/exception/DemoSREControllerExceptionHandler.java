package br.com.bradescoseguros.opin.external.exception;

import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSREBadRequestException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSREMaxRetriesExceededException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSRENoContentException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSRERegistryAlreadyExistsException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorCode;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.external.exception.entities.MetaData;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;
import java.util.Collections;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class DemoSREControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSourceService messageSourceService;

    @ExceptionHandler(DemoSRENoContentException.class)
    public ResponseEntity<Object> handleDemoSRENoContentException(final DemoSRENoContentException exception,
                                                                  final WebRequest request) {
        MetaDataEnvelope response =
                new MetaDataEnvelope(HttpStatus.NO_CONTENT.toString(), ErrorCode.DEMOSRE_NO_CONTENT, exception.getMessage());

        if (!isEmpty(exception.getErrors())) {
            final MetaData meta = new MetaData(HttpStatus.NO_CONTENT.toString(), MDC.get("TRACE_ID"));
            response = new MetaDataEnvelope(meta, exception.getErrors());
        }

        log.info("DemoSRENoContentException: {}", response);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                HttpStatus.NO_CONTENT, request);
    }

    @ExceptionHandler(DemoSREMaxRetriesExceededException.class)
    public ResponseEntity<Object> handleDemoSREMaxRetriesExceededException(final DemoSREMaxRetriesExceededException exception,
                                                                           final WebRequest request) {

        String exceptionMessage = exception.getMessage();

        if (!StringUtils.hasText(exceptionMessage)) {
            exceptionMessage = messageSourceService.getMessage("demo-sre.service-unavailable");
        }

        HttpStatus httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.DEMOSRE_MAX_RETRIES_EXCEEDED, exceptionMessage);

        if (!isEmpty(exception.getErrors())) {
            final MetaData meta = new MetaData(httpStatus.toString(), MDC.get("TRACE_ID"));
            response = new MetaDataEnvelope(meta, exception.getErrors());
        }

        log.error("handleDemoSREMaxRetriesExceededException: {}", response);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(DemoSREBadRequestException.class)
    public ResponseEntity<Object> handleDemoSREBadRequestException(final DemoSREBadRequestException exception, final WebRequest request) {
        String exceptionMessage = exception.getMessage();

        if (!StringUtils.hasText(exceptionMessage)) {
            exceptionMessage = messageSourceService.getMessage("demo-sre.bad-request");
        }

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.BAD_REQUEST, exceptionMessage);

        if (!isEmpty(exception.getErrors())) {
            final MetaData meta = new MetaData(httpStatus.toString(), MDC.get("TRACE_ID"));
            response = new MetaDataEnvelope(meta, exception.getErrors());
        }

        log.warn("handleDemoSREBadRequestException: {}", response);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(DemoSRERegistryAlreadyExistsException.class)
    public ResponseEntity<Object> handleDemoSRERegistryAlreadyExistsException(final DemoSRERegistryAlreadyExistsException exception, final WebRequest request) {
        String exceptionMessage = messageSourceService.getMessage("demo-sre.registry-already-exists");
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        ErrorData errorData = new ErrorData(exceptionMessage, exception.getMessage(), httpStatus.toString());

        final MetaData meta = new MetaData(httpStatus.toString(), MDC.get("TRACE_ID"));
        MetaDataEnvelope response = new MetaDataEnvelope(meta, Collections.singleton(errorData));

        log.warn("handleDemoSRERegistryAlreadyExistsException: {}", response);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<Object> handleCallNotPermittedException(final CallNotPermittedException exception, final WebRequest request) {

        String exceptionMessage = messageSourceService.getMessage("demo-sre.circuit-opened", exception.getCausingCircuitBreakerName());

        HttpStatus httpStatus = HttpStatus.LOCKED;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.DEMOSRE_CIRCUIT_OPENED, exceptionMessage);

        final String errorMessage = MessageFormat.format("handleCallNotPermittedException: {0}", response);
        log.warn(errorMessage, exception);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(BulkheadFullException.class)
    public ResponseEntity<Object> handleBulkheadFullException(final BulkheadFullException exception, final WebRequest request) {

        String exceptionMessage = messageSourceService.getMessage("demo-sre.service-unavailable");

        HttpStatus httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.BULKHEAD_FULL, exceptionMessage);

        final String errorMessage = MessageFormat.format("handleCallNotPermittedException: {0}", response);
        log.warn(errorMessage, exception);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }
}
