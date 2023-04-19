package br.com.bradescoseguros.opin.external.exception;

import br.com.bradescoseguros.opin.businessrule.exception.*;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorCode;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.external.exception.entities.MetaData;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BAD_REQUEST_CODE = HttpStatus.BAD_REQUEST.toString();
    private static final String NOT_FOUND_CODE = HttpStatus.NOT_FOUND.toString();
    private static final String UNPROCESSABLE_ENTITY_CODE = HttpStatus.UNPROCESSABLE_ENTITY.toString();
    private static final String SERVICE_UNAVAILABLE_CODE = HttpStatus.SERVICE_UNAVAILABLE.toString();
    private static final String EXCEPTION = "exception";
    private static final String NO_CONTENT_CODE = HttpStatus.NO_CONTENT.toString();

    private final MessageSourceService messageSourceService;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final MetaDataEnvelope response = new MetaDataEnvelope(BAD_REQUEST_CODE, processErrorsParameters(exception));
        log.warn("ValidationException: {}", response);
        return handleExceptionInternal(exception, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException exception,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final MetaDataEnvelope response = new MetaDataEnvelope(BAD_REQUEST_CODE, processMethodNotReadableErrors(exception));
        log.warn("HttpMessageNotReadableException: {}", response);
        return handleExceptionInternal(exception, response, headers, status, request);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<MetaDataEnvelope> handleValidationException(final Exception exception) {
        final ValidationException validationException = (ValidationException) exception;
        final MetaDataEnvelope response = new MetaDataEnvelope(BAD_REQUEST_CODE, validationException.getErrors());
        log.warn("ValidationException: {}", response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(final RuntimeException exception,
                                                                final WebRequest request) {
        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) exception;
        final MetaDataEnvelope response = new MetaDataEnvelope(NOT_FOUND_CODE, ErrorCode.NOT_FOUND, entityNotFoundException.getMessage());
        log.warn("EntityNotFoundException: {}", response);
        return handleExceptionInternal(exception, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(final RuntimeException exception,
                                                          final WebRequest request) {
        final BusinessException businessException = (BusinessException) exception;
        MetaDataEnvelope envelope =
                new MetaDataEnvelope(UNPROCESSABLE_ENTITY_CODE, ErrorCode.BUSINESS_ERROR, businessException.getMessage());
        if (!isEmpty(businessException.getErrors())) {
            final MetaData meta = new MetaData(UNPROCESSABLE_ENTITY_CODE, MDC.get("TRACE_ID"));
            envelope = new MetaDataEnvelope(meta, businessException.getErrors());
        }
        final String errorMessage = MessageFormat.format("BusinessException: {0}", envelope);
        log.warn(errorMessage, EXCEPTION, exception);
        return handleExceptionInternal(exception, envelope, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(final RuntimeException exception,
                                                          final WebRequest request) {
        final NotFoundException notFoundException = (NotFoundException) exception;

        MetaDataEnvelope envelope =
                new MetaDataEnvelope(NOT_FOUND_CODE, ErrorCode.NOT_FOUND, notFoundException.getMessage());

        if (!isEmpty(notFoundException.getErrors())) {
            final MetaData meta = new MetaData(NOT_FOUND_CODE, MDC.get("TRACE_ID"));
            envelope = new MetaDataEnvelope(meta, notFoundException.getErrors());
        }

        final String errorMessage = MessageFormat.format("NotFoundException: {0}", envelope);
        log.warn(errorMessage, EXCEPTION, exception);
        return handleExceptionInternal(exception, envelope, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(GatewayException.class)
    public ResponseEntity<Object> handleGatewayException(final RuntimeException exception,
                                                         final WebRequest request) {
        final GatewayException gatewayException = (GatewayException) exception;
        MetaDataEnvelope response =
                new MetaDataEnvelope(SERVICE_UNAVAILABLE_CODE, ErrorCode.GATEWAY_ERROR, gatewayException.getMessage());

        if (!isEmpty(gatewayException.getErrors())) {
            final MetaData meta = new MetaData(SERVICE_UNAVAILABLE_CODE, MDC.get("TRACE_ID"));
            response = new MetaDataEnvelope(meta, gatewayException.getErrors());
        }

        final String errorMessage = MessageFormat.format("GatewayException: {0}", response);
        log.error(errorMessage, EXCEPTION, exception);
        return handleExceptionInternal(exception, response, new HttpHeaders(),
                HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<Object> handleNoContentException(final NoContentException exception) {
        final String warnMessage = MessageFormat.format("NoContentException: {0}", exception.getMessage());

        if(exception.getMessage().equals("info")) {
            log.info(warnMessage, NO_CONTENT_CODE, exception.getLocalizedMessage());
        }
        if(exception.getMessage().equals("warn")) {
            log.warn(warnMessage, NO_CONTENT_CODE, exception.getLocalizedMessage());
        }

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException exception,
                                                                     final WebRequest request) {

        final Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        final Set<ErrorData> errors = new HashSet<>();

        constraintViolations
                .forEach(constraintViolation -> extractErrorValuesFrom(errors, constraintViolation));

        final MetaDataEnvelope response = new MetaDataEnvelope(HttpStatus.BAD_REQUEST.toString(), errors);
        log.warn("ConstraintViolationException: {}", response);
        return handleExceptionInternal(exception, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private void extractErrorValuesFrom(final Set<ErrorData> errors, final ConstraintViolation<?> constraintViolation) {
        String fieldName = null;
        for (Path.Node node : constraintViolation.getPropertyPath()) {
            fieldName = node.getName();
        }

        errors.add(new ErrorData(ErrorCode.BAD_REQUEST.name(),
                messageSourceService.getMessage("home-insurance.default-error-msg", fieldName, constraintViolation.getMessage())));
    }

    private Set<ErrorData> processErrorsParameters(final MethodArgumentNotValidException exception) {
        final BindingResult result = exception.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        final Set<ErrorData> errors = new HashSet<>();

        fieldErrors.forEach(fieldError ->
                errors.add(
                        new ErrorData(
                                ErrorCode.INVALID_PARAMETER + " " + fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                ));
        return errors;
    }

    private Set<ErrorData> processMethodNotReadableErrors(final HttpMessageNotReadableException exception) {
        return Set.of(new ErrorData(ErrorCode.INVALID_PARAMETER.name(), exception.getMessage()));
    }
}