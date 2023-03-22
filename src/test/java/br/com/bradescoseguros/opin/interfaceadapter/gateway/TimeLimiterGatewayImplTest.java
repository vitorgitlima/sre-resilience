package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void externalApiCallTimeLimiter_ThrowsInterruptedException() throws ExecutionException, InterruptedException {

        CompletableFuture<String> mock = mock(CompletableFuture.class);

        when(mock.get()).thenThrow(new InterruptedException("teste"));
        when(timeLimiterGatewayAnotation.externalApiTimeLimiterThreadPool()).thenReturn(mock);

        GatewayException gatewayException = assertThrows(GatewayException.class, () -> gateway.externalApiCallWithTimeLimiter());
        assertEquals("teste", gatewayException.getMessage());
        assertTrue(Thread.currentThread().isInterrupted());
    }

}