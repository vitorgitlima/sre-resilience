package br.com.bradescoseguros.opin.external.exception;

import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSREBadRequestException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSREMaxRetriesExceededException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSRENoContentException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorCode;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.external.exception.entities.MetaData;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;

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
            final MetaData meta = new MetaData(HttpStatus.NO_CONTENT.toString());
            response = new MetaDataEnvelope(meta, exception.getErrors());
        }

        final String errorMessage = MessageFormat.format("DemoSRENoContentException: {0}", response);
        log.error(errorMessage);
        return handleExceptionInternal(exception, response, new HttpHeaders(),
                HttpStatus.NO_CONTENT, request);
    }

    @ExceptionHandler(DemoSREMaxRetriesExceededException.class)
    public ResponseEntity<Object> handleDemoSREMaxRetriesExceededException(final DemoSREMaxRetriesExceededException exception,
                                                                  final WebRequest request) {

        String exceptionMessage = exception.getMessage();

        if(!StringUtils.hasText(exceptionMessage)) {
            exceptionMessage = messageSourceService.getMessage("demo-sre.service-unavailable");
        }

        HttpStatus httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.DEMOSRE_MAX_RETRIES_EXCEEDED, exceptionMessage);

        if (!isEmpty(exception.getErrors())) {
            final MetaData meta = new MetaData(httpStatus.toString());
            response = new MetaDataEnvelope(meta, exception.getErrors());
        }

        final String errorMessage = MessageFormat.format("handleDemoSREMaxRetriesExceededException: {0}", response);
        log.error(errorMessage);
        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

    @ExceptionHandler(DemoSREBadRequestException.class)
    public ResponseEntity<Object> handleDemoSREBadRequestException(final DemoSREBadRequestException exception, final WebRequest request) {
        String exceptionMessage = exception.getMessage();

        if(!StringUtils.hasText(exceptionMessage)) {
            exceptionMessage = messageSourceService.getMessage("demo-sre.bad-request");
        }

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        MetaDataEnvelope response =
                new MetaDataEnvelope(httpStatus.toString(), ErrorCode.DEMOSRE_MAX_RETRIES_EXCEEDED, exceptionMessage);

        if (!isEmpty(exception.getErrors())) {
            final MetaData meta = new MetaData(httpStatus.toString());
            response = new MetaDataEnvelope(meta, exception.getErrors());
        }

        final String errorMessage = MessageFormat.format("handleDemoSREBadRequestException: {0}", response);
        log.error(errorMessage);
        return handleExceptionInternal(exception, response, new HttpHeaders(),
                httpStatus, request);
    }

}
