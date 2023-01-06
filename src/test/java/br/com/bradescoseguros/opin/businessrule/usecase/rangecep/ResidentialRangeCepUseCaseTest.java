package br.com.bradescoseguros.opin.businessrule.usecase.rangecep;

import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.ResidentialRangeCepGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidentialRangeCepUseCaseTest {

    private static final String ZIP_CODE = "12345678";
    private static final String FX_INIT_CEP = "00000001";
    private static final String FX_END_CEP = "99999999";
    private static final String KEY_EMPTY_PRODUCTS_FOR_ZIPCODE = "residential-range-cep.empty-products-for-zipcode";
    private static final String ERROR_EMPTY_PRODUCTS_FOR_ZIP_CODE_EXPECTED_MSG = "No momento, não existe nenhum produto elegível para o CEP informado.";

    @Mock
    private ResidentialRangeCepGateway mockResidentialRangeCepGateway;

    @Mock
    private MessageSourceService mockMessageSourceService;

    @InjectMocks
    private ResidentialRangeCepUseCaseImpl residentialRangeCepUseCase;

    @Test
    @Tag("unit")
    void findListProductElegible_withZipCodeThatContainsProducts_thenReturnProducts() {
        //Arrange
        List<ResidentialRangeCep> dummyObjectsForSearch = findDummyObjectsForSearch();
        when(mockResidentialRangeCepGateway.findListElegibleProductsByCep(ZIP_CODE))
                .thenReturn(dummyObjectsForSearch);

        //Act
        List<ResidentialRangeCep> mockListElegibleProductsByCep = residentialRangeCepUseCase
                .findListElegibleProductsByCep(ZIP_CODE);

        //Asserts
        assertThat(mockListElegibleProductsByCep.size()).isEqualTo(1);
        assertEquals(dummyObjectsForSearch, mockListElegibleProductsByCep);
        final ResidentialRangeCep residentialRangeCep = mockListElegibleProductsByCep.get(0);
        assertThat(ZIP_CODE).isBetween(residentialRangeCep.getPostalCodeStart(), residentialRangeCep.getPostalCodeEnd());
        verify(mockResidentialRangeCepGateway).findListElegibleProductsByCep(ZIP_CODE);
        verifyNoInteractions(mockMessageSourceService);
        verifyNoMoreInteractions(mockResidentialRangeCepGateway);
    }

    @Test
    @Tag("unit")
    void findListElegibleProducts_withAZipCodeWithoutAssociatedProducts_shouldBeThrowsNoContentException() {
        //Arrange
        when(mockResidentialRangeCepGateway.findListElegibleProductsByCep(ZIP_CODE))
                .thenReturn(Collections.emptyList());
        when(mockMessageSourceService.getMessage(KEY_EMPTY_PRODUCTS_FOR_ZIPCODE))
                .thenReturn(ERROR_EMPTY_PRODUCTS_FOR_ZIP_CODE_EXPECTED_MSG);

        //Act
        NoContentException noContentException = assertThrows(NoContentException.class,
                () -> residentialRangeCepUseCase.findListElegibleProductsByCep(ZIP_CODE));

        //Asserts
        assertEquals(ERROR_EMPTY_PRODUCTS_FOR_ZIP_CODE_EXPECTED_MSG, noContentException.getMessage());
        verify(mockMessageSourceService).getMessage(KEY_EMPTY_PRODUCTS_FOR_ZIPCODE);
        verifyNoMoreInteractions(mockResidentialRangeCepGateway);
    }

    @Test
    @Tag("unit")
    void findListElegibleProducts_withANullZipCode_shouldBeThrowsNullPointerException() {
        //Arrange

        //Act
        assertThrows(NullPointerException.class,
                () -> residentialRangeCepUseCase.findListElegibleProductsByCep(null));

        //Asserts
        verifyNoMoreInteractions(mockResidentialRangeCepGateway);
    }

    private List<ResidentialRangeCep> findDummyObjectsForSearch() {
        var residential1 = DummyObjectsUtil.newInstance(ResidentialRangeCep.class);
        residential1.setPostalCodeStart(FX_INIT_CEP);
        residential1.setPostalCodeEnd(FX_END_CEP);

        return List.of(residential1);
    }
}