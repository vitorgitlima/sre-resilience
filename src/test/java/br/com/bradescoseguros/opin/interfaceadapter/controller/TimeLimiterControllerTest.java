package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import br.com.bradescoseguros.opin.configuration.TestRedisConfiguration;
import br.com.bradescoseguros.opin.configuration.TestResilienceConfig;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Import({TestResilienceConfig.class, TestRedisConfiguration.class})
public class TimeLimiterControllerTest {

    private static final String BASE_URL = "/api/sre/v1";
    @Autowired
    private RetryRegistry retryRegistry;
    @MockBean
    private RestTemplate restTemplateMock;

    @MockBean
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrudRepository crudRepositoryMock;

    private final static String RETRY_API_TIME_LIMITER = "apiTimeLimiter";

    @Value("${spring.profiles.active}")
    private String activeProfile;
    @BeforeEach
    public void setUp() {
        System.out.println("Active profile: " + activeProfile);
        Mockito.reset(restTemplateMock);
    }

    @Test
    @Tag("comp")
    public void TimeLimiter_ShouldReturn503whenGatewayException() throws Exception {
        // Arrange
        final String url = BASE_URL + "/timelimiter";
        final String errorMessage = "GATEWAY_ERROR java.util.concurrent.ExecutionException: br.com.bradescoseguros.opin.businessrule.exception.GatewayException";

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenThrow(GatewayException.class);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        MetaDataEnvelope bodyResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MetaDataEnvelope.class);

        // Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        assertThat(bodyResult.getErrors()).hasSize(1);
        assertThat(bodyResult.getErrors().stream().findFirst().get().getTitle()).isEqualTo(errorMessage);

    }


    @Test
    @Tag("comp")
    public void TimeLimiter_ShouldReturn408WhenInvoked() throws Exception {
        // Arrange
        final String url = BASE_URL + "/timelimiter";
        final String errorMessage = "TIME_OUT O serviço requisitado está indisponível.";

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenAnswer((Answer<ResponseEntity>) invocation -> {
            Thread.sleep(3000);
            return new ResponseEntity<>("response", HttpStatus.OK);
        });

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        MetaDataEnvelope bodyResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MetaDataEnvelope.class);

        // Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.REQUEST_TIMEOUT.value());
        assertThat(bodyResult.getErrors()).hasSize(1);
        assertThat(bodyResult.getErrors().stream().findFirst().get().getTitle()).isEqualTo(errorMessage);

    }

    @Test
    @Tag("comp")
    public void TimeLimiterWithRetry_ShouldReturn503whenMaxRetriesExceeded() throws Exception {
        // Arrange
        final String url = BASE_URL + "/timelimiter/retry";
        final String errorMessage = "MAX_RETRIES_EXCEEDED Número máximo de tentativas excedido.";
        final int retriesAttemps = retryRegistry.retry(RETRY_API_TIME_LIMITER).getRetryConfig().getMaxAttempts();

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenAnswer((Answer<ResponseEntity>) invocation -> {
            Thread.sleep(3000);
            return new ResponseEntity<>("response", HttpStatus.OK);
        });


        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();


        MetaDataEnvelope bodyResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MetaDataEnvelope.class);

        // Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        assertThat(bodyResult.getErrors()).hasSize(1);
        assertThat(bodyResult.getErrors().stream().findFirst().get().getTitle()).isEqualTo(errorMessage);
        verify(restTemplateMock, times(retriesAttemps)).exchange(anyString(), any(HttpMethod.class), any(), eq(String.class));
    }

    @Test
    @Tag("comp")
    public void TimeLimiter_ShouldReturn200WhenNotInvoked() throws Exception {
        // Arrange
        final String url = BASE_URL + "/timelimiter";
        final String response = "ok";

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenAnswer((Answer<ResponseEntity>) invocation -> {
            Thread.sleep(1000);
            return new ResponseEntity<>(response, HttpStatus.OK);
        });

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();


        // Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(response);

    }


    @Test
    @Tag("comp")
    public void TimeLimiterWithRetry_ShouldReturn200WhenNotInvoked() throws Exception {
        // Arrange
        final String url = BASE_URL + "/timelimiter/retry";
        final String response = "ok";

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenAnswer((Answer<ResponseEntity>) invocation -> {
            Thread.sleep(1000);
            return new ResponseEntity<>(response, HttpStatus.OK);
        });

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();


        // Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(response);

    }
}
