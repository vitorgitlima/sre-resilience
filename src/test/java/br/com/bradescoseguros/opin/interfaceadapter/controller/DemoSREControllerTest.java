package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.gateway.DemoSREGateway;
import br.com.bradescoseguros.opin.businessrule.usecase.demosre.DemoSREUseCase;
import br.com.bradescoseguros.opin.configuration.TestRedisConfiguration;
import br.com.bradescoseguros.opin.configuration.TestResilienceConfig;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import br.com.bradescoseguros.opin.interfaceadapter.repository.DemoSRERepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@AutoConfigureMockMvc
@EnableMongoRepositories
@SpringBootTest
@Import({TestResilienceConfig.class})
class DemoSREControllerTest {

    private static final String BASE_URL = "/api/sre/v1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DemoSREUseCase useCaseMock;

    @Autowired
    private DemoSREGateway demoSREGatewayMock;

    @Autowired
    private RetryRegistry retryRegistry;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private DemoSRERepository demoSRERepositoryMock;

    @MockBean
    private RestTemplate restTemplateMock;

    @MockBean
    private Logger log;

    private final static String RETRY_COSMO_CONFIG = "cosmoRetry";
    private final static String RETRY_API_CONFIG = "apiRetry";
    private final static String CB_COSMO_CONFIG = "cosmoCircuitBreaker";
    private final static String CB_API_CONFIG = "apiCircuitBreaker";

    @BeforeEach
    public void setUp() {
        circuitBreakerRegistry.circuitBreaker(CB_COSMO_CONFIG).reset();
        circuitBreakerRegistry.circuitBreaker(CB_API_CONFIG).reset();
        Mockito.reset(demoSRERepositoryMock);
        Mockito.reset(restTemplateMock);
    }

    @Test
    @Tag("comp")
    void getDemoSRE_ShouldReturnValidResult() throws Exception {
        //Arrange
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        String demoSREMockJson = new ObjectMapper().writeValueAsString(demoSREMock);
        final String url = BASE_URL + "/getDemoSRE/" + demoSREMock.getId();

        when(demoSRERepositoryMock.findById(anyInt())).thenReturn(Optional.of(demoSREMock));

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
    void getDemoSRE_ShouldRetryAndReturnException() throws Exception {
        //Arrange
        final String url = BASE_URL + "/getDemoSRE/" + 1;
        final int retriesAttemps = retryRegistry.retry(RETRY_COSMO_CONFIG).getRetryConfig().getMaxAttempts();

        when(demoSRERepositoryMock.findById(anyInt())).thenThrow(org.springframework.dao.DataAccessResourceFailureException.class);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        verify(demoSRERepositoryMock, times(retriesAttemps)).findById(anyInt());
    }

    @Test
    @Tag("comp")
    void getDemoSRE_ShouldReturnExceptionWhenCircuitIsOpened() throws Exception {
        //Arrange
        final String url = BASE_URL + "/getDemoSRE/" + 2;
        final String errorMessage = "DEMOSRE_CIRCUIT_OPENED O circuito cosmoCircuitBreaker que está registrado para esta operação está ABERTO, novas requisições estão temporariamente suspensas.";
        circuitBreakerRegistry.circuitBreaker(CB_COSMO_CONFIG).transitionToOpenState();

        when(demoSRERepositoryMock.findById(anyInt())).thenThrow(org.springframework.dao.DataAccessResourceFailureException.class);

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
    void insertDemoSRE_ShouldReturnSuccess() throws Exception {
        //Arrange
        final String url = BASE_URL + "/insertDemoSRE";
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        String demoSREMockJson = new ObjectMapper().writeValueAsString(demoSREMock);

        when(demoSRERepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
        when(demoSRERepositoryMock.insert(any(DemoSRE.class))).thenReturn(null);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(demoSREMockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @Tag("comp")
    void insertDemoSRE_ShouldReturnRegistryAlreadyExists() throws Exception {
        //Arrange
        final String url = BASE_URL + "/insertDemoSRE";
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        String demoSREMockJson = new ObjectMapper().writeValueAsString(demoSREMock);

        when(demoSRERepositoryMock.findById(anyInt())).thenReturn(Optional.of(new DemoSRE()));

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(demoSREMockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    @Tag("comp")
    void updateDemoSRE() throws Exception {
        //Arrange
        final String url = BASE_URL + "/updateDemoSRE";
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        String demoSREMockJson = new ObjectMapper().writeValueAsString(demoSREMock);

        when(demoSRERepositoryMock.findById(anyInt())).thenReturn(Optional.of(demoSREMock));
        when(demoSRERepositoryMock.save(any())).thenReturn(null);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(demoSREMockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Tag("comp")
    void removeDemoSRE() throws Exception {
        //Arrange
        final String url = BASE_URL + "/removeDemoSRE/" + 1;
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);

        when(demoSRERepositoryMock.findById(anyInt())).thenReturn(Optional.of(demoSREMock));
        doNothing().when(demoSRERepositoryMock).deleteById(anyInt());

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturnSuccess() throws Exception {
        //Arrange
        final String url = BASE_URL + "/externalApiCall/ok";
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
    void externalApiCall_RetryAndReturnException() throws Exception {
        //Arrange
        final String url = BASE_URL + "/externalApiCall/ok";
        final int retryAttempts = retryRegistry.retry(RETRY_API_CONFIG).getRetryConfig().getMaxAttempts();

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenThrow(HttpServerErrorException.class);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        verify(restTemplateMock, times(retryAttempts)).exchange(anyString(), any(HttpMethod.class), any(), eq(String.class));
    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturnExceptionWhenCircuitIsOpened() throws Exception {
        //Arrange
        final String url = BASE_URL + "/externalApiCall/ok";
        final String errorMessage = "DEMOSRE_CIRCUIT_OPENED O circuito apiCircuitBreaker que está registrado para esta operação está ABERTO, novas requisições estão temporariamente suspensas.";

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