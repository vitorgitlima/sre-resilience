package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import br.com.bradescoseguros.opin.businessrule.gateway.DemoSREGateway;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.domain.demosre.ExtraStatusCode;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import br.com.bradescoseguros.opin.interfaceadapter.repository.DemoSRERepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DemoSRETimeLimiterGatewayImplTest {

    @Mock
    private DemoSREGatewayTimeLimiterAnotation demoSREGatewayTimeLimiterAnotation;

    @InjectMocks
    private DemoSRETimeLimiterGatewayImpl gateway = new DemoSRETimeLimiterGatewayImpl();



    @Test
    void externalApiCallTimeLimiter_ThrowsInterruptedException() throws ExecutionException, InterruptedException {

        CompletableFuture<String> mock = mock(CompletableFuture.class);

        when(mock.get()).thenThrow(new InterruptedException("teste"));
        when(demoSREGatewayTimeLimiterAnotation.externalApiTimeLimiterThreadPool()).thenReturn(mock);

        GatewayException gatewayException = assertThrows(GatewayException.class, () -> gateway.externalApiCallTimeLimiter());
        assertEquals("teste", gatewayException.getMessage());
        assertTrue(Thread.currentThread().isInterrupted());
    }

}