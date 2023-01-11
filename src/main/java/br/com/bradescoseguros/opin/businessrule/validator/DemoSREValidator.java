package br.com.bradescoseguros.opin.businessrule.validator;

import br.com.bradescoseguros.opin.businessrule.exception.ValidationException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class DemoSREValidator {
	private final Set<ErrorData> errors = new HashSet<>();

	private static final String ERROR_MESSAGE = "demo-sre.invalid-property";

	@Autowired
	private MessageSourceService messageSourceService;

	public void execute(final DemoSRE payload) {
		this.validatePayload(this.errors, payload);

		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
	}

	private void validatePayload(final Set<ErrorData> errors, final DemoSRE payload) {
		if (Objects.isNull(payload)) {
			generateValidationError(errors, messageSourceService
					.getMessage(ERROR_MESSAGE), null);
			return;
		}

		this.validateId(this.errors, payload.getId());
		this.validateValue(this.errors, payload.getValue());
	}

	private void validateId(final Set<ErrorData> errors, final Integer id) {
		final String field = "id";

		if (Objects.isNull(id) || id <= 0) {
			generateValidationError(errors, messageSourceService
					.getMessage(ERROR_MESSAGE, field), field);
		}
	}

	private void validateValue(final Set<ErrorData> errors, final String value) {
		final String field = "value";

		if (!StringUtils.hasText(value)) {
			generateValidationError(errors, messageSourceService
					.getMessage(ERROR_MESSAGE, field), field);
		}
	}

	private void generateValidationError(final Set<ErrorData> errors,
										 final String errorMessage, final String field) {
		errors.add(new ErrorData(field, errorMessage));
	}
}
