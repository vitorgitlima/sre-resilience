package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.configuration.TestRedisConfiguration;
import br.com.bradescoseguros.opin.configuration.TestResilienceConfig;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Import({TestResilienceConfig.class, TestRedisConfiguration.class})
public class CircuitBreakerControllerTest {

    private static final String BASE_URL = "/api/sre/v1";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private CrudRepository crudRepositoryMock;

    @MockBean
    private RestTemplate restTemplateMock;

    private final static String CB_COSMO_CONFIG = "cosmoCircuitBreaker";
    private final static String CB_API_CONFIG = "apiCircuitBreaker";

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @BeforeEach
    public void setUp() {
        System.out.println("Active profile: " + activeProfile);
        circuitBreakerRegistry.circuitBreaker(CB_COSMO_CONFIG).reset();
        circuitBreakerRegistry.circuitBreaker(CB_API_CONFIG).reset();
        Mockito.reset(crudRepositoryMock);
        Mockito.reset(restTemplateMock);

    }



    @Test
    @Tag("comp")
    void getDemoSRE_ShouldReturnValidResult() throws Exception {
        //Arrange
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        String demoSREMockJson = new ObjectMapper().writeValueAsString(demoSREMock);
        final String url = BASE_URL + "/circuitbreaker/db";

        when(crudRepositoryMock.findById(anyInt())).thenReturn(Optional.of(demoSREMock));


        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(demoSREMockJson);
    }

    @Test
    @Tag("comp")
    void getDemoSRE_ShouldReturnNotFound() throws Exception {
        //Arrange
        final String url = BASE_URL + "/circuitbreaker/db";

        when(crudRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());


        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();


        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Tag("comp")
    void getDemoSRE_ShouldReturnExceptionWhenCircuitIsOpened() throws Exception {
        //Arrange
        final String url = BASE_URL + "/circuitbreaker/db";
        final String errorMessage = "LOCKED O circuito cosmoCircuitBreaker que está registrado para esta operação está ABERTO, novas requisições estão temporariamente suspensas.";
        circuitBreakerRegistry.circuitBreaker(CB_COSMO_CONFIG).transitionToOpenState();

        when(crudRepositoryMock.findById(anyInt())).thenThrow(org.springframework.dao.DataAccessResourceFailureException.class);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        MetaDataEnvelope bodyResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MetaDataEnvelope.class);

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.LOCKED.value());
        assertThat(bodyResult.getErrors()).hasSize(1);
        assertThat(bodyResult.getErrors().stream().findFirst().get().getTitle()).isEqualTo(errorMessage);
    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturnSuccess() throws Exception {
        //Arrange
        final String url = BASE_URL + "/circuitbreaker/api";
        final String response = "ok";

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(response);
    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturnExceptionWhenCircuitIsOpened() throws Exception {
        //Arrange
        final String url = BASE_URL + "/circuitbreaker/api";
        final String errorMessage = "LOCKED O circuito apiCircuitBreaker que está registrado para esta operação está ABERTO, novas requisições estão temporariamente suspensas.";

        circuitBreakerRegistry.circuitBreaker(CB_API_CONFIG).transitionToOpenState();

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenThrow(HttpServerErrorException.class);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        MetaDataEnvelope bodyResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MetaDataEnvelope.class);

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.LOCKED.value());
        assertThat(bodyResult.getErrors()).hasSize(1);
        assertThat(bodyResult.getErrors().stream().findFirst().get().getTitle()).isEqualTo(errorMessage);
    }
}
