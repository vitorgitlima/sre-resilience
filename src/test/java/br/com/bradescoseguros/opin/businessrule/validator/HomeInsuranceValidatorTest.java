package br.com.bradescoseguros.opin.businessrule.validator;

import br.com.bradescoseguros.opin.businessrule.exception.ValidationException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorCode;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.dummy.ObjectRandomUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeInsuranceValidatorTest {

	private static final int VALID_SIZE_VALUE = 10;
	private static final int VALID_PAGE_VALUE = 1;
	private static final String ERROR_MSG = ObjectRandomUtil.nextString(5);
	private static final String VALID_ZIP_CODE = "12345678";
	private static final String KEY_ZIP_CODE = "zip-code";
	private static final String KEY_PAGE_SIZE = "page-size";
	private static final String KEY_PAGE = "page";
	private static final String KEY_PAGE_SHOULD_BE_GREATER_THAN_ZERO = "home-insurance.page-should-be-greater-than-zero";
	private static final String KEY_ZIP_CODE_CANNOT_BE_EMPTY = "home-insurance.zipcode-cannot-be-empty";
	private static final String KEY_PAGE_SIZE_MAX_LIMIT_EXCEEDED = "home-insurance.page-size-max-limited-exceed";
	private static final String KEY_ZIP_CODE_SHOULD_HAVE_EIGTH_DIGITS = "home-insurance.zipcode-should-be-have-eight-digits";
	private static final String KEY_ZIP_CODE_ONLY_NUMBERS_ARE_ALLOWED = "home-insurance.zipcode-only-numbers-are-allowed";

	@InjectMocks
	private HomeInsuranceValidator homeInsuranceValidator;

	@Mock
	private MessageSourceService mockMessageSourceService;

	@Test
	@Tag("unit")
	void execute_withValidParameters_shouldRunSuccessfully() {
		//Act
		assertDoesNotThrow(() -> homeInsuranceValidator.execute(VALID_ZIP_CODE, VALID_PAGE_VALUE, VALID_SIZE_VALUE));

		//Asserts
		verifyNoInteractions(mockMessageSourceService);
	}

	@Test
	@Tag("unit")
	void execute_withBlankZipCode_shouldThrowsValidationException() {

		//Arrange
		when(mockMessageSourceService.getMessage(KEY_ZIP_CODE_CANNOT_BE_EMPTY, KEY_ZIP_CODE))
				.thenReturn(ERROR_MSG);

		//Act
		final ValidationException validationException =
				assertThrows(ValidationException.class, () -> homeInsuranceValidator.execute(" ",
						VALID_PAGE_VALUE, VALID_SIZE_VALUE));

		//Asserts
		assertValidationException(validationException);
		assertErrorData(validationException.getErrors(), ErrorCode.INVALID_PARAMETER + " " + KEY_ZIP_CODE);
		verify(mockMessageSourceService).getMessage(KEY_ZIP_CODE_CANNOT_BE_EMPTY, KEY_ZIP_CODE);
	}

	@Test
	@Tag("unit")
	void execute_withInvalidZipCodeBySize_shouldThrowsValidationException() {

		//Arrange
		when(mockMessageSourceService.getMessage(KEY_ZIP_CODE_SHOULD_HAVE_EIGTH_DIGITS, KEY_ZIP_CODE))
				.thenReturn(ERROR_MSG);

		//Act
		final ValidationException validationException =
				assertThrows(ValidationException.class, () -> homeInsuranceValidator.execute("12345",
						VALID_PAGE_VALUE, VALID_SIZE_VALUE));

		//Asserts
		assertValidationException(validationException);
		assertErrorData(validationException.getErrors(), ErrorCode.INVALID_PARAMETER + " " + KEY_ZIP_CODE);
		verify(mockMessageSourceService).getMessage(KEY_ZIP_CODE_SHOULD_HAVE_EIGTH_DIGITS, KEY_ZIP_CODE);
	}

	@Test
	@Tag("unit")
	void execute_withInvalidZipCodeByInvalidChars_shouldThrowsValidationException() {

		//Arrange
		when(mockMessageSourceService.getMessage(KEY_ZIP_CODE_ONLY_NUMBERS_ARE_ALLOWED, KEY_ZIP_CODE))
				.thenReturn(ERROR_MSG);
		//Act
		final ValidationException validationException =
				assertThrows(ValidationException.class, () -> homeInsuranceValidator.execute("1234567L",
						VALID_PAGE_VALUE, VALID_SIZE_VALUE));

		//Asserts
		assertValidationException(validationException);
		assertErrorData(validationException.getErrors(), ErrorCode.INVALID_PARAMETER + " " + KEY_ZIP_CODE);
		verify(mockMessageSourceService).getMessage(KEY_ZIP_CODE_ONLY_NUMBERS_ARE_ALLOWED, KEY_ZIP_CODE);
	}

	@Test
	@Tag("unit")
	void execute_withInvalidsZipCodePageAndPageSize_shouldThrowsValidationException() {

		//Arrange
		when(mockMessageSourceService.getMessage(KEY_ZIP_CODE_SHOULD_HAVE_EIGTH_DIGITS, KEY_ZIP_CODE))
				.thenReturn(ERROR_MSG);
		when(mockMessageSourceService.getMessage(KEY_ZIP_CODE_ONLY_NUMBERS_ARE_ALLOWED, KEY_ZIP_CODE))
				.thenReturn(ERROR_MSG);
		when(mockMessageSourceService.getMessage(KEY_PAGE_SIZE_MAX_LIMIT_EXCEEDED, KEY_PAGE_SIZE))
				.thenReturn(ERROR_MSG);
		when(mockMessageSourceService.getMessage(KEY_PAGE_SHOULD_BE_GREATER_THAN_ZERO, KEY_PAGE))
				.thenReturn(ERROR_MSG);

		//Act
		final ValidationException validationException =
						assertThrows(ValidationException.class,  () -> homeInsuranceValidator.execute("adfg",-1, 1010));

		//Assert
		assertValidationException(validationException);
		assertThat(validationException.getErrors().size(), is(3));

		final Set<ErrorData> errors = validationException.getErrors();
		final Set<ErrorData> expectedErrors = getExpectedErrors();

		errors.forEach(error -> assertThat(expectedErrors.contains(error), is(true)));
		verify(mockMessageSourceService).getMessage(KEY_ZIP_CODE_SHOULD_HAVE_EIGTH_DIGITS, KEY_ZIP_CODE);
		verify(mockMessageSourceService).getMessage(KEY_ZIP_CODE_ONLY_NUMBERS_ARE_ALLOWED, KEY_ZIP_CODE);
		verify(mockMessageSourceService).getMessage(KEY_PAGE_SIZE_MAX_LIMIT_EXCEEDED, KEY_PAGE_SIZE);
		verify(mockMessageSourceService).getMessage(KEY_PAGE_SHOULD_BE_GREATER_THAN_ZERO, KEY_PAGE);
		verifyNoMoreInteractions(mockMessageSourceService);
	}

	@Test
	@Tag("unit")
	void execute_withInvalidPageSizeByHugeValue_shouldThrowsValidationException() {
		//Arrange
		when(mockMessageSourceService.getMessage(KEY_PAGE_SIZE_MAX_LIMIT_EXCEEDED, KEY_PAGE_SIZE))
				.thenReturn(ERROR_MSG);

		//Act
		final ValidationException validationException =
						assertThrows(ValidationException.class, () -> homeInsuranceValidator.execute(VALID_ZIP_CODE, VALID_PAGE_VALUE, 2000));

		//Asserts
		assertThat(validationException, notNullValue());
		assertThat(validationException.getErrors(), notNullValue());

		final Set<ErrorData> errors = validationException.getErrors();
		assertThat(errors.isEmpty(), equalTo(false));
		assertErrorData(errors, ErrorCode.INVALID_PARAMETER + " " + KEY_PAGE_SIZE);
		verify(mockMessageSourceService).getMessage(KEY_PAGE_SIZE_MAX_LIMIT_EXCEEDED, KEY_PAGE_SIZE);
	}

	@Test
	@Tag("mutation")
	void execute_whenPageIsZero_shouldNotThrowsValidationException() {
		//Act
		assertDoesNotThrow(() -> homeInsuranceValidator.execute(VALID_ZIP_CODE, 0, VALID_SIZE_VALUE));

		//Asserts
		verifyNoInteractions(mockMessageSourceService);
	}

	@Test
	@Tag("mutation")
	void execute_whenPageSizeIsOneThousand_shouldNotThrowsValidationException() {
		//Act
		assertDoesNotThrow(() -> homeInsuranceValidator.execute(VALID_ZIP_CODE, VALID_PAGE_VALUE, 1000));

		//Asserts
		verifyNoInteractions(mockMessageSourceService);
	}

	private void assertValidationException(final ValidationException validationException) {
		assertThat(validationException, notNullValue());
		assertThat(validationException.getErrors(), notNullValue());
		assertThat(validationException.getErrors().isEmpty(), is(false));
	}

	private void assertErrorData(final Set<ErrorData> errors, final String invalidTitleMessage) {
		final boolean isValid = errors.stream().allMatch(error ->
						invalidTitleMessage.equals(error.getTitle())
										&& ERROR_MSG.equals(error.getDetail())
		);
		assertThat(isValid, is(true));
	}

	private Set<ErrorData> getExpectedErrors() {
		return Set.of(new ErrorData(ErrorCode.INVALID_PARAMETER + " " + KEY_PAGE_SIZE, ERROR_MSG),
						new ErrorData(ErrorCode.INVALID_PARAMETER + " " + KEY_ZIP_CODE, ERROR_MSG),
							new ErrorData(ErrorCode.INVALID_PARAMETER + " " + KEY_PAGE, ERROR_MSG));
	}
}