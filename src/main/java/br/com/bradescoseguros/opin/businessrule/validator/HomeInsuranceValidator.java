package br.com.bradescoseguros.opin.businessrule.validator;


import br.com.bradescoseguros.opin.businessrule.exception.ValidationException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeInsuranceValidator {

    private static final Integer QTD_CHARACTERS_ZIP_CODE = 8;
    private static final String KEY_ZIPCODE = "zip-code";
    private static final String KEY_PAGE = "page";
    private static final String KEY_PAGE_SIZE = "page-size";
    private static final String KEY_PAGE_SHOULD_BE_GREATER_THAN_ZERO = "home-insurance.page-should-be-greater-than-zero";
    private static final String KEY_PAGE_SIZE_MAX_LIMIT_EXCEEDED = "home-insurance.page-size-max-limited-exceed";
    private static final String KEY_ZIP_CODE_CANNOT_BE_EMPTY = "home-insurance.zipcode-cannot-be-empty";
    private static final String KEY_ZIP_CODE_SHOULD_HAVE_EIGTH_DIGITS = "home-insurance.zipcode-should-be-have-eight-digits";
    private static final String KEY_ZIP_CODE_ONLY_NUMBERS_ARE_ALLOWED = "home-insurance.zipcode-only-numbers-are-allowed";

    private final MessageSourceService messageSourceService;

    public void execute(final String zipCode, final int page, final int size) {

        final Set<ErrorData> errors = new HashSet<>();

        validateZipCode(errors, zipCode);
        validateSize(errors, size);
        validatePage(errors, page);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateZipCode(final Set<ErrorData> errors, final String zipCode) {
        if (StringUtils.isBlank(zipCode)) {
            generateValidationError(errors, messageSourceService
                    .getMessage(KEY_ZIP_CODE_CANNOT_BE_EMPTY, KEY_ZIPCODE), KEY_ZIPCODE);
        }
        else {
            if (QTD_CHARACTERS_ZIP_CODE.compareTo(zipCode.length()) != 0) {
                generateValidationError(errors, messageSourceService
                        .getMessage(KEY_ZIP_CODE_SHOULD_HAVE_EIGTH_DIGITS, KEY_ZIPCODE), KEY_ZIPCODE);
            }
            if (!StringUtils.isNumeric(zipCode)) {
                generateValidationError(errors, messageSourceService
                        .getMessage(KEY_ZIP_CODE_ONLY_NUMBERS_ARE_ALLOWED, KEY_ZIPCODE), KEY_ZIPCODE);
            }
        }
    }

    private void validatePage(final Set<ErrorData> errors, final int page) {
        if (page < 0) {
            generateValidationError(errors, messageSourceService
                    .getMessage(KEY_PAGE_SHOULD_BE_GREATER_THAN_ZERO, KEY_PAGE), KEY_PAGE);
        }
    }

    private void validateSize(final Set<ErrorData> errors, final int pageSize) {
        if (pageSize > 1000) {
            generateValidationError(errors, messageSourceService
                    .getMessage(KEY_PAGE_SIZE_MAX_LIMIT_EXCEEDED, KEY_PAGE_SIZE), KEY_PAGE_SIZE);
        }
    }

    private void generateValidationError(final Set<ErrorData> errors,
                                         final String errorMessage, final String field) {
        errors.add(new ErrorData(field, errorMessage));
    }
}