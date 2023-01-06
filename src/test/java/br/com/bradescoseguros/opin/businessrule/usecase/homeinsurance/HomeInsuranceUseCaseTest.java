package br.com.bradescoseguros.opin.businessrule.usecase.homeinsurance;

import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.exception.ValidationException;
import br.com.bradescoseguros.opin.businessrule.exception.entities.ErrorData;
import br.com.bradescoseguros.opin.businessrule.usecase.rangecep.ResidentialRangeCepUseCase;
import br.com.bradescoseguros.opin.businessrule.usecase.residential.ResidentialUseCase;
import br.com.bradescoseguros.opin.businessrule.validator.HomeInsuranceValidator;
import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeInsuranceUseCaseTest {

	private static final String ZIP_CODE = "1000000";
	private static final String ERROR_MSG = "ERROR";
	private static final String KEY_PAGE_SIZE = "page-size";
	private static final String PAGE_SIZE_LIMIT_EXCEEDED_DETAIL = "Campo page-size não deve ser maior que 1000.";
	private static final String INVALID_ZIP_CODE_TITLE = "INVALID_PARAMETER zip-code";
	private static final String INVALID_SIZE_OF_ZIP_CODE_DETAIL = "Campo zip-code deve conter exatamente 8 caracteres.";
	private static final String INVALID_CHARACTERS_IN_ZIP_CODE_DETAIL = "Campo zip-code deve conter apenas números.";
	private static final String INVALID_BLANK_ZIP_CODE_DETAIL = "Campo zip-code não pode estar vazio.";

	@Mock
	private HomeInsuranceValidator mockHomeInsuranceValidator;

	@Mock
	private ResidentialUseCase mockResidentialUseCase;

	@Mock
	private ResidentialRangeCepUseCase mockResidentialRangeCepUseCase;

	@InjectMocks
	private HomeInsuranceUseCaseImpl homeInsuranceUC;

	@Test
	@Tag("unit")
	void getResidentialInsuranceProducts_withPageableAndZipCodeThatContainsProducts_shouldBeReturnPageOfProducts() {

		//Arrange
		final Page<ResidentialDomain> pageResponse = getPageResponse();
		final Pageable pageable = PageRequest.of(0, 10);
		final List<ResidentialRangeCep> residentialRangeCepList = getResidentialRangeCepList();
		when(mockResidentialRangeCepUseCase.findListElegibleProductsByCep(ZIP_CODE)).thenReturn(residentialRangeCepList);
		when(mockResidentialUseCase.getResidentialProducts(pageable, residentialRangeCepList)).thenReturn(pageResponse);

		//Act
		Page<ResidentialDomain> residentialInsuranceProducts = homeInsuranceUC.getResidentialInsuranceProducts(pageable, ZIP_CODE);

		//Asserts
		assertThat(residentialInsuranceProducts).isNotEmpty();
		assertThat(residentialInsuranceProducts.getTotalElements()).isEqualTo(3);
		assertThat(residentialInsuranceProducts.getContent()).isNotEmpty();
		assertThat(residentialInsuranceProducts.getSize()).isEqualTo(pageResponse.getTotalElements());

		final ResidentialDomain residentialDomain = pageResponse.getContent().get(0);
		assertEquals(residentialDomain, residentialInsuranceProducts.getContent().get(0));
		verify(mockHomeInsuranceValidator).execute(ZIP_CODE, pageable.getPageNumber(), pageable.getPageSize());
		verify(mockResidentialRangeCepUseCase).findListElegibleProductsByCep(ZIP_CODE);
		verify(mockResidentialUseCase).getResidentialProducts(pageable, residentialRangeCepList);
	}

	@Test
	@Tag("unit")
	void getResidentialInsuranceProducts_withPageableAndZipCodeWithoutAssociatedProducts_shouldBeThrowsNoContentException() {
		//Arrange
		final Pageable pageable = PageRequest.of(0, 10);
		when(mockResidentialRangeCepUseCase.findListElegibleProductsByCep(ZIP_CODE)).thenThrow(new NoContentException(ERROR_MSG));

		//Act
		NoContentException noContentException = assertThrows(NoContentException.class,
				() -> homeInsuranceUC.getResidentialInsuranceProducts(pageable, ZIP_CODE));

		//Asserts
		assertThat(noContentException.getMessage()).isEqualTo(ERROR_MSG);
		verify(mockHomeInsuranceValidator).execute(ZIP_CODE, pageable.getPageNumber(), pageable.getPageSize());
		verify(mockResidentialRangeCepUseCase).findListElegibleProductsByCep(ZIP_CODE);
		verifyNoInteractions(mockResidentialUseCase);
	}

	@Test
	@Tag("unit")
	void getResidentialInsuranceProducts_withBlankZipCode_shouldThrowsValidationException() {
		//Arrange
		final String blankZipCode = " ";
		final Pageable pageable = PageRequest.of(0, 10);
		doThrow(new ValidationException(Set.of(new ErrorData(INVALID_ZIP_CODE_TITLE, INVALID_BLANK_ZIP_CODE_DETAIL))))
				.when(mockHomeInsuranceValidator).execute(blankZipCode,pageable.getPageNumber(), pageable.getPageSize());

		//Act
		ValidationException validationException = assertThrows(ValidationException.class,
				() -> homeInsuranceUC.getResidentialInsuranceProducts(pageable, blankZipCode));

		//Asserts
		assertValidationException(validationException);
		assertErrorData(validationException.getErrors(), INVALID_BLANK_ZIP_CODE_DETAIL);
		verifyNoInteractions(mockResidentialRangeCepUseCase);
		verifyNoInteractions(mockResidentialUseCase);
	}

	@Test
	@Tag("unit")
	void getResidentialInsuranceProducts_withInvalidZipCodeBySize_shouldThrowsValidationException() {
		//Arrange
		final String incompleteZipCode = "12345";
		final Pageable pageable = PageRequest.of(0, 10);
		doThrow(new ValidationException(Set.of(new ErrorData(INVALID_ZIP_CODE_TITLE, INVALID_SIZE_OF_ZIP_CODE_DETAIL))))
				.when(mockHomeInsuranceValidator).execute(incompleteZipCode,pageable.getPageNumber(), pageable.getPageSize());

		//Act
		ValidationException validationException = assertThrows(ValidationException.class,
				() -> homeInsuranceUC.getResidentialInsuranceProducts(pageable, incompleteZipCode));

		//Asserts
		assertThat(validationException.getErrors()).isNotEmpty();
		assertThat(validationException.getErrors().size()).isEqualTo(1);
		assertValidationException(validationException);
		assertErrorData(validationException.getErrors(), INVALID_SIZE_OF_ZIP_CODE_DETAIL);
		verify(mockHomeInsuranceValidator).execute(incompleteZipCode, pageable.getPageNumber(), pageable.getPageSize());
		verifyNoInteractions(mockResidentialRangeCepUseCase);
		verifyNoInteractions(mockResidentialUseCase);
	}

	@Test
	@Tag("unit")
	void getResidentialInsuranceProducts_withInvalidZipCodeByInvalidChars_shouldThrowsValidationException() {

		//Arrange
		final String invalidCharsZipCode = "12345abc";
		final Pageable pageable = PageRequest.of(0, 10);
		doThrow(new ValidationException(Set.of(new ErrorData(INVALID_ZIP_CODE_TITLE, INVALID_CHARACTERS_IN_ZIP_CODE_DETAIL))))
				.when(mockHomeInsuranceValidator).execute(invalidCharsZipCode,pageable.getPageNumber(), pageable.getPageSize());

		//Act
		ValidationException validationException = assertThrows(ValidationException.class,
				() -> homeInsuranceUC.getResidentialInsuranceProducts(pageable, invalidCharsZipCode));

		//Asserts
		assertThat(validationException.getErrors()).isNotEmpty();
		assertThat(validationException.getErrors().size()).isEqualTo(1);
		assertValidationException(validationException);
		assertErrorData(validationException.getErrors(), INVALID_CHARACTERS_IN_ZIP_CODE_DETAIL);
		verify(mockHomeInsuranceValidator).execute(invalidCharsZipCode, pageable.getPageNumber(), pageable.getPageSize());
		verifyNoInteractions(mockResidentialRangeCepUseCase);
		verifyNoInteractions(mockResidentialUseCase);
	}

	@Test
	@Tag("unit")
	void getResidentialInsuranceProducts_withInvalidPageSizeByHugeValueAndZipCodeThatContainsProducts_shouldThrowsValidationException() {

		//Arrange
		final Pageable pageable = PageRequest.of(1000, 2000);
		doThrow(new ValidationException(Set.of(new ErrorData(KEY_PAGE_SIZE, PAGE_SIZE_LIMIT_EXCEEDED_DETAIL))))
				.when(mockHomeInsuranceValidator).execute(ZIP_CODE, pageable.getPageNumber(), pageable.getPageSize());

		//Act
		ValidationException validationException = assertThrows(ValidationException.class,
				() -> homeInsuranceUC.getResidentialInsuranceProducts(pageable, ZIP_CODE));

		//Asserts
		assertThat(validationException.getErrors()).isNotEmpty();
		assertThat(validationException.getErrors().size()).isEqualTo(1);
		assertValidationException(validationException);
		assertErrorData(validationException.getErrors(), PAGE_SIZE_LIMIT_EXCEEDED_DETAIL);
		verify(mockHomeInsuranceValidator).execute(ZIP_CODE, pageable.getPageNumber(), pageable.getPageSize());
		verifyNoInteractions(mockResidentialRangeCepUseCase);
		verifyNoInteractions(mockResidentialUseCase);
	}

	private void assertValidationException(final ValidationException validationException) {
		MatcherAssert.assertThat(validationException, notNullValue());
		MatcherAssert.assertThat(validationException.getErrors(), notNullValue());
		MatcherAssert.assertThat(validationException.getErrors().isEmpty(), is(false));
	}

	private void assertErrorData(final Set<ErrorData> errors, final String invalidDetailMessage) {
		final boolean isValid = errors.stream().allMatch(error ->
				invalidDetailMessage.equals(error.getDetail())
		);
		MatcherAssert.assertThat(isValid, is(true));
	}

	private List<ResidentialRangeCep> getResidentialRangeCepList() {
		return List.of(DummyObjectsUtil.newInstance(ResidentialRangeCep.class));
	}

	private Page<ResidentialDomain> getPageResponse() {
		var residentialOne = DummyObjectsUtil.newInstance(ResidentialDomain.class);
		var residentialTwo = DummyObjectsUtil.newInstance(ResidentialDomain.class);
		var residentialThree = DummyObjectsUtil.newInstance(ResidentialDomain.class);

		return new PageImpl<>(List.of(residentialOne, residentialTwo, residentialThree));
	}
}