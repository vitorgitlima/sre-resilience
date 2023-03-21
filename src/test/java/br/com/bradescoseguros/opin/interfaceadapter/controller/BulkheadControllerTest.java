package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.configuration.TestRedisConfiguration;
import br.com.bradescoseguros.opin.configuration.TestResilienceConfig;
import br.com.bradescoseguros.opin.external.exception.entities.MetaDataEnvelope;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.bulkhead.*;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Import({TestResilienceConfig.class, TestRedisConfiguration.class})
public class BulkheadControllerTest {

    private static final String BASE_URL = "/api/sre/v1";
    @Autowired
    private RetryRegistry retryRegistry;

    @SpyBean
    private BulkheadRegistry bulkheadRegistry;

    @Autowired
    private ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry;

    @MockBean
    private RestTemplate restTemplateMock;

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private CrudRepository crudRepositoryMock;

    @Autowired
    private MockMvc mockMvc;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final static String BULKHEAD_SEMAPHORE_CONFIG = "semaphoreBulkhead";
    private final static String BULKHEAD_THREAD_POOL_CONFIG = "bulkheadInstance";
    private final static String RETRY_API_BULKHEAD = "apiBulkhead";

    @BeforeEach
    public void setUp() {
        System.out.println("Active profile: " + activeProfile);
        Mockito.reset(restTemplateMock);
        threadPoolBulkheadRegistry.remove(BULKHEAD_THREAD_POOL_CONFIG);
    }

