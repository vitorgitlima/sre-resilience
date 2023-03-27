package br.com.bradescoseguros.opin.external.exception;

import br.com.bradescoseguros.opin.businessrule.exception.BadRequestException;
import br.com.bradescoseguros.opin.businessrule.exception.RegistryAlreadyExistsException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorCode;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.external.exception.entities.MetaData;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import br.com.bradescoseguros.opin.interfaceadapter.exception.BulkheadFullException;
import br.com.bradescoseguros.opin.interfaceadapter.exception.MaxRetriesExceededException;
import br.com.bradescoseguros.opin.interfaceadapter.exception.TimeOutException;
import com.mongodb.MongoTimeoutException;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.Collections;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ResilienceControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSourceService messageSourceService;


    @ExceptionHandler(MaxRetriesExceededException.class)
    public ResponseEntity<Object> handleMaxRetriesExceededException(final MaxRetriesExceededException exception,
                                                                           final WebRequest request) {

        String exceptionMessage = exception.getMessage();

        if (!StringUtils.hasText(exceptionMessage)) {
            exceptionMessage = messageSourceService.getMessage("sre.max-retries-exceed");
        }

        HttpStatus httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.MAX_RETRIES_EXCEEDED, exceptionMessage);

        if (!isEmpty(exception.getErrors())) {
            final MetaData meta = new MetaData(httpStatus.toString(), MDC.get("TRACE_ID"));
            response = new MetaDataEnvelope(meta, exception.getErrors());
        }

        log.error("handleMaxRetriesExceededException: {}", response);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(final BadRequestException exception, final WebRequest request) {
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

        log.warn("handleBadRequestException: {}", response);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(RegistryAlreadyExistsException.class)
    public ResponseEntity<Object> handleRegistryAlreadyExistsException(final RegistryAlreadyExistsException exception, final WebRequest request) {
        String exceptionMessage = messageSourceService.getMessage("demo-sre.registry-already-exists");
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        ErrorData errorData = new ErrorData(exceptionMessage, exception.getMessage(), httpStatus.toString());

        final MetaData meta = new MetaData(httpStatus.toString(), MDC.get("TRACE_ID"));
        MetaDataEnvelope response = new MetaDataEnvelope(meta, Collections.singleton(errorData));

        log.warn("handleRegistryAlreadyExistsException: {}", response);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<Object> handleCallNotPermittedException(final CallNotPermittedException exception, final WebRequest request) {

        String exceptionMessage = messageSourceService.getMessage("sre.circuit-opened", exception.getCausingCircuitBreakerName());

        HttpStatus httpStatus = HttpStatus.LOCKED;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.LOCKED, exceptionMessage);

        final String errorMessage = MessageFormat.format("handleCallNotPermittedException: {0}", response);
        log.warn(errorMessage, exception);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler({BulkheadFullException.class, io.github.resilience4j.bulkhead.BulkheadFullException.class})
    public ResponseEntity<Object> handleBulkheadFullException(final Exception exception, final WebRequest request) {

        String exceptionMessage = messageSourceService.getMessage("demo-sre.service-unavailable");

        HttpStatus httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.BULKHEAD_FULL, exceptionMessage);

        final String errorMessage = MessageFormat.format("handleBulkheadFullException: {0}", response);
        log.error(errorMessage, exception);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(TimeOutException.class)
    public ResponseEntity<Object> handleTimeOutException(final TimeOutException exception, final WebRequest request) {

        String exceptionMessage = messageSourceService.getMessage("sre.generic-error", exception.getMessage());

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.INTERNAL_SERVER_ERROR, exceptionMessage);

        final String errorMessage = MessageFormat.format("handleTimeOutException: {0}", response);
        log.error(errorMessage, exception);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Object> handleConnectException(final ConnectException exception, final WebRequest request) {

        String exceptionMessage = messageSourceService.getMessage("sre.generic-error", exception.getMessage());

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.INTERNAL_SERVER_ERROR, exceptionMessage);

        final String errorMessage = MessageFormat.format("handleConnectException: {0}", response);
        log.error(errorMessage, exception);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Object> handleHttpServerErrorException(final HttpServerErrorException exception, final WebRequest request) {

        String exceptionMessage = messageSourceService.getMessage("sre.generic-error", exception.getMessage());

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.INTERNAL_SERVER_ERROR, exceptionMessage);

        final String errorMessage = MessageFormat.format("handleHttpServerErrorException: {0}", response);
        log.error(errorMessage, exception);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(MongoTimeoutException.class)
    public ResponseEntity<Object> handleMongoTimeoutException(final MongoTimeoutException exception, final WebRequest request) {

        String exceptionMessage = messageSourceService.getMessage("sre.generic-error", exception.getMessage());

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.INTERNAL_SERVER_ERROR, exceptionMessage);

        final String errorMessage = MessageFormat.format("handleMongoTimeoutException: {0}", response);
        log.error(errorMessage, exception);

        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }
}
