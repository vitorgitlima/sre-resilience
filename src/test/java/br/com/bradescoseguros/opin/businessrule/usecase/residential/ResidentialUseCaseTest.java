package br.com.bradescoseguros.opin.businessrule.usecase.residential;

import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.ResidentialGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidentialUseCaseTest {

	private static final String CODE = "12";
	private static final String KEY_EMPTY_PRODUCTS_FOR_ZIPCODE = "residential-range-cep.empty-products-for-zipcode";

	@Mock
	private ResidentialGateway mockResidentialGateway;

	@Mock
	private MessageSourceService mockMessageSourceService;

	@InjectMocks
	private ResidentialUseCaseImpl residentialUseCase;

	@Test
	@Tag("unit")
	void getResidentialProducts_withPageableAndRangeZipCodeParameters_shouldReturnAPageOfResidentialDomain() {

		//Arrange
		final List<String> listOfCodes = List.of(CODE);
		final Page<ResidentialDomain> pageResponse = getPageResponse();
		final Pageable pageable = PageRequest.of(0, 10);
		when(mockResidentialGateway.findByProductCodes(pageable, listOfCodes)).thenReturn(pageResponse);

		//Act
		final Page<ResidentialDomain> response = residentialUseCase.getResidentialProducts(pageable, getResidentialRangeCepList());

		//Asserts
		assertThat(response).isNotEmpty().isEqualTo(pageResponse);
		assertThat(response.getContent()).isNotEmpty();
		final List<ResidentialDomain> residentialDomains = response.getContent();
	  	assertThat(residentialDomains).containsExactlyInAnyOrderElementsOf(pageResponse.getContent());
	  	verify(mockResidentialGateway).findByProductCodes(pageable, listOfCodes);
		verify(mockMessageSourceService, never()).getMessage(KEY_EMPTY_PRODUCTS_FOR_ZIPCODE);
	}

	@Test
	@Tag("unit")
	void getResidentialProducts_withoutResultOfResidentialDomain_shouldThrowANoContentException() {

		//Arrange
		final List<String> listOfCodes = List.of(CODE);
		final Page<ResidentialDomain> pageResponse = new PageImpl<>(Collections.emptyList());
		final Pageable pageable = PageRequest.of(0, 10);
		final List<ResidentialRangeCep> residentialRangeCepList = getResidentialRangeCepList();
		when(mockResidentialGateway.findByProductCodes(pageable, listOfCodes)).thenReturn(pageResponse);

		//Act
		assertThrows(NoContentException.class, () -> residentialUseCase.getResidentialProducts(pageable, residentialRangeCepList));

		//Asserts
	 	verify(mockResidentialGateway).findByProductCodes(pageable, listOfCodes);
	}

	@Test
	@Tag("unit")
	void getResidentialProducts_withNullRangeCep_shouldBeNullPointerException() {

		//Arrange
		final Pageable pageable = PageRequest.of(0, 10);

		//Act
		assertThrows(NullPointerException.class, () -> residentialUseCase.getResidentialProducts(pageable, null));

		//Asserts
		verifyNoInteractions(mockResidentialGateway);
	}

	private Page<ResidentialDomain> getPageResponse() {
		var residentialOne = DummyObjectsUtil.newInstance(ResidentialDomain.class);
		var residentialTwo = DummyObjectsUtil.newInstance(ResidentialDomain.class);
		var residentialThree = DummyObjectsUtil.newInstance(ResidentialDomain.class);

		return new PageImpl<>(List.of(residentialOne, residentialTwo, residentialThree));
	}

	private List<ResidentialRangeCep> getResidentialRangeCepList() {
		var dummyResidential = DummyObjectsUtil.newInstance(ResidentialRangeCep.class);
		dummyResidential.setProductCode(CODE);

		return List.of(dummyResidential);
	}
}