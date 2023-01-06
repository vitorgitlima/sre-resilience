package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import br.com.bradescoseguros.opin.interfaceadapter.repository.ResidentialRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidentialGatewayImplTest {

	private static final String CODE = "12";
	private final List<String> listOfCode = List.of(CODE);

	@Mock
	private ResidentialRepository mockResidentialRepository;

	@Mock
	private Pageable mockPageable;

	@InjectMocks
	private ResidentialGatewayImpl residentialGateway;

	@Test
	@Tag("unit")
	void findByProductCode_withPageableAndRangeZipCode_shouldReturnListOfProducts() {
		//Arrange
		final Page<ResidentialDomain> residentialDomains = generateListOfProducts();
		when(mockResidentialRepository.findByProductCodeIn(mockPageable, listOfCode)).thenReturn(residentialDomains);

		//Act
		Page<ResidentialDomain> productPage = residentialGateway.findByProductCodes(mockPageable, listOfCode);

		//Asserts
		assertThat(productPage).isNotEmpty();
		assertThat(productPage.getContent())
						.isNotEmpty()
						.containsExactlyInAnyOrderElementsOf(residentialDomains.getContent());
		verify(mockResidentialRepository).findByProductCodeIn(mockPageable, listOfCode);
	}

	@Test
	@Tag("unit")
	void findByProductCode_withNullPageable_shouldThrowNullPointerException() {
		//Act
		assertThrows(NullPointerException.class, () -> residentialGateway.findByProductCodes(null, listOfCode));

		//Asserts
		verify(mockResidentialRepository, never()).findByProductCodeIn(any(Pageable.class), any());
	}

	@Test
	@Tag("unit")
	void findByProductCode_withNullCode_shouldThrowNullPointerException() {
		//Act
		assertThrows(NullPointerException.class, () -> residentialGateway.findByProductCodes(mockPageable, null));

		//Asserts
		verify(mockResidentialRepository, never()).findByProductCodeIn(any(Pageable.class), any());
	}

	private Page<ResidentialDomain> generateListOfProducts() {
		Pageable pageable = PageRequest.of(1, 10, Sort.unsorted());
		var productOne = DummyObjectsUtil.newInstance(ResidentialDomain.class);
		productOne.getProduct().setCode(CODE);
		var productTwo = DummyObjectsUtil.newInstance(ResidentialDomain.class);
		productTwo.getProduct().setCode(CODE);
		var productThree = DummyObjectsUtil.newInstance(ResidentialDomain.class);
		productThree.getProduct().setCode(CODE);
		var productFour = DummyObjectsUtil.newInstance(ResidentialDomain.class);
		productFour.getProduct().setCode(CODE);

		return PageableExecutionUtils.getPage(List.of(productOne, productTwo, productThree, productFour), pageable, () -> 4);
	}

}