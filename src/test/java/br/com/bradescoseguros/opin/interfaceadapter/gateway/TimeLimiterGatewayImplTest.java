package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeLimiterGatewayImplTest {

    @Mock
    private TimeLimiterGatewayAnotation timeLimiterGatewayAnotation;

    @InjectMocks
    private TimeLimiterGatewayImpl gateway = new TimeLimiterGatewayImpl();


    @Test
    void callExternalApiWithCompletableFuture_ThrowsInterruptedException() throws ExecutionException, InterruptedException {

        CompletableFuture<String> mock = mock(CompletableFuture.class);

        when(mock.get()).thenThrow(new InterruptedException("teste"));
        when(timeLimiterGatewayAnotation.externalApiCallWithTimeLimiter()).thenReturn(mock);

        GatewayException gatewayException = assertThrows(GatewayException.class, () -> gateway.externalApiCallWithTimeLimiter());
        assertEquals("teste", gatewayException.getMessage());
        assertTrue(Thread.currentThread().isInterrupted());
    }

    @Test
    void callDbWithCompletableFuture_ThrowsInterruptedException() throws ExecutionException, InterruptedException {

        CompletableFuture<Optional<DemoSRE>> mock = mock(CompletableFuture.class);

        when(mock.get()).thenThrow(new InterruptedException("teste"));
        when(timeLimiterGatewayAnotation.findByIdWithTimeLimiter(2)).thenReturn(mock);

        GatewayException gatewayException = assertThrows(GatewayException.class, () -> gateway.findByIdWithTimeLimiter(2));
        assertEquals("teste", gatewayException.getMessage());
        assertTrue(Thread.currentThread().isInterrupted());
    }

    @Test
    void callDbWithCompletableFuture_ThrowsGatewayException() {
        when(timeLimiterGatewayAnotation.findByIdWithTimeLimiter(anyInt())).thenThrow(new GatewayException("teste"));

        GatewayException gatewayException = assertThrows(GatewayException.class, () -> gateway.findByIdWithTimeLimiter(2));

        assertEquals("br.com.bradescoseguros.opin.businessrule.exception.GatewayException: teste", gatewayException.getMessage());

    }

}