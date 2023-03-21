package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.configuration.TestRedisConfiguration;
import br.com.bradescoseguros.opin.configuration.TestResilienceConfig;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Import({TestResilienceConfig.class, TestRedisConfiguration.class})
public class RetryControllerTest {

    private static final String BASE_URL = "/api/sre/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplateMock;

    @Autowired
    private RetryRegistry retryRegistry;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private CrudRepository crudRepositoryMock;

    private final static String RETRY_API_CONFIG = "apiRetry";
    private final static String RETRY_COSMO_CONFIG = "cosmoRetry";

    private final static String CB_API_CONFIG = "apiCircuitBreaker";

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @BeforeEach
    public void setUp() {
        System.out.println("Active profile: " + activeProfile);
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
        final String url = BASE_URL + "/retry/db";

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
    void externalApiCallWithRetry_ShouldReturnSuccess() throws Exception {
        //Arrange
        final String url = BASE_URL + "/retry/api";
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
    void externalApiCallWithRetryAndCircuitBreaker_ShouldReturnSuccess() throws Exception {
        //Arrange
        final String url = BASE_URL + "/retry/circuitbreaker";
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
        final String url = BASE_URL + "/retry/api";
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
    void getDemoSRE_ShouldRetryAndReturnException() throws Exception {
        //Arrange
        final String url = BASE_URL + "/retry/db";
        final int retriesAttemps = retryRegistry.retry(RETRY_COSMO_CONFIG).getRetryConfig().getMaxAttempts();

        when(crudRepositoryMock.findById(anyInt())).thenThrow(org.springframework.dao.DataAccessResourceFailureException.class);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .header("trace-id", "a_huge_trace_id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        assertThat(result.getResponse().getContentAsString()).contains("a_huge_trace_id");
        verify(crudRepositoryMock, times(retriesAttemps)).findById(anyInt());
    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturnCircuitBreakerException() throws Exception {
        //Arrange
        final String url = BASE_URL + "/retry/circuitbreaker";

        circuitBreakerRegistry.circuitBreaker(CB_API_CONFIG).transitionToOpenState();

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenThrow(HttpServerErrorException.class);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.LOCKED.value());
        verify(restTemplateMock, times(0)).exchange(anyString(), any(HttpMethod.class), any(), eq(String.class));
    }

}
