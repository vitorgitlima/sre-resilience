package br.com.bradescoseguros.opin.interfaceadapter.repository;

import br.com.bradescoseguros.opin.ContextTest;
import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//class ResidentialRepositoryTest extends ContextTest {
class ResidentialRepositoryTest {

//    private static final String CODE_01 = "code01";
//    private final ResidentialRepository residentialRepository;
//
//    private Pageable pageable;
//
//    @Autowired
//    ResidentialRepositoryTest(ResidentialRepository repository) {
//        this.residentialRepository = repository;
//    }
//
//    @BeforeEach
//    public void cleanUp() {
//        pageable = Pageable.unpaged();
//        residentialRepository.deleteAll();
//    }
//
//    @Test
//    @Tag("unit")
//    void findByProductCode_withParametersPageableAndProductsCode_shouldReturnResidentialList() {
//
//        //Arrange
//        final ResidentialDomain residentialDomainToCheck = createContentList();
//
//        //Act
//        final Page<ResidentialDomain> response = residentialRepository.findByProductCodeIn(pageable, List.of(CODE_01));
//
//        //Asserts
//        assertThat(response).isNotEmpty();
//        final List<ResidentialDomain> content = response.getContent();
//        assertThat(content).isNotEmpty();
//        assertThat(content.size()).isEqualTo(1);
//        final ResidentialDomain residentialDomain = content.get(0);
//        assertThat(residentialDomain)
//                .isNotNull()
//                .usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(residentialDomainToCheck);
//    }
//
//    @Test
//    @Tag("unit")
//    void findByProductCode_withoutResponse_shouldReturnAnEmptyList() {
//
//        //Act
//        Page<ResidentialDomain> response = residentialRepository.findByProductCodeIn(pageable, List.of(CODE_01));
//
//        //Asserts
//        assertThat(response).isEmpty();
//    }
//
//    private ResidentialDomain createContentList() {
//
//        var firstResidential = DummyObjectsUtil.newInstance(ResidentialDomain.class);
//        var secondResidential = DummyObjectsUtil.newInstance(ResidentialDomain.class);
//        firstResidential.setId(null);
//        firstResidential.getProduct().setCode(CODE_01);
//        secondResidential.setId(null);
//
//        residentialRepository.saveAll(List.of(firstResidential, secondResidential));
//
//        return firstResidential;
//    }
}