    @AfterEach
    public void tearsDown(){
        BulkheadConfig bulkheadConfig = bulkheadRegistry.bulkhead(BULKHEAD_SEMAPHORE_CONFIG).getBulkheadConfig();
        bulkheadRegistry.replace(BULKHEAD_SEMAPHORE_CONFIG, Bulkhead.of(BULKHEAD_SEMAPHORE_CONFIG, bulkheadConfig));
    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldRetryWhenBulkheadSemaphoreIsFull() throws Exception {
        // Arrange
        final String urlChassi = BASE_URL + "/semaphorebulkhead/retry";
        final String errorMessage = "MAX_RETRIES_EXCEEDED Número máximo de tentativas excedido.";
        final int retryBulkheadAttempts = retryRegistry.retry(RETRY_API_BULKHEAD).getRetryConfig().getMaxAttempts();

        Bulkhead bulkheadSemaphoreInstance = bulkheadRegistry.bulkhead(BULKHEAD_SEMAPHORE_CONFIG);

        bulkheadSemaphoreInstance.tryAcquirePermission();
        bulkheadSemaphoreInstance.tryAcquirePermission();

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class)))
                .thenThrow(HttpServerErrorException.class);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(urlChassi)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        MetaDataEnvelope bodyResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MetaDataEnvelope.class);

        //Assert
        assertThat(bulkheadSemaphoreInstance.getMetrics().getAvailableConcurrentCalls()).isZero();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        verify(bulkheadRegistry, times(retryBulkheadAttempts + 1)).bulkhead(BULKHEAD_SEMAPHORE_CONFIG);
        assertThat(bodyResult.getErrors().stream().findFirst().get().getTitle()).isEqualTo(errorMessage);

    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturn503WhenTheThreadPoolBulkheadReturnsError() throws Exception {

        final String url = BASE_URL + "/threadpoolbulkhead";

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenThrow(HttpServerErrorException.class);

        ThreadPoolBulkhead bulkheadInstance = threadPoolBulkheadRegistry.bulkhead(BULKHEAD_THREAD_POOL_CONFIG);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        MetaDataEnvelope bodyResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MetaDataEnvelope.class);

        //Assert
        assertThat(bulkheadInstance.getMetrics().getRemainingQueueCapacity()).isEqualTo(1);
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        assertThat(bodyResult.getErrors()).hasSize(1);

    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturn503WhenTheThreadPoolBulkheadIsFull() throws Exception {

        final String url = BASE_URL + "/threadpoolbulkhead";
        final String errorMessage = "BULKHEAD_FULL O serviço requisitado está indisponível.";

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class)))
                .thenThrow(HttpServerErrorException.class);

        ThreadPoolBulkhead bulkheadInstance = threadPoolBulkheadRegistry.bulkhead(BULKHEAD_THREAD_POOL_CONFIG);

        bulkheadInstance.executeRunnable(this::runUselessTask);
        bulkheadInstance.executeRunnable(this::runUselessTask);


        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        MetaDataEnvelope bodyResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MetaDataEnvelope.class);

        //Assert
        assertThat(bulkheadInstance.getMetrics().getRemainingQueueCapacity()).isEqualTo(0);
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        assertThat(bodyResult.getErrors()).hasSize(1);
        assertThat(bodyResult.getErrors().stream().findFirst().get().getTitle()).isEqualTo(errorMessage);

    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturn503WhenBulkheadSemaphoreIsFull() throws Exception {
        // Arrange
        final String url = BASE_URL + "/semaphorebulkhead";
        final String errorMessage = "BULKHEAD_FULL O serviço requisitado está indisponível.";

        Bulkhead bulkheadSemaphoreInstance = bulkheadRegistry.bulkhead(BULKHEAD_SEMAPHORE_CONFIG);

        bulkheadSemaphoreInstance.tryAcquirePermission();
        bulkheadSemaphoreInstance.tryAcquirePermission();


        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        MetaDataEnvelope bodyResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MetaDataEnvelope.class);

        //Assert
        assertThat(bulkheadSemaphoreInstance.getMetrics().getAvailableConcurrentCalls()).isEqualTo(0);
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        assertThat(bodyResult.getErrors().stream().findFirst().get().getTitle()).isEqualTo(errorMessage);

    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturn200WhenBulkheadSemaphoreIsEmpty() throws Exception {
        // Arrange
        final String url = BASE_URL + "/semaphorebulkhead";
        final String response = "ok";
        Bulkhead bulkheadSemaphoreInstance = bulkheadRegistry.bulkhead(BULKHEAD_SEMAPHORE_CONFIG);


        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(bulkheadSemaphoreInstance.getMetrics().getAvailableConcurrentCalls()).isEqualTo(2);
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(response);

    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturn200WhenBulkheadRetryIsEmpty() throws Exception {
        // Arrange
        final String url = BASE_URL + "/semaphorebulkhead/retry";
        final String response = "ok";
        Bulkhead bulkheadSemaphoreInstance = bulkheadRegistry.bulkhead(BULKHEAD_SEMAPHORE_CONFIG);


        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(bulkheadSemaphoreInstance.getMetrics().getAvailableConcurrentCalls()).isEqualTo(2);
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(response);

    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldRetryWithBulkheadSemaphore() throws Exception {
        // Arrange
        final String urlChassi = BASE_URL + "/semaphorebulkhead/retry";
        final String errorMessage = "MAX_RETRIES_EXCEEDED Número máximo de tentativas excedido.";
        final int retryBulkheadAttempts = retryRegistry.retry(RETRY_API_BULKHEAD).getRetryConfig().getMaxAttempts();

        Bulkhead bulkheadSemaphoreInstance = bulkheadRegistry.bulkhead(BULKHEAD_SEMAPHORE_CONFIG);

        bulkheadSemaphoreInstance.tryAcquirePermission();


        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class)))
                .thenThrow(HttpServerErrorException.class);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(urlChassi)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        MetaDataEnvelope bodyResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), MetaDataEnvelope.class);

        //Assert
        assertThat(bulkheadSemaphoreInstance.getMetrics().getAvailableConcurrentCalls()).isEqualTo(1);
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
        verify(restTemplateMock, times(retryBulkheadAttempts)).exchange(anyString(), any(HttpMethod.class), any(), eq(String.class));
        assertThat(bodyResult.getErrors().stream().findFirst().get().getTitle()).isEqualTo(errorMessage);
    }

    @Test
    @Tag("comp")
    void externalApiCall_ShouldReturn200WhenTheThreadPoolBulkheadIsEmpty() throws Exception {
        //Arrange
        final String url = BASE_URL + "/threadpoolbulkhead";
        final String response = "ok";
        ThreadPoolBulkhead bulkheadInstance = threadPoolBulkheadRegistry.bulkhead(BULKHEAD_THREAD_POOL_CONFIG);


        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));


        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(bulkheadInstance.getMetrics().getRemainingQueueCapacity()).isEqualTo(1);
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(response);

    }

    private void runUselessTask() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
