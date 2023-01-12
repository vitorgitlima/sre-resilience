package br.com.bradescoseguros.opin.external.exception;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSRENoContentException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorCode;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.external.exception.entities.MetaData;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//	@ExceptionHandler(DemoSRENoContentException.class)
//	public ResponseEntity<Object> handleNoContentException(final DemoSRENoContentException exception) {
//		final String warnMessage = MessageFormat.format("NoContentException: {0}", exception.getMessage());
//		log.warn(warnMessage, HttpStatus.NO_CONTENT.toString(), exception.getLocalizedMessage());
//
//		return ResponseEntity.ok(new DemoSRE());
//	}

    @ExceptionHandler(DemoSRENoContentException.class)
    public ResponseEntity<Object> handleGatewayException(final DemoSRENoContentException exception,
                                                         final WebRequest request) {
        MetaDataEnvelope response =
                new MetaDataEnvelope(HttpStatus.NO_CONTENT.toString(), ErrorCode.DEMOSRE_NO_CONTENT, exception.getMessage());

        if (!isEmpty(exception.getErrors())) {
            final MetaData meta = new MetaData(HttpStatus.NO_CONTENT.toString());
            response = new MetaDataEnvelope(meta, exception.getErrors());
        }

        final String errorMessage = MessageFormat.format("DemoSRENoContentException: {0}", response);
        log.error(errorMessage, "exception", exception);
        return handleExceptionInternal(exception, response, new HttpHeaders(),
                HttpStatus.NO_CONTENT, request);
    }

}
