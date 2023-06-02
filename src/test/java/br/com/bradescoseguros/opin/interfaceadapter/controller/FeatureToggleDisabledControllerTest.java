package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.gateway.FeatureToggleGateway;
import br.com.bradescoseguros.opin.businessrule.usecase.FeatureToggleUseCase;
import br.com.bradescoseguros.opin.configuration.TestRedisConfiguration;
import br.com.bradescoseguros.opin.configuration.TestResilienceConfig;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import com.azure.spring.cloud.feature.management.FeatureManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Import({TestResilienceConfig.class, TestRedisConfiguration.class})
@TestPropertySource(properties = "feature-management.feature-c=false")
public class FeatureToggleDisabledControllerTest {

    private static final String BASE_URL = "/api/sre/v1/featuretoggle";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeatureToggleUseCase featureToggleUseCase;

    @MockBean
    private FeatureToggleGateway mockGateway;

    @Mock
    private FeatureManager featureManager;

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private CrudRepository crudRepositoryMock;


    @Test
    @Tag("comp")
    void getDemoSRE_ToggleWithFallbackControllerShouldCallFallbackUrl() throws Exception {
        final String url = BASE_URL + "/fallback";

        //Arrange
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        String demoSREMockJson = new ObjectMapper().writeValueAsString(demoSREMock);

        when(mockGateway.findByIdWithFeatureToggle(anyInt())).thenReturn(Optional.of(demoSREMock));

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(redirectedUrl("/api/sre/v1/featuretoggle"))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FOUND.value());
    }
}
