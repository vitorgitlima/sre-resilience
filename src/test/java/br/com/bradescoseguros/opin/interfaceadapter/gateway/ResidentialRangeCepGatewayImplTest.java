package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import br.com.bradescoseguros.opin.interfaceadapter.repository.ResidentialRangeCepRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidentialRangeCepGatewayImplTest {

    private static final String VALID_ZIP_CODE = "12345678";
    private static final String FX_INIT_CEP = "00000001";
    private static final String FX_END_CEP = "99999999";

    @Mock
    private ResidentialRangeCepRepository mockResidentialRangeCepRepository;

    @InjectMocks
    private ResidentialRangeCepGatewayImpl residentialRangeCepGateway;

    @Test
    @Tag("unit")
    void findListElegibleProducts_withZipCodeThatContainsProducts_shouldBeReturnListResidentialRangeCep() {
        //Arrange
        List<ResidentialRangeCep> dummyObjectsForSearch = findDummyObjectsForSearch();
        when(mockResidentialRangeCepRepository.findByPostalCodeStartLessThanEqualAndPostalCodeEndGreaterThanEqual(VALID_ZIP_CODE, VALID_ZIP_CODE))
                .thenReturn(dummyObjectsForSearch);

        //Act
        List<ResidentialRangeCep> elegibleProductsByCep = residentialRangeCepGateway.findListElegibleProductsByCep(VALID_ZIP_CODE);

        //Asserts
        assertThat(elegibleProductsByCep).isNotEmpty();
        assertThat(elegibleProductsByCep.size()).isEqualTo(2);
        assertEquals(dummyObjectsForSearch, elegibleProductsByCep);
        ResidentialRangeCep residentialRangeCep = elegibleProductsByCep.get(0);
        assertThat(VALID_ZIP_CODE).isBetween(residentialRangeCep.getPostalCodeStart(), residentialRangeCep.getPostalCodeEnd());
        verify(mockResidentialRangeCepRepository).findByPostalCodeStartLessThanEqualAndPostalCodeEndGreaterThanEqual(VALID_ZIP_CODE, VALID_ZIP_CODE);
        verifyNoMoreInteractions(mockResidentialRangeCepRepository);
    }

    @Test
    @Tag("unit")
    void findListElegibleProducts_withZipCodeThatDoesntProducts_shouldBeThrowsNoContentException() {
        //Arrange
        when(mockResidentialRangeCepRepository.findByPostalCodeStartLessThanEqualAndPostalCodeEndGreaterThanEqual(VALID_ZIP_CODE, VALID_ZIP_CODE))
                .thenThrow(NoContentException.class);
        //Act
        assertThrows(NoContentException.class, () -> mockResidentialRangeCepRepository
                .findByPostalCodeStartLessThanEqualAndPostalCodeEndGreaterThanEqual(VALID_ZIP_CODE, VALID_ZIP_CODE));

        //Asserts
        verifyNoMoreInteractions(mockResidentialRangeCepRepository);
    }

    private List<ResidentialRangeCep> findDummyObjectsForSearch() {
        var residential1 = DummyObjectsUtil.newInstance(ResidentialRangeCep.class);
        residential1.setPostalCodeStart(FX_INIT_CEP);
        residential1.setPostalCodeEnd(FX_END_CEP);
        return List.of(
                    residential1,
                    DummyObjectsUtil.newInstance(ResidentialRangeCep.class)
            );
    }
}