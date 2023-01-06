package br.com.bradescoseguros.opin.interfaceadapter.repository;

import br.com.bradescoseguros.opin.ContextTest;
import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//class ResidentialRangeCepRepositoryTest extends ContextTest {
class ResidentialRangeCepRepositoryTest {

//    private static final String ZIP_CODE = "12345678";
//    private final ResidentialRangeCepRepository residentialRangeCepRepository;
//
//    @Autowired
//    ResidentialRangeCepRepositoryTest(ResidentialRangeCepRepository repository) {
//        this.residentialRangeCepRepository = repository;
//    }
//
//    @BeforeEach
//    public void init() {
//        residentialRangeCepRepository.deleteAll();
//    }
//
//    @Test
//    @Tag("unit")
//    void findResidentialRangeCep_withZipCodeThatContainsProducts_thenReturnListOfProducts() {
//
//        //Arrange
//        ResidentialRangeCep mockResidentialRangeCep = createDummyObjects();
//
//        //Act
//        List<ResidentialRangeCep> listOfResidentialRangeCepByZipCode = residentialRangeCepRepository
//                .findByPostalCodeStartLessThanEqualAndPostalCodeEndGreaterThanEqual(ZIP_CODE, ZIP_CODE);
//
//        //Asserts
//        assertThat(mockResidentialRangeCep).isNotNull();
//        assertThat(listOfResidentialRangeCepByZipCode).isNotEmpty();
//        assertThat(listOfResidentialRangeCepByZipCode.size()).isEqualTo(1);
//        assertThat(listOfResidentialRangeCepByZipCode.get(0))
//                .usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(mockResidentialRangeCep);
//    }
//
//    @Test
//    @Tag("unit")
//    void findResidentialRangeCep_withZipCodeThatDoesnCount_thenReturnEmptyList() {
//
//        //Act
//        List<ResidentialRangeCep> listOfResidentialRangeCepByZipCode = residentialRangeCepRepository
//                .findByPostalCodeStartLessThanEqualAndPostalCodeEndGreaterThanEqual("45612300", "45612300");
//
//        //Asserts
//        assertThat(listOfResidentialRangeCepByZipCode).isEmpty();
//
//    }
//
//    private ResidentialRangeCep createDummyObjects() {
//        var residentialRangeCep1 = DummyObjectsUtil.newInstance(ResidentialRangeCep.class);
//        var residentialRangeCep2 = DummyObjectsUtil.newInstance(ResidentialRangeCep.class);
//
//        residentialRangeCep1.setId(null);
//        residentialRangeCep1.setPostalCodeStart("10000000");
//        residentialRangeCep1.setPostalCodeEnd("22355555");
//
//        residentialRangeCep2.setId(null);
//        residentialRangeCep2.setPostalCodeStart("23000000");
//        residentialRangeCep2.setPostalCodeEnd("32355555");
//
//        residentialRangeCepRepository.saveAll(List.of(residentialRangeCep1, residentialRangeCep2));
//
//        return residentialRangeCep1;
//    }
}